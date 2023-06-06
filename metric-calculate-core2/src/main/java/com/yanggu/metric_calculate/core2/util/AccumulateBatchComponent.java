package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MpscArrayQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * 攒批组件
 *
 * @param <T> 攒批的数据类型
 */
@Slf4j
public class AccumulateBatchComponent<T> {

    private final String name;

    /**
     * 工作线程数组
     */
    private final List<WorkThread<T>> workThreads;

    private AtomicInteger index;

    /**
     * 构造器
     *
     * @param threadNum 队列数量
     * @param limit     攒批大小
     * @param interval  攒批时间
     * @param consumer  消费list逻辑
     */
    public AccumulateBatchComponent(String name, int threadNum, int limit,
                                    int interval, Consumer<List<T>> consumer) {
        this.name = name;
        this.workThreads = new ArrayList<>(threadNum);
        if (threadNum > 1) {
            this.index = new AtomicInteger();
        }
        for (int i = 0; i < threadNum; ++i) {
            WorkThread<T> workThread = new WorkThread<>(name + "_" + i, limit, interval, 2 * limit, consumer);
            this.workThreads.add(workThread);
            //启动消费者线程
            new Thread(workThread).start();
        }
    }

    /**
     * 生产者线程将对象添加到对应消费者线程对象内部的阻塞队列中去
     * <p>默认进行轮询, 可以拓展自己的路由算法</p>
     *
     * @param item 待添加的对象
     * @return true:添加成功 false:添加失败
     */
    public boolean add(T item) {
        int len = this.workThreads.size();
        if (len == 1) {
            return this.workThreads.get(0).add(item);
        } else {
            int mod = this.index.incrementAndGet() % len;
            return this.workThreads.get(mod).add(item);
        }
    }

    /**
     * 消费者线程
     */
    private static class WorkThread<T> implements Runnable {

        /**
         * 工作线程命名
         */
        private final String threadName;

        /**
         * 队列中允许存放元素个数限制<br>
         * 超出将从队列中取出此大小的元素转成List对象
         */
        private final int queueSizeLimit;

        /**
         * 前后两个任务的执行周期
         */
        private final long period;

        /**
         * 当前工作线程对象
         */
        private volatile Thread currentThread;

        private volatile ScheduledFuture<?> scheduledFuture;

        private volatile boolean wakeup = false;

        /**
         * 工作线程对象内部的阻塞队列
         */
        private final MpscArrayQueue<T> queue;

        /**
         * 回调接口
         */
        private final Consumer<List<T>> consumer;

        /**
         * 定时器线程池
         */
        private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

        /**
         * 消费者线程构造器
         *
         * @param threadName     线程名
         * @param queueSizeLimit 指定队列阈值(可配置)
         * @param period         工作线程对象的timeout方法前后两次执行的时间间隔周期(可配置)
         * @param capacity       阻塞队列初始容量
         * @param consumer       回调接口
         */
        public WorkThread(String threadName, int queueSizeLimit, int period, int capacity, Consumer<List<T>> consumer) {
            this.threadName = threadName;
            this.queueSizeLimit = queueSizeLimit;
            this.period = period;
            this.consumer = consumer;
            this.queue = new MpscArrayQueue<>(capacity);
            this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(threadName + "-攒批定时器线程", true));
        }

        @Override
        public void run() {
            this.currentThread = Thread.currentThread();
            this.currentThread.setName(this.threadName);
            //当前线程没有被其他线程打断
            while (!this.currentThread.isInterrupted()) {
                //线程默认阻塞, 等待被唤醒
                //唤醒条件为攒批大小到或者攒批时间到
                LockSupport.park(this);
                //log.info("阻塞线程被唤醒");
                this.consumerListData();
                wakeup = false;
            }
        }

        /**
         * 往阻塞队列中添加元素
         *
         * @param item 添加的对象
         * @return true:添加成功 false:添加失败
         */
        public boolean add(T item) {
            boolean result = this.queue.offer(item);
            this.check();
            return result;
        }

        /**
         * 队列长度检查和定时器注册
         */
        private void check() {
            //只能一个注册定时器
            //进行加锁和double check
            if (scheduledFuture == null) {
                synchronized (this) {
                    if (scheduledFuture == null) {
                        long registerTimeStamp = System.currentTimeMillis();
                        scheduledFuture = scheduledThreadPoolExecutor.schedule(() -> this.start(registerTimeStamp), period, TimeUnit.MILLISECONDS);
                    }
                }
            }
            //检查队列长度
            while (this.queue.size() >= this.queueSizeLimit) {
                this.start(0L);
            }
        }

        /**
         * 唤醒被阻塞的工作线程
         */
        private void start(long timestamp) {
            if (timestamp == 0L) {
                if (!wakeup) {
                    synchronized (this) {
                        if (!wakeup) {
                            if (this.queue.size() < this.queueSizeLimit) {
                                return;
                            }
                            log.info("攒批大小到, {}队列大小={}, 超出指定阈值={}", currentThread.getName(), this.queue.size(), queueSizeLimit);
                            LockSupport.unpark(this.currentThread);
                            wakeup = true;
                        }
                    }
                }
            } else {
                //删除之前注册的定时器
                if (scheduledFuture != null) {
                    log.info("定时器删除成功");
                    scheduledFuture.cancel(true);
                    scheduledFuture = null;
                }
                if (queue.isEmpty()) {
                    return;
                }
                log.info("攒批时间到, {}队列大小={}, 注册定时器时间: {}, 执行时间: {}",
                        currentThread.getName(), this.queue.size(),
                        DateUtil.format(new Date(timestamp), "yyyy-MM-dd HH:mm:ss.SSS"),
                        DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS")
                );
                LockSupport.unpark(this.currentThread);
                wakeup = true;
            }
        }

        /**
         * 消费队列中的list数据
         */
        private void consumerListData() {
            if (queue.isEmpty()) {
                return;
            }
            List<T> temp = new ArrayList<>(this.queueSizeLimit);
            this.queue.drain(temp::add, this.queueSizeLimit);
            if (!temp.isEmpty()) {
                log.info("{}被唤醒后,开始执行任务:从队列中腾出大小为{}的数据且转成List对象", currentThread.getName(), temp.size());
                try {
                    //执行回调函数
                    this.consumer.accept(temp);
                } catch (Throwable error) {
                    log.error("批处理发生异常", error);
                }
            }
        }

    }

}