package com.yanggu.metric_calculate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.client.magiccube.MagicCubeClient;
import com.yanggu.metric_calculate.client.magiccube.pojo.*;
import com.yanggu.metric_calculate.core.calculate.*;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.core.constant.Constant.*;
import static com.yanggu.metric_calculate.core.enums.MetricTypeEnum.*;

@Slf4j
@Api(tags = "规则引擎接口")
@RestController
@RequestMapping("/engine")
public class RuleCanvasEngine {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

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
    public ApiResponse<Object> execute(@ApiParam("明细宽表数据") @RequestBody JSONObject message) throws Exception {
        ApiResponse<Object> response = new ApiResponse<>();

        /*映射明细宽表*/
        Long tableId = message.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId");
        }
        MetricCalculate dataWideTable = metricMap.get(tableId);
        if (dataWideTable == null) {
            dataWideTable = buildMetric(tableId);
        }

        //原子指标计算结果
        Map<String, Object> atomicResultMap = new ConcurrentHashMap<>();
        //计算原子指标
        List<AtomMetricCalculate> atomMetricCalculateList = dataWideTable.getAtomMetricCalculateList();
        if (CollUtil.isNotEmpty(atomMetricCalculateList)) {
            atomMetricCalculateList.parallelStream().forEach(atomMetricCalculate -> {
                Object exec = atomMetricCalculate.exec(message);
                if (exec != null) {
                    atomicResultMap.put(atomMetricCalculate.getName(), exec);
                }
            });
        }

        if (log.isDebugEnabled()) {
            log.debug("原子指标计算后的数据: {}", JSONUtil.toJsonStr(atomicResultMap));
        }

        //派生指标计算结果
        Map<String, DeriveMetricCalculateResult> deriveResultMap = new ConcurrentHashMap<>();
        //计算衍生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
                TimedKVMetricCube<?, ?> exec = deriveMetricCalculate.exec(message);
                if (exec != null) {
                    List<DeriveMetricCalculateResult> query = deriveMetricCalculate.query(exec);
                    query.forEach(temp -> deriveResultMap.put(temp.getName(), temp));
                }
            });
        }

        if (log.isDebugEnabled()) {
            log.debug("派生指标计算后的数据: {}", JSONUtil.toJsonStr(deriveResultMap));
        }

        //派生指标计算结果
        Map<String, Object> compositeResultMap = new ConcurrentHashMap<>();
        //计算复合指标
        MetricCalculate finalDataWideTable = dataWideTable;
        List<CompositeMetricCalculate> compositeMetricCalculateList = dataWideTable.getCompositeMetricCalculateList();
        if (CollUtil.isNotEmpty(compositeMetricCalculateList)) {
            compositeMetricCalculateList.parallelStream().forEach(temp -> {
                //准备计算参数
                Map<String, Object> env = new HashMap<>();
                //放入原始指标数据
                env.put(ORIGIN_DATA, message);
                //放入指标元数据信息
                env.put(METRIC_CALCULATE, finalDataWideTable);
                //放入复合指标元数据
                env.put(COMPOSITE_METRIC_META_DATA, temp);

                //计算数据
                Object exec = temp.exec(env);
                //输出到下游
                if (exec != null) {
                    compositeResultMap.put(temp.getName(), exec);
                }
            });
        }

        if (log.isDebugEnabled()) {
            log.debug("复合指标计算后的数据: {}", JSONUtil.toJsonStr(compositeResultMap));
        }
        return response;
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

        //根据明细宽表id查询指标数据和宽表数据
        DataDetailsWideTable tableData = magiccubeClient.getTableAndMetricById(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }

        MetricCalculate metricCalculate = BeanUtil.copyProperties(tableData, MetricCalculate.class);

        Map<String, MetricTypeEnum> metricTypeMap = new HashMap<>();
        metricCalculate.setMetricTypeMap(metricTypeMap);

        //宽表字段
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);

        //原子指标
        List<Atom> atomList = tableData.getAtom();
        if (CollUtil.isNotEmpty(atomList)) {
            List<AtomMetricCalculate> collect = atomList.stream().map(tempAtom -> {
                metricTypeMap.put(tempAtom.getName(), ATOM);
                //初始化原子指标计算类
                return MetricUtil.initAtom(tempAtom, fieldMap);
            }).collect(Collectors.toList());
            metricCalculate.setAtomMetricCalculateList(collect);
        }

        //派生指标
        List<Derive> deriveList = tableData.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            List<DeriveMetricCalculate> collect = deriveList.stream().map(tempDerive -> {
                metricTypeMap.put(tempDerive.getName(), DERIVE);

                //初始化派生指标计算类
                DeriveMetricCalculate deriveMetricCalculate = MetricUtil.initDerive(tempDerive, fieldMap);

                return deriveMetricCalculate;
            }).collect(Collectors.toList());

            metricCalculate.setDeriveMetricCalculateList(collect);
        }

        //复合指标
        List<Composite> compositeList = tableData.getComposite();
        if (CollUtil.isNotEmpty(compositeList)) {
            List<CompositeMetricCalculate> collect = new ArrayList<>();
            compositeList.forEach(compositeMetric -> {
                metricTypeMap.put(compositeMetric.getName(), COMPOSITE);

                //初始化复合指标计算类
                List<CompositeMetricCalculate> compositeMetricCalculateList = MetricUtil.initComposite(compositeMetric);
                collect.addAll(compositeMetricCalculateList);
            });
            metricCalculate.setCompositeMetricCalculateList(collect);
        }

        //全局指标
        List<Global> globalList = tableData.getGlobal();
        if (CollUtil.isNotEmpty(globalList)) {
            globalList.forEach(temp -> metricTypeMap.put(temp.getName(), GLOBAL));
        }

        metricMap.put(tableId, metricCalculate);
        return metricCalculate;
    }

}
