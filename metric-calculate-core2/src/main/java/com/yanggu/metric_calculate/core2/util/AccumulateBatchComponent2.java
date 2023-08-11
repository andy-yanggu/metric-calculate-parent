package com.yanggu.metric_calculate.core2.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MpscBlockingConsumerArrayQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_PATTERN;

/**
 * 攒批组件2
 *
 * @param <T>
 */
@Slf4j
public class AccumulateBatchComponent2<T> {

    /**
     * 组件持有一个工作线程对象数组
     */
    private final List<WorkThread<T>> workThreads;

    private AtomicInteger index;

    /**
     * 构造器
     *
     * @param name      组件名称
     * @param threadNum 默认新建的消费者线程个数
     * @param limit     队列长度阈值;超过将唤醒阻塞的线程
     * @param period    工作线程对象的timeout方法前后两次执行的时间间隔周期
     * @param consumer  回调接口(初始化组价实例的时候需要传递)
     */
    public AccumulateBatchComponent2(String name, int threadNum, int limit, int period, Consumer<List<T>> consumer) {
        this.workThreads = new ArrayList<>(threadNum);
        if (threadNum > 1) {
            this.index = new AtomicInteger();
        }
        for (int i = 0; i < threadNum; ++i) {
            WorkThread<T> workThread = new WorkThread<>(limit, period, consumer);
            workThread.setName(name + i);

            this.workThreads.add(workThread);
            new Thread(workThread, name + ", 攒批线程" + i).start();
        }
    }

    /**
     * 生产者线程将对象添加到对应消费者线程对象内部的阻塞队列中去<br>
     * 内部采用HASH取模算法进行动态路由
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

    @Data
    public static class WorkThread<T> implements Runnable {

        private String name;

        private int limit;

        private long interval;

        private Consumer<List<T>> consumer;

        private BlockingQueue<T> queue;

        private List<T> list;

        private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("攒批定时器线程", true));

        private ScheduledFuture<?> scheduledFuture;

        public WorkThread(Integer limit, long interval, Consumer<List<T>> consumer) {
            this.limit = limit;
            this.interval = interval;
            this.consumer = consumer;
            this.list = new ArrayList<>(limit + 1);
            this.queue = new MpscBlockingConsumerArrayQueue<>(limit * 2);
        }

        public void add(T item) {
            boolean result = false;
            while (!result) {
                result = this.queue.offer(item);
            }
        }

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                T element = queue.take();
                addEvent(element);
            }
        }

        private synchronized void addEvent(T element) {
            list.add(element);
            if (list.size() >= limit) {
                flush(0L);
                return;
            }
            if (scheduledFuture == null) {
                long registerTimeStamp = System.currentTimeMillis();
                scheduledFuture = scheduledThreadPoolExecutor.schedule(() -> this.flush(registerTimeStamp), interval, TimeUnit.MILLISECONDS);
            }
        }

        public synchronized void flush(long registerTimeStamp) {
            if (CollUtil.isEmpty(list)) {
                return;
            }
            try {
                consumer.accept(list);
            } catch (Throwable e) {
                log.error("攒批线程消费失败", e);
            }
            if (registerTimeStamp == 0L) {
                log.info(name + "攒批大小到, 当前时间: " + DateUtil.formatDateTime(new Date()) +
                        ", list被消费了, 消费list大小" + list.size());
            } else {
                log.info(name + "攒批时间到, 定时器注册时间: " + DateUtil.format(new Date(registerTimeStamp), NORM_DATETIME_MS_PATTERN) +
                        ", 当前时间: " + DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN) + ", list被消费了, 消费list大小" + list.size());
            }
            list.clear();
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
        }

    }

}