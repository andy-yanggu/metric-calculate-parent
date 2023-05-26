package com.yanggu.metric_calculate.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.metric_config.MetricConfigClient;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

/**
 * 指标配置Service
 */
@Slf4j
@Service
public class MetricConfigService implements ApplicationRunner {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final Striped<ReadWriteLock> readWriteLockStriped = Striped.lazyWeakReadWriteLock(20);

    @Autowired
    private MetricConfigClient metricConfigClient;

    @Autowired
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Override
    public void run(ApplicationArguments args) {
        //项目启动时, 初始化指标配置数据
        buildAllMetric();
    }

    /**
     * 获取当前的指标配置数据
     *
     * @return
     */
    public List<DataDetailsWideTable> allMetricConfigData() {
        if (CollUtil.isEmpty(metricMap)) {
            return Collections.emptyList();
        }
        return metricMap.values().stream()
                .map(temp -> BeanUtil.copyProperties(temp, DataDetailsWideTable.class))
                .collect(Collectors.toList());
    }

    /**
     * 增量更新指标配置（更新某个宽表下的所有指标）
     *
     * @param tableId
     * @return
     */
    public void updateTable(Long tableId) {
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
    }

    /**
     * 获取指标计算类
     *
     * @param tableId
     * @return
     */
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
    public void buildAllMetric() {
        log.info("load metric from DB");
        //获取所有宽表id
        List<Long> allTableId = metricConfigClient.getAllTableId();
        metricMap.clear();
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
    }

    private MetricCalculate buildMetric(Long tableId) {
        //根据明细宽表id查询指标数据和宽表数据
        DataDetailsWideTable tableData = metricConfigClient.getTableAndMetricByTableId(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }
        //初始化指标计算类
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
