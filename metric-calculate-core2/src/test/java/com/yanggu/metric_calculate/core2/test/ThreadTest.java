package com.yanggu.metric_calculate.core2.test;


import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class ThreadTest {

    @Test
    public void test1() throws InterruptedException {
        MyRunnable<String> runnable = new MyRunnable<>();
        runnable.setLimit(10);
        runnable.setInterval(1000);
        new Thread(runnable).start();

        SynchronousQueue<String> synchronousQueue = runnable.getSynchronousQueue();
        synchronousQueue.put("");
    }

    @Data
    public static class MyRunnable<T> implements Runnable {

        private SynchronousQueue<T> synchronousQueue = new SynchronousQueue<>();

        private List<T> list = new ArrayList<>();

        private Integer limit;

        private long interval;

        private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

        private ScheduledFuture<?> scheduledFuture;

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                addEvent();
            }
        }

        private synchronized void addEvent() throws InterruptedException {
            T element = synchronousQueue.take();
            list.add(element);
            if (list.size() > limit) {
                flush();
                return;
            }
            if (scheduledFuture == null) {
                scheduledThreadPoolExecutor.schedule(this::flush, interval, TimeUnit.MILLISECONDS);
            }
        }

        public synchronized void flush() {
            if (CollUtil.isEmpty(list)) {
                return;
            }
            System.out.println("list被消费了");
            list.clear();
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
        }
    }


}
