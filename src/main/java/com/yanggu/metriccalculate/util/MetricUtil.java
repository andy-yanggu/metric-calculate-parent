package com.yanggu.metriccalculate.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.yanggu.client.magiccube.pojo.*;
import com.yanggu.metriccalculate.aviatorfunction.CoalesceFunction;
import com.yanggu.metriccalculate.aviatorfunction.GetFunction;
import com.yanggu.metriccalculate.calculate.*;
import com.yanggu.metriccalculate.fieldprocess.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    /**
     * 初始化原子指标计算类
     * @param atom
     * @param fieldMap
     * @return
     */
    public static AtomMetricCalculate initAtom(Atom atom, Map<String, Class<?>> fieldMap) {
        AtomMetricCalculate atomMetricCalculate = new AtomMetricCalculate<>();

        //设置名称
        atomMetricCalculate.setName(atom.getName());

        //设置前置过滤条件处理器
        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, atom.getFilter());
        try {
            filterProcessor.init();
        } catch (Exception e) {
            log.error("前置过滤条件处理器初始化失败, 指标的名称: " + atom.getDisplayName());
            throw new RuntimeException(e);
        }
        atomMetricCalculate.setFilterProcessor(filterProcessor);

        //度量字段处理器
        MetricFieldProcessor<?> metricFieldProcessor = new MetricFieldProcessor<>(fieldMap, atom.getMetricColumn().getColumnName());
        try {
            metricFieldProcessor.init();
        } catch (Exception e) {
            log.error("度量字段处理器初始化失败, 指标名称: " + atom.getDisplayName());
            throw new RuntimeException(e);
        }
        atomMetricCalculate.setMetricFieldProcessor(metricFieldProcessor);

        //时间字段处理器
        TimeColumn timeColumn = atom.getTimeColumn();
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
        atomMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(atom.getDimension());
        dimensionSetProcessor.setMetricName(atom.getName());
        dimensionSetProcessor.init();
        atomMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //存储宽表
        atomMetricCalculate.setStore(atom.getStore());

        return atomMetricCalculate;
    }

    /**
     * 初始化派生指标
     *
     * @param tempDerive
     * @param fieldMap
     * @return
     */
    public static DeriveMetricCalculate initDerive(Derive tempDerive, Map<String, Class<?>> fieldMap) {
        DeriveMetricCalculate deriveMetricCalculate = new DeriveMetricCalculate();

        TimedKVMetricContext timedKVMetricContext = new TimedKVMetricContext();
        timedKVMetricContext.setCache(new ConcurrentHashMap());
        deriveMetricCalculate.init(timedKVMetricContext);

        //设置名称
        deriveMetricCalculate.setName(tempDerive.getName());

        //设置前置过滤条件处理器
        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, tempDerive.getFilter());
        try {
            filterProcessor.init();
        } catch (Exception e) {
            log.error("前置过滤条件处理器初始化失败, 指标的名称: " + tempDerive.getDisplayName());
            throw new RuntimeException(e);
        }
        deriveMetricCalculate.setFilterProcessor(filterProcessor);

        //聚合字段处理器
        String columnName = tempDerive.getMetricColumn().getColumnName();
        //如果是计数, 度量值强制设定成1
        if (StrUtil.equalsIgnoreCase(tempDerive.getCalculateLogic(), "COUNT")) {
            columnName = "1";
        }

        //聚合字段处理器
        AggregateFieldProcessor<?> aggregateFieldProcessor = new AggregateFieldProcessor<>();
        aggregateFieldProcessor.setMetricExpress(columnName);
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(tempDerive.getCalculateLogic());
        try {
            aggregateFieldProcessor.init();
        } catch (Exception e) {
            log.error("聚合字段处理器初始化失败, 指标名称: " + tempDerive.getDisplayName());
            throw new RuntimeException(e);
        }
        deriveMetricCalculate.setAggregateFieldProcessor(aggregateFieldProcessor);

        //时间字段处理器
        TimeColumn timeColumn = tempDerive.getTimeColumn();
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
        deriveMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //设置时间聚合粒度
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(
                tempDerive.getDuration(), tempDerive.getTimeUnit(), timeFieldProcessor);
        deriveMetricCalculate.setTimeBaselineDimension(timeBaselineDimension);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(tempDerive.getDimension());
        dimensionSetProcessor.setMetricName(tempDerive.getName());
        dimensionSetProcessor.init();
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        //存储宽表
        deriveMetricCalculate.setStore(tempDerive.getStore());

        return deriveMetricCalculate;
    }

    /**
     * 初始化复合指标
     *
     * @param compositeMetric
     * @return
     */
    public static List<CompositeMetricCalculate> initComposite(Composite compositeMetric) {
        List<MultiDimensionCalculate> multiDimensionCalculateList = compositeMetric.getMultiDimensionCalculateList();
        if (CollUtil.isEmpty(multiDimensionCalculateList)) {
            throw new RuntimeException("复合指标多维度计算为空, 复合指标元数据: " + JSONUtil.toJsonStr(compositeMetric));
        }
        return multiDimensionCalculateList.stream()
                .map(temp -> {
                    CompositeMetricCalculate compositeMetricCalculate = new CompositeMetricCalculate();

                    //设置表达式字符串
                    String expression = temp.getCalculateExpression();
                    compositeMetricCalculate.setExpressString(expression);
                    //设置维度字段处理器
                    DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(temp.getDimension());
                    dimensionSetProcessor.init();
                    compositeMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

                    //设置时间字段处理器
                    TimeColumn timeColumn = compositeMetric.getTimeColumn();
                    TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
                    compositeMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);
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
                    //设置存储宽表
                    compositeMetricCalculate.setStore(compositeMetric.getStore());

                    return compositeMetricCalculate;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取宽表字段
     *
     * @param metricCalculate
     * @return
     */
    public static Map<String, Class<?>> getFieldMap(MetricCalculate metricCalculate) {
        //宽表字段
        Map<String, Class<?>> fieldMap = new HashMap<>();
        List<Fields> fields = metricCalculate.getFields();
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate));
        }
        fields.forEach(temp -> fieldMap.put(temp.getName(), temp.getValueType().getType()));
        return fieldMap;
    }

    /**
     * 从原始数据中提取数据, 进行手动数据类型转换
     * 防止输入的数据类型和数据明细宽表定义的数据类型不匹配
     * 主要是数值型
     * @param input
     * @param fieldMap
     * @return
     */
    public static Map<String, Object> getParam(JSONObject input, Map<String, Class<?>> fieldMap) {
        Map<String, Object> params = new HashMap<>();
        fieldMap.forEach((key, tempDataClass) -> {
            Object value = input.get(key);
            if (value == null) {
                return;
            }
            //如果是数值型的, 并且原始数据类型不是数值型的, 进行转换
            if (tempDataClass.getSuperclass().equals(Number.class) || tempDataClass.equals(Boolean.class)) {
                value = Convert.convert(tempDataClass, value);
                //如果是Boolean类型的, , 并且原始数据类型不是boolean型的, 需要进行转换
            }
            params.put(key, value);
        });
        if (CollUtil.isEmpty(params)) {
            throw new RuntimeException("没有对应的原始数据");
        }
        return params;
    }

}

