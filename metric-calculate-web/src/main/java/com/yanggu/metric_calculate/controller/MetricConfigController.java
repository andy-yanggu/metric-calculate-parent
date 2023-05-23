package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.metric_config.MetricConfigClient;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * 指标配置接口
 */
@Slf4j
@RestController
@Api(tags = "指标配置接口")
@RequestMapping("/metric-config")
public class MetricConfigController implements ApplicationRunner {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final Striped<ReadWriteLock> readWriteLockStriped = Striped.lazyWeakReadWriteLock(20);

    @Autowired
    private MetricConfigClient metricConfigClient;

    @Autowired
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void run(ApplicationArguments args) {
        //项目启动时, 初始化指标配置数据
        queryMetric();
    }

    /**
     * 定期刷新指标元数据
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledRefreshMetric() {
        queryMetric();
    }

    @ApiOperation("全量更新指标配置")
    @GetMapping("/refresh-metric-config")
    public ApiResponse<Object> refreshMetric() {
        queryMetric();
        return ApiResponse.success();
    }

    @ApiOperation("增量更新指标配置")
    @PutMapping("/update-metric-config")
    public ApiResponse<Object> updateDeriveMetric(@ApiParam("数据明细宽表id") @RequestParam Long tableId) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();

        DataDetailsWideTable table = metricConfigClient.getTableAndMetricByTableId(tableId);
        if (table == null || table.getId() == null) {
            throw new RuntimeException("传入的tableId: " + tableId + "有误");
        }

        //初始化指标计算类
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(table);

        //更新指标数据
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            metricMap.put(tableId, metricCalculate);
        } finally {
            writeLock.unlock();
        }
        return apiResponse;
    }

    public MetricCalculate getMetricCalculate(Long tableId) {
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock readLock = readWriteLock.readLock();

        //先上读锁
        readLock.lock();
        MetricCalculate dataWideTable;
        try {
            dataWideTable = metricMap.get(tableId);
            //如果缓存中存在直接return
            if (dataWideTable != null) {
                return dataWideTable;
            }
        } finally {
            //释放读锁
            readLock.unlock();
        }
        Lock writeLock = readWriteLock.writeLock();
        //如果缓存中不存在上写锁
        writeLock.lock();
        try {
            //double check防止多次读取数据
            dataWideTable = metricMap.get(tableId);
            if (dataWideTable == null) {
                dataWideTable = buildMetric(tableId);
                metricMap.put(tableId, dataWideTable);
            }
        } finally {
            //释放写锁
            writeLock.unlock();
        }
        return dataWideTable;
    }

    /**
     * 从数据库加载指标定义
     */
    private void queryMetric() {
        log.info("load metric from DB");
        //获取所有宽表id
        List<Long> allTableId = metricConfigClient.getAllTableId();
        allTableId.parallelStream().forEach(tempTableId -> {
            ReadWriteLock readWriteLock = readWriteLockStriped.get(tempTableId);
            Lock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            try {
                MetricCalculate metricCalculate = buildMetric(tempTableId);
                metricMap.put(tempTableId, metricCalculate);
            } finally {
                writeLock.unlock();
            }
        });
        //删除metricMap不存在allTableId的元素
        List<Long> oldIdList = new ArrayList<>(metricMap.keySet());
        if (allTableId.retainAll(oldIdList)) {
            allTableId.forEach(metricMap::remove);
        }
    }

    private MetricCalculate buildMetric(Long tableId) {
        //根据明细宽表id查询指标数据和宽表数据
        DataDetailsWideTable tableData = metricConfigClient.getTableAndMetricByTableId(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tableData);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return metricCalculate;
        }
        //设置外部存储
        deriveMetricCalculateList.forEach(temp -> temp.setDeriveMetricMiddleStore(deriveMetricMiddleStore));
        return metricCalculate;
    }

}
