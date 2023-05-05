package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
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

    /**
     * 组件持有一个工作线程对象数组
     */
    private final List<WorkThread<T>> workThreads;

    private AtomicInteger index;

    /**
     * 任务定时器
     */
    private static final ScheduledThreadPoolExecutor scheduleThreadPool = new ScheduledThreadPoolExecutor(1);

    /**
     * 组件初始化完成工作线程的新建
     */
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 构造器
     *
     * @param threadNum 默认新建的消费者线程个数
     * @param limitSize 队列长度阈值;超过将唤醒阻塞的线程
     * @param period    工作线程对象的timeout方法前后两次执行的时间间隔周期
     * @param capacity  工作线程内部的有界阻塞队列的初始容量大小
     * @param consumer  回调接口(初始化组价实例的时候需要传递)
     */
    public AccumulateBatchComponent(int threadNum, int limitSize, int period, int capacity, Consumer<List<T>> consumer) {
        this.workThreads = new ArrayList<>(threadNum);
        if (threadNum > 1) {
            this.index = new AtomicInteger();
        }
        for (int i = 0; i < threadNum; ++i) {
            WorkThread<T> workThread = new WorkThread<>("workThread" + "_" + i, limitSize, period, capacity, consumer);
            this.workThreads.add(workThread);

            executorService.submit(workThread);
            //开启threadNum个定时任务，每个任务各自检查各个工作线程对象内部的timeout方法，查看前后两次的timeout方法执行周期
            scheduleThreadPool.scheduleAtFixedRate(workThread::timeout, RandomUtil.randomInt(50), period, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 生产者线程将对象添加到对应消费者线程对象内部的阻塞队列中去<br>
     * 内部采用HASH取模算法进行动态路由
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
         * 用来记录任务的即时处理时间
         */
        private volatile long lastFlushTime;

        /**
         * 当前工作线程对象
         */
        private volatile Thread currentThread;

        /**
         * 工作线程对象内部的阻塞队列
         */
        private final BlockingQueue<T> queue;

        /**
         * 回调接口
         */
        private final Consumer<List<T>> consumer;

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
            this.lastFlushTime = System.currentTimeMillis();
            this.consumer = consumer;
            this.queue = new ArrayBlockingQueue<>(capacity);
        }

        @Override
        public void run() {
            this.currentThread = Thread.currentThread();
            this.currentThread.setName(this.threadName);
            //当前线程没有被其他线程打断
            while (!this.currentThread.isInterrupted()) {
                //死循环的判断是否满足触发条件(队列实际大小是否超出指定阈值或距离上次任务时间是否超出指定阈值)，如果未满足将阻塞当前线程，避免死循环给系统带来性能开销
                while (!this.canFlush()) {
                    //当前工作线程被阻塞
                    //log.info("线程被阻塞...");
                    LockSupport.park(this);
                }
                //一旦add方法执行的时候判断存放的阻塞队列元素大小超出自定制阈值亦或距离上次任务处理时间差超出指定阈值，就会调用LockSupport.unpark方法解除阻塞的线程
                //一旦线程被解除阻塞，就会触发此方法，将队列元素转成List对象且调用已经注册的回调函数
                // log.info("阻塞线程被唤醒");
                this.flush();
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
            this.checkQueueSize();
            return result;
        }

        /**
         * 当前时间与上次任务处理时间差是否超过指定阈值;如果超过触发start方法
         */
        public void timeout() {
            if (System.currentTimeMillis() - this.lastFlushTime >= this.period) {
                //log.info("当前时间={}距离上次任务处理时间={}周期={}超出指定阈值={}", System.currentTimeMillis(), lastFlushTime, (System.currentTimeMillis() - this.lastFlushTime), period);
                this.start(false);
            }
        }

        /**
         * 唤醒被阻塞的工作线程
         */
        private void start(boolean matchLength) {
            if (matchLength) {
                //log.info("攒批大小匹配, 执行start方法，唤醒被阻塞的线程" + currentThread.getName());
            } else {
                //log.info("攒批时间到, 执行start方法，唤醒被阻塞的线程" + currentThread.getName());
            }
            LockSupport.unpark(this.currentThread);
        }

        /**
         * 判断队列实际长度是否超过指定阈值;如果超过触发start方法
         */
        private void checkQueueSize() {
            if (this.queue.size() > this.queueSizeLimit) {
                log.info("{}队列大小={}超出指定阈值={}", currentThread.getName(), this.queue.size(), queueSizeLimit);
                this.start(true);
            }
        }

        /**
         * 将队列中的元素添加到指定集合(初始容量限制)
         */
        public void flush() {
            //记录最新任务处理开始时间
            this.lastFlushTime = System.currentTimeMillis();
            if (queue.isEmpty()) {
                return;
            }
            List<T> temp = new ArrayList<>(this.queueSizeLimit);
            int size = this.queue.drainTo(temp, this.queueSizeLimit);
            if (size > 0) {
                log.info("{}被唤醒后,开始执行任务:从队列中腾出大小为{}的数据且转成List对象", currentThread.getName(), size);
                try {
                    //执行回调函数
                    this.consumer.accept(temp);
                } catch (Throwable error) {
                    log.error("批处理发生异常", error);
                }
            }
        }

        /**
         * 判断队列实际大小是否超过指定阈值亦或距离上次任务处理时间差是否超过指定阈值
         *
         * @return true:满足触发条件 false:不满足触发条件
         */
        private boolean canFlush() {
            return this.queue.size() > this.queueSizeLimit || System.currentTimeMillis() - this.lastFlushTime > this.period;
        }

    }

}