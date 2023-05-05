package com.yanggu.metric_calculate.core2.kryo.pool;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.lang.mutable.MutablePair;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import com.yanggu.metric_calculate.core2.kryo.serializer.*;
import com.yanggu.metric_calculate.core2.table.GlobalTable;
import com.yanggu.metric_calculate.core2.table.TumblingTimeTimeTable;
import org.objenesis.strategy.StdInstantiatorStrategy;

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

        kryo.register(TumblingTimeTimeTable.class, new TimeTableSerializer<>(), 1);
        kryo.register(Tuple.class, new TupleSerializer(), 2);
        kryo.register(MutablePair.class, new MutablePairSerializer<>(), 3);
        kryo.register(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer<>(), 4);
        kryo.register(GlobalTable.class, new GlobalTableSerializer<>(), 5);
        return kryo;
    }

}
