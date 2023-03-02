package com.yanggu.metric_calculate.core.kryo.serializer;


import cn.hutool.core.collection.BoundedPriorityQueue;
import cn.hutool.core.util.ReflectUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.util.ArrayList;
import java.util.Comparator;

public class BoundedPriorityQueueSerializer extends Serializer<BoundedPriorityQueue> {

    @Override
    public void write(Kryo kryo, Output output, BoundedPriorityQueue boundedPriorityQueue) {
        kryo.writeObject(output, ReflectUtil.getFieldValue(boundedPriorityQueue, "capacity"));
        kryo.writeObjectOrNull(output, ReflectUtil.getFieldValue(boundedPriorityQueue, "comparator"), Comparator.class);
        kryo.writeClassAndObject(output, new ArrayList<>(boundedPriorityQueue));
    }

    @Override
    public BoundedPriorityQueue read(Kryo kryo, Input input, Class<BoundedPriorityQueue> type) {
        Integer capacity = kryo.readObject(input, Integer.class);
        Comparator comparator = kryo.readObjectOrNull(input, Comparator.class);
        ArrayList dataList = (ArrayList) kryo.readClassAndObject(input);
        BoundedPriorityQueue boundedPriorityQueue = new BoundedPriorityQueue<>(capacity, comparator);
        dataList.forEach(temp -> boundedPriorityQueue.add(temp));
        return boundedPriorityQueue;
    }

}
