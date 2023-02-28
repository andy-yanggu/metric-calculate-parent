package com.yanggu.metric_calculate.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import com.yanggu.metric_calculate.util.AccumulateBatchComponent;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private AccumulateBatchComponent<Request<String>> component;

    @PostConstruct
    public void init() {
        Consumer<List<Request<String>>> consumer = requests -> {
            for (TestController.Request<String> request : requests) {
                request.getCompletableFuture().complete(request.getUuid());
            }
        };
        this.component = new AccumulateBatchComponent<>(SystemUtil.getTotalThreadCount(), 100, 2000, 200, consumer);
    }

    /**
     * 测试合并接口请求
     *
     * @return
     */
    @GetMapping("/test1")
    public DeferredResult<String> test1() {
        DeferredResult<String> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));
        String uuid = IdUtil.fastSimpleUUID();
        Request<String> request = new Request<>();
        request.setUuid(uuid);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);

        //进行攒批处理
        component.add(request);

        completableFuture.whenComplete((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    @Data
    public static class Request<T> {

        private String uuid;

        private CompletableFuture<T> completableFuture;

    }


}
