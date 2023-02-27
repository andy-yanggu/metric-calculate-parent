package com.yanggu.metric_calculate.controller;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private final Integer length = 20;

    private Integer intervalTime = 10;

    private ScheduledFuture<?> scheduledFuture;

    private final Queue<Request<String>> queue = new LinkedBlockingQueue<>(length);

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    /**
     * 测试合并接口请求
     *
     * @return
     */
    @GetMapping("/test1")
    public DeferredResult<String> test1() {
        log.info("当前线程外部 " + Thread.currentThread().getName());

        DeferredResult<String> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        deferredResult.onTimeout(() -> log.warn("请求超时"));
        deferredResult.onCompletion(() -> log.info("请求处理完成"));
        String uuid = IdUtil.fastSimpleUUID();

        log.info("uuid: {}", uuid);

        Request<String> request = new Request<>();
        request.setUuid(uuid);
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        request.setCompletableFuture(completableFuture);

        addRequest(request);

        completableFuture.whenCompleteAsync((result, throwable) -> {
            deferredResult.setResult(result);
        });
        return deferredResult;
    }

    private void addRequest(Request<String> request) {
        queue.offer(request);
        if (queue.size() >= length) {
            flush();
        }
        //如果没有注册定时器, 注册一个定时器
        if (scheduledFuture == null) {
            this.scheduledFuture = scheduledExecutorService.schedule(this::flush, intervalTime, TimeUnit.MILLISECONDS);
        }
    }

    private void flush() {
        Request<String> request;
        List<Request<String>> requestList = new ArrayList<>();
        log.info("进入了flush方法");
        while ((request = queue.poll()) != null) {
            requestList.add(request);
            log.info("执行poll方法");
        }
        for (Request<String> stringRequest : requestList) {
            log.info("uuid: {}, 数据被消费了", stringRequest.getUuid());
            //设置完成的标志
            stringRequest.getCompletableFuture().complete(stringRequest.getUuid());
        }
        if (scheduledFuture != null) {
            this.scheduledFuture.cancel(true);
            this.scheduledFuture = null;
        }
    }

    @Data
    public static class Request<T> {

        private String uuid;

        private CompletableFuture<T> completableFuture;

    }


}
