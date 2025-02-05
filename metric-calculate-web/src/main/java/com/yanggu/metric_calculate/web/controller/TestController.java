package com.yanggu.metric_calculate.web.controller;

import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent;
import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static org.dromara.hutool.core.date.DateFormatPool.NORM_DATETIME_MS_PATTERN;


@Slf4j
@RestController
@Tag(name = "测试接口")
@RequestMapping("/test")
public class TestController {

    private AccumulateBatchComponent<Request<String>> component;

    private AccumulateBatchComponent2<Request<String>> component2;

    @PostConstruct
    public void init() {
        Consumer<List<Request<String>>> consumer = requests -> {
            for (TestController.Request<String> request : requests) {
                request.getCompletableFuture().complete(request.getUuid() + ", " + DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN));
            }
        };
        this.component = new AccumulateBatchComponent<>("测试攒批组件1", 1, 10, 200, consumer);
        this.component2 = new AccumulateBatchComponent2<>("测试攒批组件2", 1, 10, 200, consumer);
    }

    /**
     * 测试合并接口请求
     *
     * @return
     */
    @Operation(summary = "测试攒批组件1")
    @GetMapping("/test1")
    public DeferredResult<String> test1() {
        DeferredResult<String> deferredResult = new DeferredResult<>(2000L);

        String uuid = DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN);
        Request<String> request = new Request<>();
        request.setUuid(uuid);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);

        //进行攒批处理
        component.add(request);

        completableFuture.whenComplete((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    /**
     * 测试合并接口请求
     *
     * @return
     */
    @Operation(summary = "测试攒批组件2")
    @GetMapping("/test2")
    public DeferredResult<String> test2() {
        DeferredResult<String> deferredResult = new DeferredResult<>(2000L);

        String uuid = DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN);
        Request<String> request = new Request<>();
        request.setUuid(uuid);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);

        //进行攒批处理
        component2.add(request);

        completableFuture.whenComplete((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    @GetMapping("/test-log")
    public void testLog() {
        log.debug("测试debug日志");
        log.info("测试info日志");
        log.warn("测试warn日志");
        log.error("测试error日志");
    }

    @Data
    public static class Request<T> {

        private String uuid;

        private CompletableFuture<T> completableFuture;

    }

}
