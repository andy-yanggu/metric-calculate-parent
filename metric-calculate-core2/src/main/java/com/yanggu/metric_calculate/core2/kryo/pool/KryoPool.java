package com.yanggu.metric_calculate.core2.kryo.pool;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.lang.mutable.MutablePair;
import cn.hutool.json.JSONObject;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrder;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.kryo.serializer.acc.*;
import com.yanggu.metric_calculate.core2.kryo.serializer.cube.DimensionSetSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.cube.MetricCubeSerializer;
import com.yanggu.metric_calculate.core2.kryo.serializer.table.*;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core2.table.*;
import com.yanggu.metric_calculate.core2.util.KeyValue;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class KryoPool extends Pool<Kryo> {

    public KryoPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
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

        //添加Table序列化和反序列化器
        kryo.register(TumblingTimeTable.class, new TumblingTimeTableSerializer<>(), 1);
        kryo.register(GlobalTable.class, new GlobalTableSerializer<>(), 2);
        kryo.register(SlidingTimeTable.class, new SlidingTimeTableSerializer<>(), 3);
        kryo.register(SlidingCountWindowTable.class, new SlidingCountWindowTableSerializer<>(), 4);
        kryo.register(StatusWindowTable.class, new StatusWindowTableSerializer<>(), 5);
        kryo.register(PatternTable.class, new PatternTableSerializer<>(), 6);

        //ACC序列化器和反序列化器
        kryo.register(Tuple.class, new TupleSerializer(), 11);
        kryo.register(MutablePair.class, new MutablePairSerializer<>(), 12);
        kryo.register(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>(), 13);
        kryo.register(MutableObj.class, new MutableObjectSerializer<>(), 14);
        kryo.register(Pair.class, new PairSerializer<>(), 15);
        kryo.register(MultiFieldDistinctKey.class, new MultiFieldDistinctKeySerializer(), 16);
        kryo.register(NodePattern.class, new BeanSerializer<>(kryo, NodePattern.class), 17);
        kryo.register(FieldOrder.class, new FieldOrderSerializer(), 18);
        kryo.register(MultiFieldOrderCompareKey.class, new MultiFieldOrderCompareKeySerializer(), 19);
        kryo.register(JSONObject.class, 20);
        kryo.register(KeyValue.class, new KeyValueSerializer<>(), 21);
        kryo.register(String.class, new DefaultSerializers.StringSerializer(), 22);
        kryo.register(ArrayList.class, new CollectionSerializer<ArrayList<Object>>(), 23);
        kryo.register(TreeMap.class, new DefaultSerializers.TreeMapSerializer(), 24);
        kryo.register(HashMap.class, new MapSerializer<HashMap<Object, Object>>(), 25);

        //MetricCube序列化器和反序列化器
        kryo.register(DimensionSet.class, new DimensionSetSerializer(), 40);
        kryo.register(MetricCube.class, new MetricCubeSerializer<>(), 41);
        return kryo;
    }

}
