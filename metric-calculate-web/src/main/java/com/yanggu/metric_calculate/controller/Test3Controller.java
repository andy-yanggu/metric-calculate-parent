package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceBarrier;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test3")
public class Test3Controller {

    private RingBuffer<Event> ringBuffer;

    @PostConstruct
    public void init() {
        RingBuffer<Event> tempRingBuffer = RingBuffer.createMultiProducer(Event::new, 8);
        this.ringBuffer = tempRingBuffer;

        SequenceBarrier sequenceBarrier = tempRingBuffer.newBarrier();

        MyRunnable myRunnable = new MyRunnable();
        myRunnable.setRingBuffer(tempRingBuffer);
        myRunnable.setSequenceBarrier(sequenceBarrier);

        tempRingBuffer.addGatingSequences(myRunnable.getSequence());

        new Thread(myRunnable, "消费者线程").start();
    }

    @GetMapping("/test1")
    public void test1() {
        EventTranslator<Event> eventEventTranslator =
                (event, sequence) -> event.setValue("当前sequence, " + sequence + ", 当前时间: " + DateUtil.formatDateTime(new Date()));
        ringBuffer.publishEvent(eventEventTranslator);
    }

    @Data
    @Slf4j
    public static final class MyRunnable implements Runnable {

        private RingBuffer<Event> ringBuffer;

        private SequenceBarrier sequenceBarrier;

        private Sequence sequence = new Sequence();

        private List<Long> sequenceList = new ArrayList<>();

        private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1, new NamedThreadFactory("攒批定时器线程", true));

        private ScheduledFuture<?> scheduledFuture;

        @SneakyThrows
        @Override
        public void run() {
            sequenceBarrier.clearAlert();
            long nextSequence = sequence.get() + 1;
            while (true) {
                final long availableSequence = sequenceBarrier.waitFor(nextSequence);
                while (nextSequence <= availableSequence) {
                    addEvent(nextSequence);
                    nextSequence++;
                }
            }
        }
        
        private synchronized void addEvent(Long nextSequence) {
            sequenceList.add(nextSequence);
            if (sequenceList.size() >= 4) {
                flush(0L, nextSequence);
            }
            if (scheduledFuture == null) {
                long registerTimeStamp = System.currentTimeMillis();
                scheduledFuture = scheduledThreadPoolExecutor.schedule(() -> this.flush(registerTimeStamp, nextSequence), 5000, TimeUnit.MILLISECONDS);
            }
        }

        public synchronized void flush(long registerTimeStamp, Long nextSequence) {
            if (CollUtil.isEmpty(sequenceList)) {
                return;
            }
            if (registerTimeStamp == 0L) {
                log.info("攒批大小到, 当前时间: " + DateUtil.formatDateTime(new Date()) +
                        ", list被消费了, 消费list大小" + sequenceList.size());
            } else {
                log.info("攒批时间到, 定时器注册时间: " + DateUtil.formatDateTime(new Date(registerTimeStamp)) +
                        ", 当前时间: " + DateUtil.formatDateTime(new Date()) + ", list被消费了, 消费list大小" + sequenceList.size());
            }
            try {
                System.out.println("-----------------");
                sequenceList.stream()
                        .map(temp -> ringBuffer.get(temp).getValue())
                        .forEach(System.out::println);
            } catch (Throwable e) {
                log.error("攒批线程消费失败", e);
            }
            sequenceList.clear();
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
            sequence.set(nextSequence);
        }
        
    }

    @Data
    public static final class Event {

        private String value;

    }

}
