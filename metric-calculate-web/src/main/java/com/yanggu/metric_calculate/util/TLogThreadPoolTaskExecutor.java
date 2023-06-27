package com.yanggu.metric_calculate.util;

import com.yomahub.tlog.core.thread.TLogInheritableTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/**
 * TLog线程池继承自SpringBoot线程池
 * <p>能够提供监控信息</p>
 * <p>能够增加traceId</p>
 */
public class TLogThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    private static final long serialVersionUID = 5397432096046779481L;

    public TLogThreadPoolTaskExecutor(int corePoolSize,
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
        setTaskDecorator(this::wrap);
        super.initialize();
        super.prefersShortLivedTasks();
    }

    private Runnable wrap(Runnable command) {
        if (command instanceof TLogInheritableTask) {
            return command;
        } else {
            return new TLogInheritableTask() {

                @Override
                public void runTask() {
                    command.run();
                }
            };
        }
    }

}