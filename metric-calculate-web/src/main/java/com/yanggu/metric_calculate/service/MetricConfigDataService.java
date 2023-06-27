package com.yanggu.metric_calculate.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.metric_config.MetricConfigClient;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.stream.Collectors;

/**
 * 指标配置数据Service
 */
@Slf4j
@Service
public class MetricConfigDataService implements ApplicationRunner {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final Striped<ReadWriteLock> readWriteLockStriped = Striped.lazyWeakReadWriteLock(20);

    @Autowired
    private MetricConfigClient metricConfigClient;

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
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
                .map(temp -> {
                    Lock readLock = readWriteLockStriped.get(temp.getId()).readLock();
                    readLock.lock();
                    try {
                        return BeanUtil.copyProperties(temp, DataDetailsWideTable.class);
                    } finally {
                        readLock.unlock();
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据宽表id获取指标配置数据
     *
     * @param tableId
     * @return
     */
    public DataDetailsWideTable metricConfigDataById(Long tableId) {
        if (CollUtil.isEmpty(metricMap)) {
            return null;
        }
        Lock readLock = readWriteLockStriped.get(tableId).readLock();
        readLock.lock();
        try {
            return Optional.ofNullable(metricMap.get(tableId))
                    .map(temp -> BeanUtil.copyProperties(temp, DataDetailsWideTable.class))
                    .orElseThrow(() -> new RuntimeException("传入的tableId: " + tableId + "有误"));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 增量更新指标配置（更新某个宽表下的所有指标）
     *
     * @param tableId
     * @return
     */
    public void updateTable(Long tableId) {
        //初始化和设置指标计算类
        buildAndSetMetricData(tableId);
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
        MetricCalculate metricCalculate;
        try {
            metricCalculate = metricMap.get(tableId);
            //如果缓存中存在直接return
            if (metricCalculate != null) {
                return metricCalculate;
            }
        } finally {
            //释放读锁
            readLock.unlock();
        }
        //如果缓存中不存在上写锁
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            //double check防止多次读取数据
            metricCalculate = metricMap.get(tableId);
            if (metricCalculate == null) {
                metricCalculate = buildMetric(tableId);
                metricMap.put(tableId, metricCalculate);
            }
        } finally {
            //释放写锁
            writeLock.unlock();
        }
        return metricCalculate;
    }

    /**
     * 从数据库加载指标定义
     */
    public synchronized void buildAllMetric() {
        log.info("初始化所有指标计算类");
        //获取所有宽表id
        List<Long> allTableId = metricConfigClient.getAllTableId();
        //删除原有的数据
        Iterator<Map.Entry<Long, MetricCalculate>> iterator = metricMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Long tempTableId = iterator.next().getKey();
            Lock writeLock = readWriteLockStriped.get(tempTableId).writeLock();
            writeLock.lock();
            try {
                iterator.remove();
            } finally {
                writeLock.unlock();
            }
        }
        if (CollUtil.isEmpty(allTableId)) {
            return;
        }
        allTableId.parallelStream().forEach(this::buildAndSetMetricData);
    }

    /**
     * 根据宽表id和派生指标id获取派生指标
     *
     * @param tableId
     * @param deriveId
     * @return
     */
    public Derive getDerive(Long tableId, Long deriveId) {
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            MetricCalculate metricCalculate = metricMap.get(tableId);
            if (metricCalculate == null) {
                return null;
            }
            List<Derive> deriveList = metricCalculate.getDerive();
            if (CollUtil.isEmpty(deriveList)) {
                return null;
            }
            return deriveList.stream()
                    .filter(tempDerive -> deriveId.equals(tempDerive.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("传入的派生指标id" + deriveId + "有误"));
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 根据宽表id和派生指标id获取派生指标计算类
     *
     * @param tableId
     * @param deriveId
     * @return
     */
    public <IN, ACC, OUT> DeriveMetricCalculate<IN, ACC, OUT> getDeriveMetricCalculateById(Long tableId, Long deriveId) {
        Lock readLock = readWriteLockStriped.get(tableId).readLock();
        readLock.lock();
        try {
            MetricCalculate metricCalculate = metricMap.get(tableId);
            if (metricCalculate == null) {
                return null;
            }
            return metricCalculate.getDeriveMetricCalculateById(deriveId);
        } finally {
            readLock.unlock();
        }
    }

    public List<DeriveMetricCalculate> getDeriveMetricCalculateList(Long tableId, List<Long> deriveIdList) {
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            MetricCalculate metricCalculate = metricMap.get(tableId);
            if (metricCalculate == null) {
                return Collections.emptyList();
            }
            return metricCalculate.getDeriveMetricCalculateListById(deriveIdList);
        } finally {
            readLock.unlock();
        }
    }

    public List<Long> getAllDeriveIdList(Long tableId) {
        Lock readLock = readWriteLockStriped.get(tableId).readLock();
        readLock.lock();
        try {
            MetricCalculate metricCalculate = metricMap.get(tableId);
            List<Derive> deriveList = metricCalculate.getDerive();
            if (CollUtil.isEmpty(deriveList)) {
                return Collections.emptyList();
            }
            return deriveList.stream()
                    .map(Derive::getId)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 初始化和设置指标计算类
     *
     * @param tableId
     */
    private void buildAndSetMetricData(Long tableId) {
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            MetricCalculate metricCalculate = buildMetric(tableId);
            metricMap.put(tableId, metricCalculate);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 初始化指标计算类
     *
     * @param tableId
     * @return
     */
    private MetricCalculate buildMetric(Long tableId) {
        //根据明细宽表id查询指标数据和宽表数据
        DataDetailsWideTable tableData = metricConfigClient.getTableAndMetricByTableId(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }
        //初始化指标计算类
        MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tableData);
        if (metricCalculate == null) {
            throw new RuntimeException("指标计算类初始化失败");
        }
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            //设置派生指标外部存储
            deriveMetricCalculateList.forEach(temp -> temp.setDeriveMetricMiddleStore(deriveMetricMiddleStore));
        }
        return metricCalculate;
    }

}
