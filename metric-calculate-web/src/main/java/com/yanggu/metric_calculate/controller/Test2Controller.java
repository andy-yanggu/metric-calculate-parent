package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.IdUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/test2")
public class Test2Controller {

    private SynchronousQueue<TestController.Request<String>> synchronousQueue;

    @PostConstruct
    public void init() {
        MyRunnable<TestController.Request<String>> runnable = new MyRunnable<>();
        runnable.setLimit(2);
        runnable.setInterval(10000);
        Consumer<List<TestController.Request<String>>> consumer = requests -> {
            for (TestController.Request<String> request : requests) {
                request.getCompletableFuture().complete(request.getUuid() + ", 离开系统时间: " + DateUtil.formatDateTime(new Date()));
            }
        };
        runnable.setConsumer(consumer);
        synchronousQueue = runnable.getSynchronousQueue();
        new Thread(runnable, "攒批线程").start();
    }

    @GetMapping("/test1")
    public void test1() throws Exception {
        DeferredResult<String> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));
        TestController.Request<String> request = new TestController.Request<>();
        request.setUuid("进入系统时间: " + DateUtil.formatDateTime(new Date()));
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);
        synchronousQueue.put(request);
        completableFuture.thenAccept(deferredResult::setResult);
    }


    @Data
    public static class MyRunnable<T> implements Runnable {

        private SynchronousQueue<T> synchronousQueue = new SynchronousQueue<>();

        private List<T> list = new ArrayList<>();

        private Integer limit;

        private long interval;

        private Consumer<List<T>> consumer;

        private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("定时器线程", false));

        private ScheduledFuture<?> scheduledFuture;

        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                T element = synchronousQueue.take();
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
            consumer.accept(list);
            if (registerTimeStamp == 0L) {
                System.out.println("攒批大小到, 当前时间: " + DateUtil.formatDateTime(new Date()) +
                        ", list被消费了, 消费list大小" + list.size());
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                    scheduledFuture = null;
                }
            } else {
                System.out.println("攒批时间到, 定时器注册时间: " + DateUtil.formatDateTime(new Date(registerTimeStamp)) +
                        ", 当前时间: " + DateUtil.formatDateTime(new Date()) + ", list被消费了, 消费list大小" + list.size());
            }
            list.clear();
        }
    }

}