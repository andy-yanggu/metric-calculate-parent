package com.yanggu.metric_calculate.controller;

import com.lmax.disruptor.*;

import java.util.concurrent.TimeUnit;

public class BatchWaitStrategy implements WaitStrategy {

    private final int batchSize;
    private final long timeout;
    private final TimeUnit timeUnit;

    public BatchWaitStrategy(int batchSize, long timeout, TimeUnit timeUnit) {
        this.batchSize = batchSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    @Override
    public long waitFor(long sequence, Sequence cursor, Sequence dependentSequence, SequenceBarrier barrier) throws AlertException, InterruptedException, TimeoutException {
        long availableSequence;
        long currentBatchSize = 0L;
        long start = System.currentTimeMillis();

        while ((availableSequence = dependentSequence.get()) < sequence) {
            barrier.checkAlert();

            long delta = sequence - availableSequence;
            if (delta > batchSize - currentBatchSize) {
                currentBatchSize = 0L;
            }

            if (delta > 0) {
                long timeoutMs = timeUnit.toMillis(timeout) - (System.currentTimeMillis() - start);
                if (timeoutMs <= 0) {
                    return availableSequence;
                }
                barrier.waitFor(delta);
            } else {
                currentBatchSize++;
                if (currentBatchSize >= batchSize) {
                    return availableSequence;
                }
                Thread.yield();
            }
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking() {

    }

    @Override
    public String toString() {
        return "BatchWaitStrategy{" +
                "batchSize=" + batchSize +
                ", timeout=" + timeout +
                ", timeUnit=" + timeUnit +
                '}';
    }
}