package com.yanggu.metric_calculate.core2.util;

import com.yomahub.tlog.core.thread.TLogInheritableTask;

import java.util.concurrent.*;

/**
 * TLog线程池
 * <p>能够增加traceId</p>
 */
public class TLogThreadPoolExecutor extends ThreadPoolExecutor {

    public TLogThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command));
    }

    private TLogInheritableTask wrap(Runnable command) {
        if (command instanceof TLogInheritableTask) {
            return ((TLogInheritableTask) command);
        }
        return new TLogInheritableTask() {
            @Override
            public void runTask() {
                command.run();
            }
        };
    }

}
