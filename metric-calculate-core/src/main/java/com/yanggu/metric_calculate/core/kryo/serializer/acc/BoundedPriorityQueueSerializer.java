package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.dromara.hutool.core.collection.queue.BoundedPriorityQueue;
import org.dromara.hutool.core.reflect.FieldUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 有界优先队列序列化器
 */
public class BoundedPriorityQueueSerializer<T> extends Serializer<BoundedPriorityQueue<T>> implements Serializable {

    private static final long serialVersionUID = 7019833704874520657L;

    @Override
    public void write(Kryo kryo, Output output, BoundedPriorityQueue<T> boundedPriorityQueue) {
        kryo.writeObject(output, FieldUtil.getFieldValue(boundedPriorityQueue, "capacity"));
        kryo.writeObjectOrNull(output, FieldUtil.getFieldValue(boundedPriorityQueue, "comparator"), Comparator.class);
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
