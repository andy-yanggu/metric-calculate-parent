package com.yanggu.metric_calculate.controller;

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
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "测试接口")
@RestController
@RequestMapping("/test")
public class TestController {

    private Queue<Request<String>> queue = new LinkedBlockingQueue<>(20);

    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(10);

    @PostConstruct
    public void init() {
        //queue
    }

    /**
     * 测试合并接口请求
     *
     * @return
     */
    @GetMapping("/test1")
    public DeferredResult<String> test1() {
        log.info(" 当前线程 外部 " + Thread.currentThread().getName());
        DeferredResult<String> result = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));
        result.onTimeout(() -> log.warn("请求超时"));
        result.onCompletion(() -> log.info("请求处理完成"));
        String uuid = IdUtil.fastSimpleUUID();
        log.info("uuid: {}", uuid);
        Request<String> request = new Request<>();
        request.setUuid(uuid);
        request.setCompletableFuture(new CompletableFuture<>());



        return result;
    }

    private void addRequest(Request<String> request) {
        boolean offer = queue.offer(request);
        if (!offer) {
            //queue.poll()
            List<Request<String>> collect = new ArrayList<>(queue);
            for (Request<String> stringRequest : collect) {
                log.info("uuid: {}, 数据被消费了", stringRequest.getUuid());
                //设置完成的标志
                stringRequest.getCompletableFuture().complete(request.getUuid());
            }

        }
    }

    @Data
    public static class Request<T> {

        private String uuid;

        private CompletableFuture<T> completableFuture;

    }


}
