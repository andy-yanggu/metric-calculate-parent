package com.yanggu.metric_calculate.core2.kryo.serializer.acc;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.util.ReflectUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * 有界优先队列序列化器
 */
public class BoundedPriorityQueueSerializer<T> extends Serializer<BoundedPriorityQueue<T>> {

    @Override
    public void write(Kryo kryo, Output output, BoundedPriorityQueue<T> boundedPriorityQueue) {
        kryo.writeObject(output, ReflectUtil.getFieldValue(boundedPriorityQueue, "capacity"));
        kryo.writeObjectOrNull(output, ReflectUtil.getFieldValue(boundedPriorityQueue, "comparator"), Comparator.class);
        kryo.writeClassAndObject(output, new ArrayList<>(boundedPriorityQueue));
    }

    @Override
    public BoundedPriorityQueue<T> read(Kryo kryo, Input input, Class<? extends BoundedPriorityQueue<T>> type) {
        Integer capacity = kryo.readObject(input, Integer.class);
        Comparator<T> comparator = kryo.readObjectOrNull(input, Comparator.class);
        ArrayList<T> dataList = (ArrayList) kryo.readClassAndObject(input);
        BoundedPriorityQueue<T> boundedPriorityQueue = new BoundedPriorityQueue<>(capacity, comparator);
        boundedPriorityQueue.addAll(dataList);
        return boundedPriorityQueue;
    }

}
