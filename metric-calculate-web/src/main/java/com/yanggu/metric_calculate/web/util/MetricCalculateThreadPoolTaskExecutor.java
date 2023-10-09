package com.yanggu.metric_calculate.web.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serial;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * TLog线程池继承自SpringBoot线程池
 * <p>能够提供监控信息</p>
 */
public class MetricCalculateThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Serial
    private static final long serialVersionUID = 5397432096046779481L;

    public MetricCalculateThreadPoolTaskExecutor(int corePoolSize,
                                                 int maxPoolSize,
                                                 int keepAliveSeconds,
                                                 int queueCapacity,
                                                 ThreadFactory threadFactory,
                                                 RejectedExecutionHandler handler) {
        super();
        setCorePoolSize(corePoolSize);
        setMaxPoolSize(maxPoolSize);
        setKeepAliveSeconds(keepAliveSeconds);
        setQueueCapacity(queueCapacity);
        setThreadFactory(threadFactory);
        setRejectedExecutionHandler(handler);
        super.initialize();
        super.prefersShortLivedTasks();
    }

}