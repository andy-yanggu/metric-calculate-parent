package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.NamedThreadFactory;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;

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
    public static final class MyRunnable implements Runnable {

        private RingBuffer<Event> ringBuffer;

        private SequenceBarrier sequenceBarrier;

        private Sequence sequence = new Sequence();

        @SneakyThrows
        @Override
        public void run() {
            sequenceBarrier.clearAlert();
            long nextSequence = sequence.get() + 1;
            while (true) {
                final long availableSequence = sequenceBarrier.waitFor(nextSequence);
                while (nextSequence <= availableSequence) {
                    Event event = ringBuffer.get(nextSequence);
                    nextSequence++;
                    System.out.println("消费数据" + event);
                }
                sequence.set(availableSequence);
            }
        }
    }

    @Data
    public static final class Event {

        private String value;

    }

}
