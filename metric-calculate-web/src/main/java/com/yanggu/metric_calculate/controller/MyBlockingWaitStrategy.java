package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import com.lmax.disruptor.*;
import org.omg.PortableInterceptor.INACTIVE;

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
        if (cursorSequence.get() < sequence)
        {
            lock.lock();
            try
            {
                boolean flag = false;
                long interval = 0L;
                long firstTimestamp = 0L;
                while (cursorSequence.get() < sequence)
                {
                    barrier.checkAlert();
                    if (firstTimestamp != 0L) {
                        interval = System.currentTimeMillis() - firstTimestamp;
                        System.out.println("时间差" + interval);
                    }
                    boolean await = processorNotifyCondition.await(5000 - interval, TimeUnit.MILLISECONDS);
                    if (!await) {
                        System.out.println("定时器时间到, 当前时间: " + DateUtil.formatDateTime(new Date()));
                        if (flag) {
                            break;
                        }
                    } else {
                        flag = true;
                        if (firstTimestamp == 0L) {
                            firstTimestamp = System.currentTimeMillis();
                            System.out.println("第一次数据时间:" + DateUtil.formatDateTime(new Date(firstTimestamp)));
                        }
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