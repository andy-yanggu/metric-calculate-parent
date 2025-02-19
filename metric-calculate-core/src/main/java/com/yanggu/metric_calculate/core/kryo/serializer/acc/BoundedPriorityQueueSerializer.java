package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.enums.SortType;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoEnumSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoIntSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.BoundedPriorityQueue;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 有界优先队列序列化器
 */
public class BoundedPriorityQueueSerializer<T> extends Serializer<BoundedPriorityQueue<T>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7019833704874520657L;

    private final KryoIntSerializer intSerializer = new KryoIntSerializer();

    private final KryoEnumSerializer enumSerializer = new KryoEnumSerializer(SortType.class);

    private final KryoCollectionSerializer<List> listSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, BoundedPriorityQueue<T> boundedPriorityQueue) {
        intSerializer.write(kryo, output, boundedPriorityQueue.getCapacity());
        kryo.writeClassAndObject(output, boundedPriorityQueue.getComparator());
        enumSerializer.write(kryo, output, boundedPriorityQueue.getSortType());
        listSerializer.write(kryo, output, new ArrayList<>(boundedPriorityQueue));
    }

    @Override
    public BoundedPriorityQueue<T> read(Kryo kryo, Input input, Class<? extends BoundedPriorityQueue<T>> type) {
        Integer capacity = intSerializer.read(kryo, input, Integer.class);
        Comparator<? super T> comparator = (Comparator<? super T>) kryo.readClassAndObject(input);
        SortType sortType = (SortType) enumSerializer.read(kryo, input, SortType.class);
        List<T> dataList = listSerializer.read(kryo, input, ArrayList.class);
        BoundedPriorityQueue<T> boundedPriorityQueue = new BoundedPriorityQueue<>(capacity, comparator, sortType);
        boundedPriorityQueue.addAll(dataList);
        return boundedPriorityQueue;
    }

}
