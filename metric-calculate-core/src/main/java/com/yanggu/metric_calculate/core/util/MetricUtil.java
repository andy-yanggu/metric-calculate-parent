package com.yanggu.metric_calculate.core.util;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.aviatorfunction.CoalesceFunction;
import com.yanggu.metric_calculate.core.aviatorfunction.GetFunction;
import com.yanggu.metric_calculate.core.calculate.AtomMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.CompositeMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.fieldprocess.*;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.*;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.core.enums.MetricTypeEnum.*;

@Slf4j
public class MetricUtil {

    private MetricUtil() {
    }

    public static MetricCalculate initMetricCalculate(DataDetailsWideTable tableData) {
        if (tableData == null) {
            throw new RuntimeException("明细宽表为空");
        }

        MetricCalculate metricCalculate = BeanUtil.copyProperties(tableData, MetricCalculate.class);

        Map<String, MetricTypeEnum> metricTypeMap = new HashMap<>();
        metricCalculate.setMetricTypeMap(metricTypeMap);

        //宽表字段
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);
        metricCalculate.setFieldMap(fieldMap);

        //原子指标
        List<Atom> atomList = tableData.getAtom();
        if (CollUtil.isNotEmpty(atomList)) {
            List<AtomMetricCalculate> collect = atomList.stream()
                    .map(tempAtom -> {
                        metricTypeMap.put(tempAtom.getName(), ATOM);
                        //初始化原子指标计算类
                        return MetricUtil.initAtom(tempAtom, metricCalculate);
                    })
                    .collect(Collectors.toList());
            metricCalculate.setAtomMetricCalculateList(collect);
        }

        //派生指标
        List<Derive> deriveList = tableData.getDerive();
        if (CollUtil.isNotEmpty(deriveList)) {
            List<DeriveMetricCalculate> collect = deriveList.stream()
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
            List<CompositeMetricCalculate> collect = new ArrayList<>();
            compositeList.forEach(compositeMetric -> {
                metricTypeMap.put(compositeMetric.getName(), COMPOSITE);

                //初始化复合指标计算类
                List<CompositeMetricCalculate> compositeMetricCalculateList =
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
     * 初始化原子指标计算类
     *
     * @param atom
     * @return
     */
    @SneakyThrows
    private static AtomMetricCalculate initAtom(Atom atom, MetricCalculate metricCalculate) {
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        AtomMetricCalculate atomMetricCalculate = new AtomMetricCalculate<>();

        //设置名称
        atomMetricCalculate.setName(atom.getName());

        //设置前置过滤条件处理器
        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, atom.getFilter());
        filterProcessor.init();

        atomMetricCalculate.setFilterProcessor(filterProcessor);

        //度量字段处理器
        MetricFieldProcessor<?> metricFieldProcessor = new MetricFieldProcessor<>();
        metricFieldProcessor.setMetricExpress(atom.getMetricColumn().getColumnName());
        metricFieldProcessor.setFieldMap(fieldMap);
        metricFieldProcessor.init();
        atomMetricCalculate.setMetricFieldProcessor(metricFieldProcessor);

        //时间字段处理器
        TimeColumn timeColumn = atom.getTimeColumn();
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(),
                timeColumn.getColumnName());
        timeFieldProcessor.init();
        atomMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(atom.getDimension());
        dimensionSetProcessor.setFieldMap(fieldMap);
        dimensionSetProcessor.setMetricName(atom.getName());
        dimensionSetProcessor.setKey(metricCalculate.getId() + "_" + atom.getId());
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
     * @return
     */
    @SneakyThrows
    private static DeriveMetricCalculate initDerive(Derive tempDerive, MetricCalculate metricCalculate) {
        DeriveMetricCalculate deriveMetricCalculate = new DeriveMetricCalculate<>();
        deriveMetricCalculate.init();

        //设置名称
        deriveMetricCalculate.setName(tempDerive.getName());

        //设置key
        deriveMetricCalculate.setKey(metricCalculate.getId() + "_" + tempDerive.getId());

        //设置前置过滤条件处理器
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        FilterProcessor filterProcessor = new FilterProcessor(fieldMap, tempDerive.getFilter());
        filterProcessor.init();
        deriveMetricCalculate.setFilterProcessor(filterProcessor);

        //聚合字段处理器
        String columnName = tempDerive.getMetricColumn().getColumnName();
        //如果是计数, 度量值强制设定成1
        String calculateLogic = tempDerive.getCalculateLogic();
        if (StrUtil.equalsIgnoreCase(calculateLogic, "COUNT")) {
            columnName = "1";
        }

        //设置UnitFactory, 生成MergeUnit
        UnitFactory unitFactory = new UnitFactory(tempDerive.getUdafJarPathList());
        unitFactory.init();

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor =
                getAggregateFieldProcessor(unitFactory, tempDerive.getUdafParams(), fieldMap, columnName, calculateLogic);

        deriveMetricCalculate.setAggregateFieldProcessor(aggregateFieldProcessor);

        if (aggregateFieldProcessor.getMergeUnitClazz().getAnnotation(MergeType.class).countWindow()) {
            //如果是计数窗口, 需要添加子聚合字段处理器
            //滑动计数窗口的udafParams参数
            /*
            {
                "limit": 5, //滑动计数窗口大小
                "aggregateType": "SUM", //聚合类型
                "udafParams": { //自定义udaf参数
                    "metricExpress": "amount", //度量字段(数值)、比较字段(排序或者去重) TODO 需要前端手动设置原子指标度量字段名
                    "retainExpress": "", //保留字段名
                }
            }
            */

            //例如如果是一个普通的sortlistfield
            /*
            {
              "desc": true, //升序还是降序
              "limit": 10,  //限制大小
              "retainExpress": "amount" //保留字段名
            }
            */

            Map<String, Object> udafParams = tempDerive.getUdafParams();
            Object aggregateType = udafParams.get("aggregateType");
            if (StrUtil.isBlankIfStr(aggregateType)) {
                throw new RuntimeException("滑动计数窗口需要设置聚合类型aggregateType");
            }

            Object subUdafParamsMapObject = udafParams.get("udafParams");
            Map<String, Object> subUdafParams = new HashMap<>();
            Object metricExpress = null;
            if (subUdafParamsMapObject instanceof Map && CollUtil.isNotEmpty((Map<?, ?>) subUdafParamsMapObject)) {
                subUdafParams = (Map<String, Object>) subUdafParamsMapObject;
                metricExpress = subUdafParams.get("metricExpress");
            }

            BaseAggregateFieldProcessor<?> subAggregateFieldProcessor =
                    MetricUtil.getAggregateFieldProcessor(unitFactory, subUdafParams, fieldMap, metricExpress, aggregateType.toString());

            subAggregateFieldProcessor.setUdafParams(subUdafParams);
            deriveMetricCalculate.setSubAggregateFieldProcessor(subAggregateFieldProcessor);
        }

        //时间字段处理器
        TimeColumn timeColumn = tempDerive.getTimeColumn();
        TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(),
                timeColumn.getColumnName());
        timeFieldProcessor.init();
        deriveMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);

        //设置时间聚合粒度
        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension(tempDerive.getDuration(), tempDerive.getTimeUnit());
        deriveMetricCalculate.setTimeBaselineDimension(timeBaselineDimension);

        //维度字段处理器
        DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(tempDerive.getDimension());
        dimensionSetProcessor.setMetricName(tempDerive.getName());
        dimensionSetProcessor.setKey(metricCalculate.getId() + "_" + tempDerive.getId());
        dimensionSetProcessor.setFieldMap(fieldMap);
        dimensionSetProcessor.init();
        deriveMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

        //精度数据
        deriveMetricCalculate.setRoundAccuracy(tempDerive.getRoundAccuracy());

        //存储宽表
        deriveMetricCalculate.setStore(tempDerive.getStore());

        //派生指标中间结算结果存储接口
        //并发HashMap存储中间数据
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleHashMapStore();

        //redis存储中间数据
        //DeriveMetricMiddleRedisStore deriveMetricMiddleStore = new DeriveMetricMiddleRedisStore();
        //RedisTemplate<String, byte[]> redisTemplate = SpringUtil.getBean("kryoRedisTemplate");
        //List<Class<? extends MergedUnit>> classList = new ArrayList<>(unitFactory.getMethodReflection().values());
        //deriveMetricMiddleStore.setClassList(classList);
        //deriveMetricMiddleStore.setRedisTemplate(redisTemplate);
        deriveMetricMiddleStore.init();
        deriveMetricCalculate.setDeriveMetricMiddleStore(deriveMetricMiddleStore);

        return deriveMetricCalculate;
    }

    /**
     * 生成聚合字段处理器, 包含数值型、集合型、对象型
     *
     * @param unitFactory
     * @param udafParams
     * @param fieldMap
     * @param metricExpress
     * @param calculateLogic
     * @return
     * @throws Exception
     */
    private static BaseAggregateFieldProcessor<?> getAggregateFieldProcessor(UnitFactory unitFactory,
                                                                             Map<String, Object> udafParams,
                                                                             Map<String, Class<?>> fieldMap,
                                                                             Object metricExpress,
                                                                             String calculateLogic) throws Exception {

        BaseAggregateFieldProcessor<?> aggregateFieldProcessor;
        Class<? extends MergedUnit<?>> mergeUnitClazz = unitFactory.getMergeableClass(calculateLogic);
        //数值型, 把原子指标度量字段当成聚合的字段
        if (mergeUnitClazz.isAnnotationPresent(Numerical.class)) {
            aggregateFieldProcessor = new AggregateNumberFieldProcessor<>();
            aggregateFieldProcessor.setMetricExpress(metricExpress.toString());
        } else if (mergeUnitClazz.isAnnotationPresent(Objective.class)) {
            //对象型
            aggregateFieldProcessor = new AggregateObjectFieldProcessor<>();
            Objective annotation = mergeUnitClazz.getAnnotation(Objective.class);
            //对象型如果需要比较字段, 把原子指标的度量字段当成比较字段
            if (annotation.useCompareField()) {
                aggregateFieldProcessor.setMetricExpress(metricExpress.toString());
            }
            if (!annotation.retainObject()) {
                //设置保留字段处理器
                ((AggregateObjectFieldProcessor<?>) aggregateFieldProcessor)
                        .setRetainFieldValueFieldProcessor(getRetainFieldValueFieldProcessor(fieldMap, udafParams));
            }
        } else if (mergeUnitClazz.isAnnotationPresent(Collective.class)) {
            //集合型
            aggregateFieldProcessor = new AggregateCollectionFieldProcessor<>();
            Collective annotation = mergeUnitClazz.getAnnotation(Collective.class);
            //集合型如果需要比较字段, 把原子指标的度量字段当成比较字段
            if (annotation.useCompareField()) {
                aggregateFieldProcessor.setMetricExpress(metricExpress.toString());
            }
            if (!annotation.retainObject()) {
                //设置保留字段处理器
                ((AggregateCollectionFieldProcessor<?>) aggregateFieldProcessor)
                        .setRetainFieldValueFieldProcessor(getRetainFieldValueFieldProcessor(fieldMap, udafParams));
            }
        } else {
            throw new RuntimeException("不支持的聚合类型: " + calculateLogic);
        }

        //聚合字段处理器
        aggregateFieldProcessor.setFieldMap(fieldMap);
        aggregateFieldProcessor.setAggregateType(calculateLogic);
        aggregateFieldProcessor.setIsUdaf(false);
        aggregateFieldProcessor.setUdafParams(udafParams);
        aggregateFieldProcessor.setUnitFactory(unitFactory);
        aggregateFieldProcessor.setMergeUnitClazz(mergeUnitClazz);
        aggregateFieldProcessor.init();
        return aggregateFieldProcessor;
    }

    private static MetricFieldProcessor<?> getRetainFieldValueFieldProcessor(Map<String, Class<?>> fieldMap,
                                                                             Map<String, Object> udafParams) throws Exception {
        MetricFieldProcessor<?> retainFieldValueFieldProcessor = new MetricFieldProcessor<>();
        Object retainExpress = udafParams.get("retainExpress");
        if (StrUtil.isBlankIfStr(retainExpress)) {
            throw new RuntimeException("需要指定字段需要设置保留表达式");
        }
        retainFieldValueFieldProcessor.setMetricExpress(retainExpress.toString());
        retainFieldValueFieldProcessor.setFieldMap(fieldMap);
        retainFieldValueFieldProcessor.init();
        return retainFieldValueFieldProcessor;
    }

    /**
     * 初始化复合指标
     *
     * @param compositeMetric
     * @return
     */
    private static List<CompositeMetricCalculate> initComposite(Composite compositeMetric, MetricCalculate metricCalculate) {
        Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
        List<MultiDimensionCalculate> multiDimensionCalculateList = compositeMetric.getMultiDimensionCalculateList();
        if (CollUtil.isEmpty(multiDimensionCalculateList)) {
            throw new RuntimeException("复合指标多维度计算为空, 复合指标元数据: " + JSONUtil.toJsonStr(compositeMetric));
        }
        return multiDimensionCalculateList.stream()
                .map(temp -> {
                    CompositeMetricCalculate compositeMetricCalculate = new CompositeMetricCalculate();

                    //设置维度字段处理器
                    DimensionSetProcessor dimensionSetProcessor = new DimensionSetProcessor(temp.getDimension());
                    dimensionSetProcessor.setMetricName(compositeMetric.getName());
                    dimensionSetProcessor.setKey(metricCalculate.getId() + "_" + compositeMetric.getId());
                    dimensionSetProcessor.setFieldMap(fieldMap);
                    dimensionSetProcessor.init();
                    compositeMetricCalculate.setDimensionSetProcessor(dimensionSetProcessor);

                    //设置时间字段处理器
                    TimeColumn timeColumn = compositeMetric.getTimeColumn();
                    TimeFieldProcessor timeFieldProcessor = new TimeFieldProcessor(timeColumn.getTimeFormat(), timeColumn.getColumnName());
                    timeFieldProcessor.init();
                    compositeMetricCalculate.setTimeFieldProcessor(timeFieldProcessor);
                    AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();

                    //设置表达式字符串
                    String expression = temp.getCalculateExpression();
                    compositeMetricCalculate.setExpressString(expression);

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
    private static Map<String, Class<?>> getFieldMap(MetricCalculate metricCalculate) {
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
