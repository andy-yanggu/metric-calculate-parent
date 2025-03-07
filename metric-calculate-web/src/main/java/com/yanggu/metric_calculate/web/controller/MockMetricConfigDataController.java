package com.yanggu.metric_calculate.web.controller;

import com.alibaba.fastjson2.JSON;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.web.pojo.dto.DeriveData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.MultiResource;
import org.dromara.hutool.core.io.resource.Resource;
import org.dromara.hutool.core.io.resource.ResourceFinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@RestController
@Tag(name = "模拟指标配置数据")
@RequestMapping("/mock-model")
public class MockMetricConfigDataController {

    private List<Model> modelList;

    @PostConstruct
    public void init() {
        MultiResource resources = ResourceFinder.of().find("mock_metric_config" + File.separator + "*.json");
        List<Model> list = new ArrayList<>();
        for (Resource resource : resources) {
            String jsonString = IoUtil.readUtf8(resource.getStream());
            Model tempModel = JSON.parseObject(jsonString, Model.class);
            list.add(tempModel);
        }
        this.modelList = list;
    }

    /**
     * 返回mock_metric_config目录下的json配置文件
     *
     * @param tableId 明细宽表id
     * @return
     */
    @Operation(summary = "返回指标配置数据")
    @GetMapping("/{tableId}")
    @Parameter(name = "tableId", description = "明细宽表id", required = true, in = PATH)
    public Model getTableAndMetricByTableId(@PathVariable("tableId") Long tableId) {
        return modelList.stream()
                .filter(temp -> temp.getId().equals(tableId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("传入的tableId错误"));
    }

    @Operation(summary = "获取所有宽表id")
    @GetMapping("/all-id")
    public List<Long> getAllTableId() {
        return modelList.stream()
                .map(Model::getId)
                .toList();
    }

    @Operation(summary = "所有宽表数据")
    @GetMapping("/all-data")
    public List<Model> allTableData() {
        return modelList;
    }

    @Operation(summary = "所有派生指标数据")
    @GetMapping("/all-derive-data")
    public List<DeriveData> allDeriveData() {
        return modelList.stream()
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
