package com.yanggu.metric_calculate.core.kryo.pool;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core.kryo.serializer.acc.*;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.DimensionSetSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.window.*;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorFunctionInstance;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.util.KeyValue;
import com.yanggu.metric_calculate.core.window.*;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.lang.mutable.MutableObj;
import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.json.JSONObject;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Kryo池, 实例化kryo对象时默认注册一些类
 */
public class KryoPool extends Pool<Kryo> {

    public KryoPool(int maximumCapacity) {
        super(true, true, maximumCapacity);
    }

    @Override
    protected Kryo create() {
        Kryo kryo = new Kryo();

        //检测循环依赖，默认值为true,避免版本变化显式设置
        kryo.setReferences(true);
        //默认值为true，避免版本变化显式设置
        kryo.setRegistrationRequired(false);
        //设定默认的实例化器
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        //添加Window序列化和反序列化器
        kryo.register(TumblingTimeWindow.class, new TumblingTimeWindowSerializer<>(), 1);
        kryo.register(GlobalWindow.class, new GlobalWindowSerializer<>(), 2);
        kryo.register(SlidingTimeWindow.class, new SlidingTimeWindowSerializer<>(), 3);
        kryo.register(SlidingCountWindow.class, new SlidingCountWindowSerializer<>(), 4);
        kryo.register(StatusWindow.class, new StatusWindowSerializer<>(), 5);
        kryo.register(PatternWindow.class, new PatternWindowSerializer<>(), 6);

        //ACC序列化器和反序列化器
        kryo.register(Tuple.class, new TupleSerializer(), 21);
        kryo.register(MutableEntry.class, new MutableEntrySerializer<>(), 22);
        kryo.register(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>(), 23);
        kryo.register(MutableObj.class, new MutableObjectSerializer<>(), 24);
        kryo.register(Pair.class, new PairSerializer<>(), 25);
        kryo.register(MultiFieldDistinctKey.class, new MultiFieldDistinctKeySerializer(), 26);
        kryo.register(NodePattern.class, new BeanSerializer<>(kryo, NodePattern.class), 27);
        kryo.register(MultiFieldOrderCompareKey.class, new MultiFieldOrderCompareKeySerializer(), 28);
        kryo.register(JSONObject.class, 29);
        kryo.register(KeyValue.class, new KeyValueSerializer<>(), 30);
        kryo.register(String.class, new DefaultSerializers.StringSerializer(), 31);
        kryo.register(ArrayList.class, new CollectionSerializer<ArrayList<Object>>(), 32);
        kryo.register(TreeMap.class, new DefaultSerializers.TreeMapSerializer(), 33);
        kryo.register(HashMap.class, new MapSerializer<HashMap<Object, Object>>(), 34);
        kryo.register(AviatorExpressParam.class, new BeanSerializer<>(kryo, AviatorExpressParam.class), 35);
        kryo.register(AviatorFunctionInstance.class, new BeanSerializer<>(kryo, AviatorFunctionInstance.class), 36);

        //MetricCube序列化器和反序列化器
        kryo.register(DimensionSet.class, new DimensionSetSerializer(), 60);
        kryo.register(MetricCube.class, new MetricCubeSerializer<>(), 61);
        return kryo;
    }

}
