package com.yanggu.metric_calculate.test;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yanggu.metric_calculate.controller.BatchWaitStrategy;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class BatchWaitStrategyTest {

    @Test
    public void testBatchWaitStrategy() throws InterruptedException {
        final int RING_BUFFER_SIZE = 1024;
        final int BATCH_SIZE = 10;
        final long TIMEOUT = 100;
        final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BatchWaitStrategy(BATCH_SIZE, TIMEOUT, TIME_UNIT));
        disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
            System.out.println("Event: " + event.getValue() + ", Sequence: " + sequence + ", End of batch: " + endOfBatch);
        });

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