package com.yanggu.metric_calculate.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "模拟指标配置数据")
@RequestMapping("/mock-model")
public class MockMetricConfigDataController {

    /**
     * 返回mock_metric_config目录下的json配置文件
     *
     * @param tableId 明细宽表id
     * @return
     */
    @Operation(summary = "返回指标配置数据")
    @GetMapping("/{tableId}")
    public DataDetailsWideTable getTableAndMetricByTableId(@Parameter(description = "明细宽表id", required = true) @PathVariable("tableId") Long tableId) {
        String jsonString = FileUtil.readUtf8String("mock_metric_config/" + tableId + ".json");
        return JSONUtil.toBean(jsonString, DataDetailsWideTable.class);
    }

    @Operation(summary = "获取所有宽表id")
    @GetMapping("/all-id")
    public List<Long> getAllTableId() {
        return FileUtil.loopFiles("mock_metric_config", pathname -> pathname.getName().endsWith(".json"))
                .stream()
                .map(file -> Long.parseLong(file.getName().split("\\.")[0]))
                .collect(Collectors.toList());
    }

}
