package com.yanggu.metric_calculate.controller;

import cn.hutool.core.date.DateUtil;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RestController
@RequestMapping("/test4")
public class Test4Controller {

    private Disruptor<Event> disruptor;

    private EventTranslator<Event> eventEventTranslator =
            (event, sequence) -> event.setValue("当前sequence, " + sequence + ", 当前时间: " + DateUtil.formatDateTime(new Date()));

    @PostConstruct
    public void init() {
        final int RING_BUFFER_SIZE = 1024;
        final int BATCH_SIZE = 10;
        final long TIMEOUT = 100;
        final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
        Disruptor<Event> tempDisruptor = new Disruptor<>(Event::new, RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.MULTI, new BatchWaitStrategy(BATCH_SIZE, TIMEOUT, TIME_UNIT));
        Consumer<List<Event>> consumer = list -> {
            System.out.println("--------------");
            System.out.println("list大小: " + list.size());
            list.forEach(System.out::println);
        };
        MyEventHandler<Event> longMyEventHandler = new MyEventHandler<>(consumer, BATCH_SIZE);
        tempDisruptor.handleEventsWith(longMyEventHandler);
        tempDisruptor.start();
        this.disruptor = tempDisruptor;
        for (int i = 0; i < 100; i++) {
            tempDisruptor.publishEvent(eventEventTranslator);
        }
    }

    @GetMapping("/test1")
    public void test1() {
        disruptor.publishEvent(eventEventTranslator);
    }

    @Data
    public static final class Event {

        private String value;

    }
}
