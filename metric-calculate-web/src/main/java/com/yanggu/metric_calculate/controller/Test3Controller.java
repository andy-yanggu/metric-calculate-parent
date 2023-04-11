//package com.yanggu.metric_calculate.controller;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.thread.NamedThreadFactory;
//import com.lmax.disruptor.EventTranslator;
//import com.lmax.disruptor.RingBuffer;
//import com.lmax.disruptor.Sequence;
//import com.lmax.disruptor.SequenceBarrier;
//import lombok.Data;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.ScheduledFuture;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@RequestMapping("/test3")
//public class Test3Controller {
//
//    private RingBuffer<Event> ringBuffer;
//
//    @PostConstruct
//    public void init() {
//        RingBuffer<Event> tempRingBuffer = RingBuffer.createMultiProducer(Event::new, 8, new MyBlockingWaitStrategy());
//        this.ringBuffer = tempRingBuffer;
//
//        SequenceBarrier sequenceBarrier = tempRingBuffer.newBarrier();
//
//        MyRunnable myRunnable = new MyRunnable();
//        myRunnable.setRingBuffer(tempRingBuffer);
//        myRunnable.setSequenceBarrier(sequenceBarrier);
//
//        tempRingBuffer.addGatingSequences(myRunnable.getSequence());
//
//        new Thread(myRunnable, "消费者线程").start();
//    }
//
//    @GetMapping("/test1")
//    public void test1() {
//        EventTranslator<Event> eventEventTranslator =
//                (event, sequence) -> event.setValue("当前sequence, " + sequence + ", 当前时间: " + DateUtil.formatDateTime(new Date()));
//        ringBuffer.publishEvent(eventEventTranslator);
//    }
//
//    @Data
//    @Slf4j
//    public static final class MyRunnable implements Runnable {
//
//        private RingBuffer<Event> ringBuffer;
//
//        private SequenceBarrier sequenceBarrier;
//
//        private Sequence sequence = new Sequence();
//
//        private List<Long> sequenceList = new ArrayList<>();
//
//        private List<Event> eventList = new ArrayList<>();
//
//        @SneakyThrows
//        @Override
//        public void run() {
//            sequenceBarrier.clearAlert();
//            long nextSequence = sequence.get() + 1;
//            while (true) {
//                final long availableSequence = sequenceBarrier.waitFor(nextSequence + 3);
//                while (nextSequence <= availableSequence) {
//                    eventList.add(ringBuffer.get(nextSequence));
//                    nextSequence++;
//                }
//                System.out.println("-----------------");
//                eventList.forEach(System.out::println);
//                eventList.clear();
//                sequence.set(nextSequence);
//            }
//        }
//
//    }
//
//    @Data
//    public static final class Event {
//
//        private String value;
//
//    }
//
//}
