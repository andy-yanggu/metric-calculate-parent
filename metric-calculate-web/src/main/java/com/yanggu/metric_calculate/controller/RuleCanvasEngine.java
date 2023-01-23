package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.magiccube.MagicCubeClient;
import com.yanggu.metric_calculate.core.calculate.*;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.pojo.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;

import static com.yanggu.metric_calculate.core.constant.Constant.*;

@Slf4j
@Api(tags = "规则引擎接口")
@RestController
@RequestMapping("/engine")
public class RuleCanvasEngine {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final Striped<Lock> lockStriped = Striped.lazyWeakLock(20);

    @Autowired
    private MagicCubeClient magiccubeClient;

    //定期刷新指标元数据
    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledRefreshMetric() {
        queryMetric();
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void scheduledRefreshRule() {
        queryRule();
    }

    @ApiOperation("刷新指标接口")
    @GetMapping("/manualRefreshMetric")
    public ApiResponse<?> manualRefreshMetric() {
        queryMetric();
        return ApiResponse.success();
    }

    @ApiOperation("刷新规则接口")
    @GetMapping("/manualRefreshRule")
    public ApiResponse<?> manualRefreshRule() {
        queryRule();
        return ApiResponse.success();
    }

    @ApiOperation("执行接口")
    @PostMapping("/execute")
    public ApiResponse<Map<String, Object>> execute(@ApiParam("明细宽表数据") @RequestBody JSONObject message) throws Exception {
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();

        Long tableId = message.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId");
        }
        MetricCalculate dataWideTable = metricMap.get(tableId);
        if (dataWideTable == null) {
            dataWideTable = buildMetric(tableId);
        }

        Map<String, Object> returnData = new HashMap<>();
        response.setData(returnData);

        //原子指标计算结果
        Map<String, Object> atomicResultMap = new ConcurrentHashMap<>();
        returnData.put("ATOM", atomicResultMap);
        //计算原子指标
        calcAtom(message, dataWideTable, atomicResultMap);

        //派生指标计算结果
        List<DeriveMetricCalculateResult> deriveMetricCalculateResultList = new CopyOnWriteArrayList<>();
        returnData.put("DERIVE", deriveMetricCalculateResultList);
        //计算衍生指标
        calcDerive(message, dataWideTable, deriveMetricCalculateResultList);

        //复合指标
        Map<String, Object> compositeResultMap = new ConcurrentHashMap<>();
        returnData.put("COMPOSITE", compositeResultMap);
        //计算复合指标
        calcComposite(message, dataWideTable, compositeResultMap);

        return response;
    }

    private void calcAtom(JSONObject message, MetricCalculate dataWideTable, Map<String, Object> atomicResultMap) {
        List<AtomMetricCalculate> atomMetricCalculateList = dataWideTable.getAtomMetricCalculateList();
        if (CollUtil.isEmpty(atomMetricCalculateList)) {
            return;
        }
        atomMetricCalculateList.parallelStream().forEach(atomMetricCalculate -> {
            Object exec = atomMetricCalculate.exec(message);
            if (exec != null) {
                atomicResultMap.put(atomMetricCalculate.getName(), exec);
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("原子指标计算后的数据: {}", JSONUtil.toJsonStr(atomicResultMap));
        }
    }

    private void calcDerive(JSONObject message,
                            MetricCalculate dataWideTable,
                            List<DeriveMetricCalculateResult> deriveMetricCalculateResultList) {
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return;
        }
        deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
            MetricCube<Table, Long, ?, ?> exec = deriveMetricCalculate.exec(message);
            if (exec != null) {
                List<DeriveMetricCalculateResult> query = deriveMetricCalculate.query(exec);
                if (CollUtil.isNotEmpty(query)) {
                    deriveMetricCalculateResultList.addAll(query);
                }
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("派生指标计算后的数据: {}", JSONUtil.toJsonStr(deriveMetricCalculateResultList));
        }
    }

    private void calcComposite(JSONObject message,
                               MetricCalculate dataWideTable,
                               Map<String, Object> compositeResultMap) {

        List<CompositeMetricCalculate> compositeMetricCalculateList = dataWideTable.getCompositeMetricCalculateList();
        if (CollUtil.isEmpty(compositeMetricCalculateList)) {
            return;
        }
        compositeMetricCalculateList.parallelStream().forEach(temp -> {
            //准备计算参数
            Map<String, Object> env = new HashMap<>();
            //放入原始指标数据
            env.put(ORIGIN_DATA, message);
            //放入指标元数据信息
            env.put(METRIC_CALCULATE, dataWideTable);
            //放入复合指标元数据
            env.put(COMPOSITE_METRIC_META_DATA, temp);

            //计算数据
            Object exec = temp.exec(env);
            //输出到下游
            if (exec != null) {
                compositeResultMap.put(temp.getName(), exec);
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("复合指标计算后的数据: {}", JSONUtil.toJsonStr(compositeResultMap));
        }
    }

    /**
     * 从数据库加载指标定义
     */
    private void queryMetric() {
        log.info("load metric from DB");
        if (CollUtil.isEmpty(metricMap)) {
            return;
        }
        Set<Long> tableIdSet = metricMap.keySet();
        tableIdSet.parallelStream().forEach(this::buildMetric);
    }

    /**
     * 从数据库加载规则定义
     */
    private void queryRule() {
        log.info("load rule from DB");
    }

    public MetricCalculate buildMetric(Long tableId) {
        Lock lock = lockStriped.get(tableId);
        lock.lock();
        try {
            //根据明细宽表id查询指标数据和宽表数据
            DataDetailsWideTable tableData = magiccubeClient.getTableAndMetricById(tableId);
            if (tableData == null || tableData.getId() == null) {
                log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
                throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
            }
            MetricCalculate metricCalculate = MetricUtil.initMetricCalculate(tableData);
            if (metricCalculate == null) {
                throw new RuntimeException("指标计算类初始化失败");
            }
            metricMap.put(tableId, metricCalculate);
            return metricCalculate;
        } finally {
            lock.unlock();
        }
    }

}
