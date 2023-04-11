package com.yanggu.metric_calculate.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MyEventHandler<T> implements com.lmax.disruptor.EventHandler<T> {

    private final Consumer<List<T>> consumer;

    private final int batchSize;

    private final List<T> list;

    public MyEventHandler(Consumer<List<T>> consumer, int batchSize) {
        this.consumer = consumer;
        this.batchSize = batchSize;
        this.list = new ArrayList<>(batchSize);
    }

    @Override
    public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
        // 将事件添加到 list 中
        list.add(event);

        // 如果 list 中的事件数量达到批处理大小或者是批处理的结束，则传递 list 给 Consumer 进行消费
        if (list.size() >= batchSize || endOfBatch) {
            consumer.accept(list);
            // 清空 list
            list.clear();
        }
    }

}