package com.yanggu.metric_calculate.timer;

import com.yanggu.metric_calculate.service.MetricConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MetricConfigTimer {

    @Autowired
    private MetricConfigService metricConfigService;

    /**
     * 定期刷新指标元数据
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledRefreshMetric() {
        metricConfigService.buildAllMetric();
    }

}
