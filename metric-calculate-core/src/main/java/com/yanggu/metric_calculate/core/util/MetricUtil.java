package com.yanggu.metric_calculate.core.util;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.yanggu.metric_calculate.core.aviatorfunction.CoalesceFunction;
import com.yanggu.metric_calculate.core.aviatorfunction.GetFunction;
import com.yanggu.metric_calculate.core.calculate.CompositeMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCubeFactory;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.field_process.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Fields;
import com.yanggu.metric_calculate.core.pojo.metric.*;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.core.enums.MetricTypeEnum.*;

/**
 * 指标工具类
 */
@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    /**
     * 根据指标定义元数据, 初始化指标计算类
     *
     * @param tableData
     * @return
     */
    public static <T> MetricCalculate<T> initMetricCalculate(DataDetailsWideTable tableData) {
        if (tableData == null) {
            throw new RuntimeException("明细宽表为空");
        }

        String jsonString = JSONUtil.toJsonStr(tableData);
        MetricCalculate<T> metricCalculate =
                JSONUtil.toBean(jsonString, new TypeReference<MetricCalculate<T>>() {}, false);

        Map<String, MetricTypeEnum> metricTypeMap = new HashMap<>();
        metricCalculate.setMetricTypeMap(metricTypeMap);

        //宽表字段
        Map<String, Class<?>> fieldMap = getFieldMap(metricCalculate);
        metricCalculate.setFieldMap(fieldMap);

        //派生指标
        List<Derive> deriveList = tableData.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            List<DeriveMetricCalculate<T, ?>> collect = deriveList.stream()
                    .map(tempDerive -> {
                        metricTypeMap.put(tempDerive.getName(), DERIVE);
                        //初始化派生指标计算类
                        return MetricUtil.initDerive(tempDerive, metricCalculate);
                    })
                    .collect(Collectors.toList());

            metricCalculate.setDeriveMetricCalculateList(collect);
        }

        //复合指标
        List<Composite> compositeList = tableData.getComposite();
        if (CollUtil.isNotEmpty(compositeList)) {
            List<CompositeMetricCalculate<T>> collect = new ArrayList<>();
            compositeList.forEach(compositeMetric -> {
                metricTypeMap.put(compositeMetric.getName(), COMPOSITE);

                //初始化复合指标计算类
                List<CompositeMetricCalculate<T>> compositeMetricCalculateList =
                        MetricUtil.initComposite(compositeMetric, metricCalculate);
                collect.addAll(compositeMetricCalculateList);
            });
            metricCalculate.setCompositeMetricCalculateList(collect);
        }

        //全局指标
        List<Global> globalList = tableData.getGlobal();
        if (CollUtil.isNotEmpty(globalList)) {
            globalList.forEach(temp -> metricTypeMap.put(temp.getName(), GLOBAL));
        }
        return metricCalculate;
    }

    /**
     * 初始化派生指标
     *
     * @param tempDerive
     * @return
     */
    @SneakyThrows
    private static <T, M extends MergedUnit<M> & Value<?>> DeriveMetricCalculate<T, M> initDerive(Derive tempDerive,
                                                                                                  MetricCalculate<T> metricCalculate) {
        DeriveMetricCalculate<T, M> deriveMetricCalculate = new DeriveMetricCalculate<>();
        deriveMetricCalculate.init();

        //设置名称
        String name = tempDerive.getName();
        deriveMetricCalculate.setName(name);

        //设置key
        String key = metricCalculate.getId() + "_" + tempDerive.getId();
        deriveMetricCalculate.setKey(key);

        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();

        //设置前置过滤条件处理器
        FilterFieldProcessor<T> filterFieldProcessor =
                FieldProcessorUtil.getFilterFieldProcessor(fieldMap, tempDerive.getFilter());
        deriveMetricCalculate.setFilterFieldProcessor(filterFieldProcessor);

        //设置UnitFactory, 生成MergeUnit
        UnitFactory unitFactory = new UnitFactory(tempDerive.getUdafJarPathList());
        unitFactory.init();

        //设置聚合字段处理器
        AggregateFieldProcessor<T, M> aggregateFieldProcessor = FieldProcessorUtil.getAggregateFieldProcessor(
                tempDerive, fieldMap, unitFactory);

        deriveMetricCalculate.setAggregateFieldProcessor(aggregateFieldProcessor);

        //时间字段处理器
        TimeFieldProcessor<T> timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(tempDerive.getTimeColumn());
        deriveMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //设置时间聚合粒度
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(tempDerive.getDuration(), tempDerive.getTimeUnit());
        deriveMetricCalculate.setTimeBaselineDimension(timeBaselineDimension);

        //维度字段处理器
        DimensionSetProcessor<T> dimensionSetProcessor =
                FieldProcessorUtil.getDimensionSetProcessor(key, name, fieldMap, tempDerive.getDimension());
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        //存储宽表
        deriveMetricCalculate.setStoreInfo(tempDerive.getStoreInfo());

        //设置MetricCubeFactory
        MetricCubeFactory<M> metricCubeFactory = new MetricCubeFactory<>();
        metricCubeFactory.setKey(key);
        metricCubeFactory.setName(name);
        metricCubeFactory.setTimeBaselineDimension(timeBaselineDimension);
        metricCubeFactory.setMergeUnitClazz(aggregateFieldProcessor.getMergeUnitClazz());
        deriveMetricCalculate.setMetricCubeFactory(metricCubeFactory);

        //派生指标中间结算结果存储接口
        //并发HashMap存储中间数据
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();
        //redis存储中间数据
        /*DeriveMetricMiddleRedisStore deriveMetricMiddleStore = new DeriveMetricMiddleRedisStore();
        RedisTemplate<String, byte[]> redisTemplate = SpringUtil.getBean("kryoRedisTemplate");
        List<Class<? extends MergedUnit>> classList = new ArrayList<>(unitFactory.getMethodReflection().values());
        deriveMetricMiddleStore.setClassList(classList);
        deriveMetricMiddleStore.setRedisTemplate(redisTemplate);*/
        deriveMetricMiddleStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);

        return deriveMetricCalculate;
    }

    /**
     * 初始化复合指标
     *
     * @param compositeMetric
     * @return
     */
    private static <T> List<CompositeMetricCalculate<T>> initComposite(Composite compositeMetric,
                                                                       MetricCalculate<T> metricCalculate) {
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        List<MultiDimensionCalculate> multiDimensionCalculateList = compositeMetric.getMultiDimensionCalculateList();
        if (CollUtil.isEmpty(multiDimensionCalculateList)) {
            throw new RuntimeException("复合指标多维度计算为空, 复合指标元数据: " + JSONUtil.toJsonStr(compositeMetric));
        }
        return multiDimensionCalculateList.stream().map(temp -> {
            CompositeMetricCalculate<T> compositeMetricCalculate = new CompositeMetricCalculate<>();

            //设置维度字段处理器
            String key = metricCalculate.getId() + "_" + compositeMetric.getId();
            String name = compositeMetric.getName();

            DimensionSetProcessor<T> dimensionSetProcessor =
                    FieldProcessorUtil.getDimensionSetProcessor(key, name, fieldMap, temp.getDimension());
            compositeMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

            //设置时间字段处理器
            TimeFieldProcessor<T> timeFieldProcessor = FieldProcessorUtil.getTimeFieldProcessor(compositeMetric.getTimeColumn());
            compositeMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

            //设置表达式字符串
            String expression = temp.getCalculateExpression();
            compositeMetricCalculate.setExpressString(expression);

            //在Aviator中添加自定义函数
            AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
            instance.addFunction(new GetFunction());
            instance.addFunction(new CoalesceFunction());
            instance.setOption(Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY, false);
            Expression compile = instance.compile(expression, true);
            compositeMetricCalculate.setExpression(compile);

            //设置变量名
            List<String> variableNames = compile.getVariableNames();
            compositeMetricCalculate.setParamList(variableNames);

            //设置名称
            compositeMetricCalculate.setName(name);
            //设置精度信息
            compositeMetricCalculate.setRoundAccuracy(compositeMetric.getRoundAccuracy());
            //设置存储宽表
            compositeMetricCalculate.setStoreInfo(compositeMetric.getStoreInfo());

            return compositeMetricCalculate;
        }).collect(Collectors.toList());
    }

    /**
     * 获取宽表字段
     *
     * @param metricCalculate 计算类
     * @return 字段名和数据类型的映射
     */
    private static <T> Map<String, Class<?>> getFieldMap(MetricCalculate<T> metricCalculate) {
        if (metricCalculate == null) {
            throw new RuntimeException("传入的明细宽表为空");
        }
        List<Fields> fields = metricCalculate.getFields();
        if (CollUtil.isEmpty(fields)) {
            throw new RuntimeException("宽表字段为空, 宽表数据: " + JSONUtil.toJsonStr(metricCalculate));
        }
        //宽表字段
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fields.forEach(temp -> fieldMap.put(temp.getName(), temp.getValueType().getType()));
        return fieldMap;
    }

    /**
     * 从原始数据中提取数据, 进行手动数据类型转换
     * <p>防止输入的数据类型和数据明细宽表定义的数据类型不匹配
     * <p>主要是数值型
     *
     * @param input    输入的数据
     * @param fieldMap 宽表字段名称和数据类型
     * @return
     */
    public static Map<String, Object> getParam(JSONObject input, Map<String, Class<?>> fieldMap) {
        if (CollUtil.isEmpty((Map<?, ?>) input)) {
            throw new RuntimeException("输入数据为空");
        }

        if (CollUtil.isEmpty(fieldMap)) {
            throw new RuntimeException("宽表字段为空");
        }

        Map<String, Object> params = new HashMap<>();
        fieldMap.forEach((key, tempDataClass) -> {
            Object value = input.get(key);
            if (value == null) {
                return;
            }
            value = Convert.convert(tempDataClass, value);
            params.put(key, value);
        });
        return params;
    }

}
