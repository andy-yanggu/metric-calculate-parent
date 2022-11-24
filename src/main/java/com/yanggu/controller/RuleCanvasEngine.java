package com.yanggu.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.yanggu.client.magiccube.MagicCubeClient;
import com.yanggu.client.magiccube.pojo.Atom;
import com.yanggu.client.magiccube.pojo.Composite;
import com.yanggu.client.magiccube.pojo.DataDetailsWideTable;
import com.yanggu.client.magiccube.pojo.Derive;
import com.yanggu.metriccalculate.aviatorfunction.CoalesceFunction;
import com.yanggu.metriccalculate.aviatorfunction.GetFunction;
import com.yanggu.metriccalculate.dingo.DingoService;
import com.yanggu.metriccalculate.calculate.AtomMetricCalculate;
import com.yanggu.metriccalculate.calculate.CompositeMetricCalculate;
import com.yanggu.metriccalculate.calculate.DeriveMetricCalculate;
import com.yanggu.metriccalculate.calculate.MetricCalculate;
import com.yanggu.metriccalculate.util.MetricUtil;
import com.yanggu.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metriccalculate.constant.Constant.*;

@Slf4j
@Api(tags = "规则引擎接口")
@RestController
@RequestMapping("/engine")
public class RuleCanvasEngine {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final AtomicBoolean refreshMetric = new AtomicBoolean(true);

    private final AtomicBoolean refreshRule = new AtomicBoolean(true);

    @Autowired
    private MagicCubeClient magiccubeClient;

    @Autowired
    private DingoService dingoService;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void scheduledRefreshMetric() {
        refreshMetric.set(true);
        log.info("scheduledRefreshMetric, refreshMetric={}", refreshMetric.get());
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void scheduledRefreshRule() {
        refreshRule.set(true);
        log.info("scheduledRefreshRule, refreshRule={}", refreshRule.get());
    }

    @ApiOperation("刷新指标接口")
    @GetMapping("/manualRefreshMetric")
    public ApiResponse<Object> manualRefreshMetric() {
        ApiResponse<Object> response = new ApiResponse<>();
        refreshMetric.set(true);
        log.info("manualRefreshMetric, refreshMetric={}", refreshMetric.get());
        return response;
    }

    @ApiOperation("刷新规则接口")
    @GetMapping("/manualRefreshRule")
    public ApiResponse<Object> manualRefreshRule() {
        ApiResponse<Object> response = new ApiResponse<>();
        refreshRule.set(true);
        log.info("manualRefreshRule, refreshRule={}", refreshRule.get());
        return response;
    }

    @ApiOperation("执行接口")
    @PostMapping("/execute")
    public ApiResponse<Object> execute(@RequestBody JSONObject message) throws Exception {
        ApiResponse<Object> response = new ApiResponse<>();
        /*刷新指标*/
        if (refreshMetric.compareAndSet(true, false)) {
            queryMetric();
        }

        /*映射明细宽表*/
        Long tableId = message.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId");
        }
        MetricCalculate dataWideTable = metricMap.get(tableId);
        if (dataWideTable == null) {
            dataWideTable = buildMetric(tableId);
        }

        Map<String, Object> resultMap = new HashMap<>();

        //计算原子指标
        List<AtomMetricCalculate> atomMetricCalculateList = dataWideTable.getAtomMetricCalculateList();
        if (CollUtil.isNotEmpty(atomMetricCalculateList)) {
            for (AtomMetricCalculate atomMetricCalculate : atomMetricCalculateList) {
                Object exec = atomMetricCalculate.exec(message);
                if (exec != null) {
                    resultMap.put(atomMetricCalculate.getName(), exec);
                }
            }
        }

        //计算衍生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isNotEmpty(deriveMetricCalculateList)) {
            for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
                Object exec = deriveMetricCalculate.exec(message);
                if (exec != null) {
                    resultMap.put(deriveMetricCalculate.getName(), exec);
                }
            }
        }

        //计算复合指标
        //准备计算参数
        Map<String, Object> env = new HashMap<>();
        //放入原始指标数据
        env.put(ORIGIN_DATA, message);

        //放入指标元数据信息
        //原子指标
        Map<String, Atom> atomMap = dataWideTable.getAtom().stream()
                .collect(Collectors.toMap(Atom::getName, Function.identity()));
        env.put(ATOM_METRIC_META_DATA, atomMap);

        //衍生指标
        Map<String, DeriveMetricCalculate> metricMetaDataMap = deriveMetricCalculateList.stream()
                .collect(Collectors.toMap(DeriveMetricCalculate::getName, Function.identity()));
        env.put(DERIVE_METRIC_META_DATA, metricMetaDataMap);

        //放入dingo连接信息
        env.put(DINGO_CLIENT, dingoService.getDingoClient());

        //执行复合指标的计算操作
        //dataWideTable.getCompositeMetricCalculateList().parallelStream()
        //        .forEach(temp -> {
        //            Object exec = temp.exec(env);
        //            //输出到下游
        //            if (exec != null) {
        //                resultMap.put(temp.getName(), exec);
        //            }
        //        });

        if (log.isDebugEnabled()) {
            log.debug("原子指标、衍生指标、复合指标计算后的数据: {}", JSONUtil.toJsonStr(resultMap));
        }

        /*根据明细宽表过滤指标*/

        /*指标查询&加工*/

        /*刷新规则*/
        if (refreshRule.compareAndSet(true, false)) {
            queryRule();
        }

        /*查询规则的指标*/

        /*规则表达式执行*/

        return response;
    }

    /** 从数据库加载指标定义 */
    private void queryMetric() {
        log.info("load metric from DB");
        if (CollUtil.isEmpty(metricMap)) {
            return;
        }
        Set<Long> tableIdSet = metricMap.keySet();

        tableIdSet.forEach(this::buildMetric);
    }

    /** 从数据库加载规则定义 */
    private void queryRule() {
        log.info("load rule from DB");
    }

    private MetricCalculate buildMetric(Long tableId) {

        DataDetailsWideTable tableData = magiccubeClient.getTableAndMetricById(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }

        MetricCalculate metricCalculate = BeanUtil.copyProperties(tableData, MetricCalculate.class);

        //宽表字段
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);

        //原子指标
        List<Atom> atomList = tableData.getAtom();
        if (CollUtil.isNotEmpty(atomList)) {
            List<AtomMetricCalculate> collect = atomList.stream()
                    //初始化原子指标
                    .map(tempAtom -> MetricUtil.initAtom(tempAtom, fieldMap))
                    .collect(Collectors.toList());
            metricCalculate.setAtomMetricCalculateList(collect);
        }

        //派生指标
        List<Derive> deriveList = tableData.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            List<DeriveMetricCalculate> collect = deriveList.stream()
                    .map(tempDerive -> {

                        //初始化派生指标
                        DeriveMetricCalculate deriveMetricCalculate = MetricUtil.initDerive(tempDerive, fieldMap);

                        //设置dingo客户端
                        deriveMetricCalculate.setDingoClient(dingoService.getDingoClient());

                        return deriveMetricCalculate;
                    })
                    .collect(Collectors.toList());

            metricCalculate.setDeriveMetricCalculateList(collect);
        }

        //复合指标
        List<Composite> compositeList = tableData.getComposite();
        if (CollUtil.isNotEmpty(compositeList)) {
            List<CompositeMetricCalculate> collect = compositeList.stream()
                    .map(compositeMetric -> {
                        CompositeMetricCalculate compositeMetricCalculate = new CompositeMetricCalculate();

                        //设置表达式字符串
                        String expression = compositeMetric.getCalculateExpression();
                        compositeMetricCalculate.setExpressString(expression);

                        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
                        //在Aviator中添加自定义函数
                        instance.addFunction(new GetFunction());
                        instance.addFunction(new CoalesceFunction());
                        instance.setOption(Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY, false);
                        Expression compile = instance.compile(expression, true);
                        compositeMetricCalculate.setExpression(compile);

                        List<String> variableNames = compile.getVariableNames();
                        compositeMetricCalculate.setParamList(variableNames);

                        //设置名称
                        compositeMetricCalculate.setName(compositeMetric.getName());
                        //设置精度信息
                        compositeMetricCalculate.setRoundAccuracy(compositeMetric.getRoundAccuracy());

                        return compositeMetricCalculate;
                    })
                    .collect(Collectors.toList());

            metricCalculate.setCompositeMetricCalculateList(collect);
        }

        metricMap.put(tableId, metricCalculate);
        return metricCalculate;
    }

}
