package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import com.lmax.disruptor.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Blocking strategy that uses a lock and condition variable for {@link EventProcessor}s waiting on a barrier.
 * <p>
 * This strategy can be used when throughput and low-latency are not as important as CPU resource.
 */
public final class MyBlockingWaitStrategy implements WaitStrategy
{
    private final Lock lock = new ReentrantLock();

    private final Condition processorNotifyCondition = lock.newCondition();

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
                boolean flag = false;
                while (cursorSequence.get() < sequence)
                {
                    barrier.checkAlert();
                    boolean await = processorNotifyCondition.await(5, TimeUnit.SECONDS);
                    if (!await) {
                        System.out.println("定时器时间到, 当前时间: " + DateUtil.formatDateTime(new Date()));
                        //System.out.println("cursorSequence.get()等于" + cursorSequence.get());
                        if (flag) {
                            break;
                        }
                    } else {
                        //System.out.println("有数据了");
                        //System.out.println("cursorSequence.get()" + cursorSequence.get());
                        flag = true;
                    }
                }
            }
            finally
            {
                lock.unlock();
            }
        }

        return dependentSequence.get();
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