package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoIntSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoPriorityQueueSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;

import java.io.Serial;
import java.io.Serializable;
import java.util.Comparator;

/**
 * 有界优先队列序列化器
 */
public class BoundedPriorityQueueSerializer<T> extends Serializer<BoundedPriorityQueue<T>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7019833704874520657L;

    private final KryoIntSerializer intSerializer = new KryoIntSerializer();

    private final KryoPriorityQueueSerializer priorityQueueSerializer = new KryoPriorityQueueSerializer();

    @Override
    public void write(Kryo kryo, Output output, BoundedPriorityQueue<T> boundedPriorityQueue) {
        intSerializer.write(kryo, output, boundedPriorityQueue.getCapacity());
        kryo.writeClassAndObject(output, boundedPriorityQueue.getComparator());
        priorityQueueSerializer.write(kryo, output, boundedPriorityQueue);
    }

    @Override
    public BoundedPriorityQueue<T> read(Kryo kryo, Input input, Class<? extends BoundedPriorityQueue<T>> type) {
        Integer capacity = intSerializer.read(kryo, input, Integer.class);
        Comparator<? super T> comparator = (Comparator<? super T>) kryo.readClassAndObject(input);
        BoundedPriorityQueue<T> boundedPriorityQueue =
                (BoundedPriorityQueue<T>) priorityQueueSerializer.read(kryo, input, BoundedPriorityQueue.class);
        boundedPriorityQueue.setCapacity(capacity);
        boundedPriorityQueue.setComparator(comparator);
        return boundedPriorityQueue;
    }

}
