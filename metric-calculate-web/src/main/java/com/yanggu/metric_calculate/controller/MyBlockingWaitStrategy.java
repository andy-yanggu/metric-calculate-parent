package com.yanggu.metric_calculate.controller;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.thread.NamedThreadFactory;
import com.lmax.disruptor.*;
import com.lmax.disruptor.util.ThreadHints;

/**
 * Blocking strategy that uses a lock and condition variable for {@link EventProcessor}s waiting on a barrier.
 * <p>
 * This strategy can be used when throughput and low-latency are not as important as CPU resource.
 */
public final class MyBlockingWaitStrategy implements WaitStrategy
{
    private final Lock lock = new ReentrantLock();
    private final Condition processorNotifyCondition = lock.newCondition();

    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
            new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("攒批定时器线程", true));

    private ScheduledFuture<?> scheduledFuture;

    private AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    @Override
    public long waitFor(long sequence, Sequence cursorSequence, Sequence dependentSequence, SequenceBarrier barrier)
        throws AlertException, InterruptedException
    {
        long availableSequence;
        if (cursorSequence.get() < sequence)
        {
            lock.lock();
            try
            {
                while (cursorSequence.get() < sequence || !atomicBoolean.get())
                {
                    if (scheduledFuture == null) {
                        scheduledFuture = scheduledThreadPoolExecutor.schedule(() -> {
                            atomicBoolean.compareAndSet(false, true);
                            processorNotifyCondition.signalAll();
                        }, 5000, TimeUnit.MILLISECONDS);
                    }
                    barrier.checkAlert();
                    processorNotifyCondition.await();
                }
            }
            finally
            {
                lock.unlock();
                atomicBoolean.set(false);
            }
        }

        while ((availableSequence = dependentSequence.get()) < sequence)
        {
            barrier.checkAlert();
            ThreadHints.onSpinWait();
        }

        return availableSequence;
    }

    @Override
    public void signalAllWhenBlocking()
    {
        lock.lock();
        try
        {
            processorNotifyCondition.signalAll();
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public String toString()
    {
        return "BlockingWaitStrategy{" +
            "processorNotifyCondition=" + processorNotifyCondition +
            '}';
    }
}