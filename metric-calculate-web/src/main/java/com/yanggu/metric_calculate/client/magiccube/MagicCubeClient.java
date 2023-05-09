package com.yanggu.metric_calculate.client.magiccube;

import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 获取magiccube指标、宽表相关定义
 */
@FeignClient(name = "magiccube-api", url = "${feign.magiccube-api.host}")
public interface MagicCubeClient {

    /**
     * 根据数据明细宽表id获取明细宽表以及派生指标和复合指标
     *
     * @param tableId 数据明细宽表的id
     * @return
     */
    @GetMapping("/model/{tableId}")
    DataDetailsWideTable getTableAndMetricByTableId(@PathVariable("tableId") Long tableId);

    /**
     * 获取所有宽表id
     *
     * @return
     */
    @GetMapping("/model/all-id")
    List<Long> getAllTableId();

}
