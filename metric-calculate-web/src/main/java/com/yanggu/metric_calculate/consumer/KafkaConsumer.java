package com.yanggu.metric_calculate.consumer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.service.MetricDataService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 消费事后成功数据
 */
@Component
public class KafkaConsumer {

    @Autowired
    private MetricDataService metricDataService;

    /**
     * 批量消费成功的明细数据
     * <p>topic的分区策略是一个table的数据，放入到同一个分区中</p>
     *
     * @param records
     */
    @KafkaListener(groupId = "${spring.application.name}", topics = "${spring.kafka.consumer.topic}")
    public void onMessage(List<ConsumerRecord<String, String>> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        Long tableId = Long.parseLong(records.get(0).key());
        List<JSONObject> collect = records.stream()
                .map(ConsumerRecord::value)
                .map(JSONUtil::parseObj)
                .collect(Collectors.toList());
        metricDataService.fullFillDeriveData(collect, tableId);
    }

}