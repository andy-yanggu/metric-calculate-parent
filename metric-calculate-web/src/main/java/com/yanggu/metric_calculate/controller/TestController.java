package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_PATTERN;

@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/test1")
public class TestController {

    private AccumulateBatchComponent<Request<String>> component;

    @PostConstruct
    public void init() {
        Consumer<List<Request<String>>> consumer = requests -> {
            for (TestController.Request<String> request : requests) {
                request.getCompletableFuture().complete(request.getUuid() + ", " + DateUtil.format(new Date(), NORM_DATETIME_MS_PATTERN));
            }
        };
        this.component = new AccumulateBatchComponent<>("测试攒批组件", 1, 10, 200, consumer);
    }

    /**
     * 测试合并接口请求
     *
     * @return
     */
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
