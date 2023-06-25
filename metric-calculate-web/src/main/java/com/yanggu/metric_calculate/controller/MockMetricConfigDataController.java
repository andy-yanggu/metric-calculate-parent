package com.yanggu.metric_calculate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.pojo.DeriveData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileFilter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "模拟指标配置数据")
@RequestMapping("/mock-model")
public class MockMetricConfigDataController {

    private static final String SUFFIX = ".json";

    private final FileFilter fileFilter = pathname -> pathname.getName().endsWith(SUFFIX);

    /**
     * 返回mock_metric_config目录下的json配置文件
     *
     * @param tableId 明细宽表id
     * @return
     */
    @Operation(summary = "返回指标配置数据")
    @GetMapping("/{tableId}")
    public DataDetailsWideTable getTableAndMetricByTableId(@Parameter(description = "明细宽表id", required = true) @PathVariable("tableId") Long tableId) {
        String jsonString = FileUtil.readUtf8String("mock_metric_config/" + tableId + SUFFIX);
        return JSONUtil.toBean(jsonString, DataDetailsWideTable.class);
    }

    @Operation(summary = "获取所有宽表id")
    @GetMapping("/all-id")
    public List<Long> getAllTableId() {
        return FileUtil.loopFiles("mock_metric_config", fileFilter)
                .stream()
                .map(file -> Long.parseLong(file.getName().split("\\.")[0]))
                .collect(Collectors.toList());
    }

    @Operation(summary = "所有宽表数据")
    @GetMapping("/all-data")
    List<DataDetailsWideTable> allTableData() {
        return FileUtil.loopFiles("mock_metric_config", fileFilter)
                .stream()
                .map(file -> JSONUtil.toBean(FileUtil.readUtf8String(file), DataDetailsWideTable.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "所有派生指标数据")
    @GetMapping("/all-derive-data")
    List<DeriveData> allDeriveData() {
        List<DataDetailsWideTable> dataDetailsWideTables = allTableData();
        if (CollUtil.isEmpty(dataDetailsWideTables)) {
            return Collections.emptyList();
        }
        return dataDetailsWideTables.stream()
                .flatMap(tempTable -> {
                    MetricCalculate metricCalculate = BeanUtil.copyProperties(tempTable, MetricCalculate.class);
                    MetricUtil.setFieldMap(metricCalculate);
                    List<Derive> deriveList = metricCalculate.getDerive();
                    return deriveList.stream()
                            .map(tempDerive -> {
                                DeriveData deriveData = new DeriveData();
                                deriveData.setTableId(metricCalculate.getId());
                                deriveData.setFieldMap(metricCalculate.getFieldMap());
                                deriveData.setDerive(tempDerive);
                                return deriveData;
                            });
                })
                .collect(Collectors.toList());
    }

}
