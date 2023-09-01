package com.yanggu.metric_calculate.web.controller;

import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.web.pojo.dto.DeriveData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.json.JSONUtil;
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
    public Model getTableAndMetricByTableId(@Parameter(description = "明细宽表id", required = true) @PathVariable("tableId") Long tableId) {
        String jsonString = FileUtil.readUtf8String("mock_metric_config/" + tableId + SUFFIX);
        return JSONUtil.toBean(jsonString, Model.class);
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
    public List<Model> allTableData() {
        return FileUtil.loopFiles("mock_metric_config", fileFilter)
                .stream()
                .map(file -> JSONUtil.toBean(FileUtil.readUtf8String(file), Model.class))
                .collect(Collectors.toList());
    }

    @Operation(summary = "所有派生指标数据")
    @GetMapping("/all-derive-data")
    public List<DeriveData> allDeriveData() {
        List<Model> models = allTableData();
        if (CollUtil.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream()
                .flatMap(tempTable -> {
                    MetricCalculate metricCalculate = BeanUtil.copyProperties(tempTable, MetricCalculate.class);
                    MetricUtil.setFieldMap(metricCalculate);
                    return metricCalculate.getDeriveMetricsList().stream()
                            .map(tempDerive -> {
                                DeriveData deriveData = new DeriveData();
                                deriveData.setFieldMap(metricCalculate.getFieldMap());
                                deriveData.setDeriveMetrics(tempDerive);
                                deriveData.setUdafJarPathList(metricCalculate.getUdafJarPathList());
                                deriveData.setAviatorFunctionJarPathList(metricCalculate.getAviatorFunctionJarPathList());
                                return deriveData;
                            });
                })
                .toList();
    }

}
