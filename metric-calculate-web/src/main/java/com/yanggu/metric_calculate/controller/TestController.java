package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private final Integer length = 200;

    private Integer intervalTime = 50;

    private final Queue<Request<String>> queue = new LinkedBlockingQueue<>();

    private final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @PostConstruct
    public void init() {
        this.scheduledExecutorService.scheduleAtFixedRate(() -> this.flush(false),
                intervalTime, intervalTime, TimeUnit.MILLISECONDS);
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

        addRequest(request);

        completableFuture.whenComplete((result, throwable) -> deferredResult.setResult(result));
        return deferredResult;
    }

    private void addRequest(Request<String> request) {
        queue.offer(request);
        if (queue.size() >= length) {
            flush(true);
        }
    }

    private void flush(boolean lengthMatch) {
        Request<String> request;
        List<Request<String>> requestList = new ArrayList<>();
        while ((request = queue.poll()) != null) {
            requestList.add(request);
        }
        if (CollUtil.isEmpty(requestList)) {
            return;
        }
        if (lengthMatch) {
            log.info("满足数量, 攒批的数据大小: {}, 线程: {}", requestList.size(), Thread.currentThread().getName());
        } else {
            log.info("到达时间, 攒批的数据大小: {}, 线程: {}", requestList.size(), Thread.currentThread().getName());
        }
        for (Request<String> stringRequest : requestList) {
            //设置完成的标志
            stringRequest.getCompletableFuture().complete(stringRequest.getUuid());
        }
    }

    @Data
    public static class Request<T> {

        private String uuid;

        private CompletableFuture<T> completableFuture;

    }


}
