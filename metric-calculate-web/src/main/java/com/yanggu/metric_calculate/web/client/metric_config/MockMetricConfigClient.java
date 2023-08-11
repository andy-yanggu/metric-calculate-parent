package com.yanggu.metric_calculate.web.client.metric_config;

import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 获取指标、宽表相关定义
 * <p>实际调用本身controller</p>
 */
@FeignClient(name = "metric-config-mock-api", path = "/mock-model", url = "${feign.metric-config-mock-api.url}")
public interface MockMetricConfigClient {

    /**
     * 根据数据明细宽表id获取明细宽表以及相关指标
     *
     * @param tableId 数据明细宽表的id
     * @return
     */
    @GetMapping("/{tableId}")
    Model getTableAndMetricByTableId(@PathVariable("tableId") Long tableId);

    /**
     * 获取所有宽表id
     *
     * @return
     */
    @GetMapping("/all-id")
    List<Long> getAllTableId();

}
