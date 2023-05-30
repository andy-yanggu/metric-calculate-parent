package com.yanggu.metric_calculate.client.metric_config;

import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 获取指标、宽表相关定义
 */
@FeignClient(name = "metric-config-api", path = "/mock-model", url = "${feign.metric-config-api.host}")
public interface MetricConfigClient {

    /**
     * 根据数据明细宽表id获取明细宽表以及派生指标和复合指标
     *
     * @param tableId 数据明细宽表的id
     * @return
     */
    @GetMapping("/{tableId}")
    DataDetailsWideTable getTableAndMetricByTableId(@PathVariable("tableId") Long tableId);

    /**
     * 获取所有宽表id
     *
     * @return
     */
    @GetMapping("/all-id")
    List<Long> getAllTableId();

}
