package com.yanggu.metric_calculate.web.timer;

import com.yanggu.metric_calculate.web.service.MetricConfigDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 指标配置数据定时器
 */
@Slf4j
@Component
public class MetricConfigDataTimer {

    @Autowired
    private MetricConfigDataService metricConfigDataService;

    /**
     * 定期刷新指标元数据
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledRefreshMetric() {
        log.info("定时刷新指标配置数据任务开始");
        metricConfigDataService.buildAllMetric();
    }

}
