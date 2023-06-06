package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MpscArrayQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_PATTERN;

/**
 * 攒批组件
 *
 * @param <T> 攒批的数据类型
 */
@Slf4j
public class AccumulateBatchComponent<T> {

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
        this.workThreads = new ArrayList<>(threadNum);
        if (threadNum > 1) {
            this.index = new AtomicInteger();
        }

        //定时器线程池
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(name + "-攒批定时器线程", true));
        for (int i = 0; i < threadNum; ++i) {
            String threadName = name + "_" + i;
            WorkThread<T> workThread = new WorkThread<>(limit, consumer);
            Thread thread = new Thread(workThread);
            workThread.currentThread = thread;
            this.workThreads.add(workThread);

            //启动消费者线程
            thread.setName(threadName);
            thread.start();

            //启动定时器, 唤醒消费者线程
            scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> workThread.start(false), 0L, interval, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 生产者线程将对象添加到对应消费者线程对象内部的阻塞队列中去
     * <p>默认进行轮询, 可以拓展自己的路由算法</p>
     *
     * @param item 待添加的对象
     * @return true:添加成功 false:添加失败
     */
    public void add(T item) {
        int len = this.workThreads.size();
        if (len == 1) {
            this.workThreads.get(0).add(item);
        } else {
            int mod = this.index.incrementAndGet() % len;
            this.workThreads.get(mod).add(item);
        }
    }

    /**
     * 消费者线程
     */
    private static class WorkThread<T> implements Runnable {

        /**
         * 队列中允许存放元素个数限制<br>
         * 超出将从队列中取出此大小的元素转成List对象
         */
        private final int limit;

        /**
         * 工作线程对象内部的阻塞队列
         */
        private final MpscArrayQueue<T> queue;

        /**
         * 回调接口
         */
        private final Consumer<List<T>> consumer;

        /**
         * 用来记录任务的即时处理时间
         */
        private volatile long lastFlushTime;

        /**
         * 当前工作线程对象
         */
        private volatile Thread currentThread;

        private volatile boolean wakeup = false;

        /**
         * 消费者线程构造器
         *
         * @param limit    指定队列阈值(可配置)
         * @param consumer 回调接口
         */
        public WorkThread(int limit, Consumer<List<T>> consumer) {
            this.limit = limit;
            this.consumer = consumer;
            this.queue = new MpscArrayQueue<>(2 * limit);
            this.lastFlushTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            //当前线程没有被其他线程打断
            while (!this.currentThread.isInterrupted()) {
                //线程默认阻塞, 等待被唤醒
                //唤醒条件为攒批大小到或者攒批时间到
                LockSupport.park(this);
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
        public void add(T item) {
            boolean result = false;
            while (!result) {
                result = this.queue.offer(item);
            }
            this.check();
        }

        /**
         * 队列长度检查
         */
        private void check() {
            //检查队列长度
            while (this.queue.size() >= this.limit) {
                this.start(true);
            }
        }

        /**
         * 唤醒被阻塞的工作线程
         */
        private void start(boolean matchLength) {
            if (matchLength) {
                if (!wakeup) {
                    synchronized (this) {
                        if (!wakeup) {
                            if (this.queue.size() < this.limit) {
                                return;
                            }
                            log.info("攒批大小到, {}队列大小={}, 超出指定阈值={}", currentThread.getName(), this.queue.size(), limit);
                            LockSupport.unpark(this.currentThread);
                            wakeup = true;
                        }
                    }
                }
            } else {
                //记录最新任务处理开始时间
                long flushTime = this.lastFlushTime;
                this.lastFlushTime = System.currentTimeMillis();
                if (queue.isEmpty()) {
                    return;
                }
                log.info("攒批时间到, {}队列大小={}, 上次执行时间: {}, 执行时间: {}",
                        currentThread.getName(), this.queue.size(),
                        DateUtil.format(new Date(flushTime), NORM_DATETIME_MS_PATTERN),
                        DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN)
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
            List<T> temp = new ArrayList<>(this.limit);
            this.queue.drain(temp::add, this.limit);
            if (!temp.isEmpty()) {
                log.info("{}被唤醒后, 开始执行任务:从队列中腾出大小为{}的数据且转成List对象", currentThread.getName(), temp.size());
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