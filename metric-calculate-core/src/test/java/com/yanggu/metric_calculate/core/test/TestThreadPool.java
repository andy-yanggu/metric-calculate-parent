package com.yanggu.metric_calculate.core.test;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestThreadPool {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);
        executorService.scheduleAtFixedRate(new Runnable() {
            private int count = 0;
            private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String time = simpleDateFormat.format(new Date());
                System.out.println(time + " >>第"+ (++count) + "次执行定时任务.");
            }
        }, 0, 2000, TimeUnit.MILLISECONDS);
        Thread.sleep(Long.MAX_VALUE);
    }

}
