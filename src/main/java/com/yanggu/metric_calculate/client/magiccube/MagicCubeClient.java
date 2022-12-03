package com.yanggu.metric_calculate.client.magiccube;

import com.yanggu.metric_calculate.client.magiccube.pojo.DataDetailsWideTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 获取magiccube指标、宽表相关定义
 */
@FeignClient(name = "magiccube-api", url = "${feign.magiccube-api.host}")
public interface MagicCubeClient {

    /**
     * 根据数据明细宽表id获取明细宽表以及原子指标、派生指标和复合指标
     *
     * @param tableId 数据明细宽表的id
     * @return
     */
    @GetMapping("/model/getUsedInfoById/{tableId}")
    DataDetailsWideTable getTableAndMetricById(@PathVariable("tableId") Long tableId);

}
