package com.yanggu.metric_calculate.web.consumer;

import com.yanggu.metric_calculate.web.service.MetricDataService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消费事后成功数据
 */
//@Component
public class KafkaConsumer {

    @Autowired
    private MetricDataService metricDataService;

    /**
     * 批量消费明细数据
     *
     * @param records
     */
    @KafkaListener(groupId = "${spring.application.name}", topics = "${spring.kafka.consumer.topic}")
    public void onMessage(List<ConsumerRecord<String, String>> records) throws Exception {
        if (CollUtil.isEmpty(records)) {
            return;
        }
        Map<Long, List<JSONObject>> collect = records.stream()
                .map(ConsumerRecord::value)
                .map(JSONUtil::parseObj)
                .collect(Collectors.groupingBy(temp -> temp.getLong("tableId")));
        for (Map.Entry<Long, List<JSONObject>> entry : collect.entrySet()) {
            Long tempTableId = entry.getKey();
            List<Map<String, Object>> tempList = entry.getValue().stream()
                    .map(temp -> temp.toMap(String.class, Object.class))
                    .toList();
            metricDataService.fullFillDeriveData(tempList, tempTableId);
        }
    }

}