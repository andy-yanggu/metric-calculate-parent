package com.yanggu.metric_calculate.test;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yanggu.metric_calculate.controller.BatchWaitStrategy;
import com.yanggu.metric_calculate.controller.MyEventHandler;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

public class BatchWaitStrategyTest {

    @Test
    public void testBatchWaitStrategy() throws InterruptedException {
        final int RING_BUFFER_SIZE = 1024;
        final int BATCH_SIZE = 10;
        final long TIMEOUT = 100;
        final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BatchWaitStrategy(BATCH_SIZE, TIMEOUT, TIME_UNIT));
        Consumer<List<LongEvent>> consumer = list -> {
            System.out.println("--------------");
            System.out.println("list大小: " + list.size());
            list.forEach(System.out::println);
        };
        MyEventHandler<LongEvent> longMyEventHandler = new MyEventHandler<>(consumer, BATCH_SIZE);
        disruptor.handleEventsWith(longMyEventHandler);

        RingBuffer<LongEvent> ringBuffer = disruptor.start();

        for (int i = 0; i < 50; i++) {
            long sequence = ringBuffer.next();
            ringBuffer.get(sequence).setValue(i);
            ringBuffer.publish(sequence);
        }

        Thread.sleep(1000);

        disruptor.shutdown();
    }

    private static class LongEvent {
        private long value;

        public void setValue(long value) {
            this.value = value;
        }

        public long getValue() {
            return value;
        }
    }
}