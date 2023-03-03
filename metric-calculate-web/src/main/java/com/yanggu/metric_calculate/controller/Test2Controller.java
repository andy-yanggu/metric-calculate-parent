package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RestController
@RequestMapping("/test2")
public class Test2Controller {

    private AccumulateBatchComponent2<TestController.Request<String>> accumulateBatchComponent2;

    @PostConstruct
    public void init() {
        Consumer<List<TestController.Request<String>>> consumer = requests -> {
            for (TestController.Request<String> request : requests) {
                request.getCompletableFuture().complete(request.getUuid() + ", 离开系统时间: " + DateUtil.formatDateTime(new Date()));
            }
        };
        this.accumulateBatchComponent2 = new AccumulateBatchComponent2<>("测试组件", 1, 10, 2000, consumer);
    }

    @GetMapping("/test1")
    public /*DeferredResult<String>*/ void test1() {
        DeferredResult<String> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));
        TestController.Request<String> request = new TestController.Request<>();
        request.setUuid("进入系统时间: " + DateUtil.formatDateTime(new Date()));
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);

        //进行攒批处理
        this.accumulateBatchComponent2.add(request);

        completableFuture.thenAccept(deferredResult::setResult);
        //return deferredResult;
    }

}