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
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.ResourceUtil;
import org.dromara.hutool.json.JSONUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

@RestController
@Tag(name = "模拟指标配置数据")
@RequestMapping("/mock-model")
public class MockMetricConfigDataController {

    private static final String SUFFIX = ".json";

    private static final String MOCK_DIRECTORY_NAME = "mock_metric_config";

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
        String jsonString = ResourceUtil.readUtf8Str(MOCK_DIRECTORY_NAME + File.separator + tableId + SUFFIX);
        return JSONUtil.toBean(jsonString, Model.class);
    }

    @Operation(summary = "获取所有宽表id")
    @GetMapping("/all-id")
    public List<Long> getAllTableId() throws Exception {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(CLASSPATH_ALL_URL_PREFIX + MOCK_DIRECTORY_NAME + "/*");
        List<Long> list = new ArrayList<>();
        for (Resource resource : resources) {
            String path = resource.getURL().getPath();
            path = path.substring(path.lastIndexOf(MOCK_DIRECTORY_NAME));
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            list.add(Long.parseLong(fileName.split("\\.")[0]));
        }
        return list;
    }

    @Operation(summary = "所有宽表数据")
    @GetMapping("/all-data")
    public List<Model> allTableData() throws Exception {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources(CLASSPATH_ALL_URL_PREFIX +MOCK_DIRECTORY_NAME + "/*");
        List<Model> list = new ArrayList<>();
        for (Resource resource : resources) {
            list.add(JSONUtil.toBean(IoUtil.readUtf8(resource.getInputStream()), Model.class));
        }
        return list;
    }

    @Operation(summary = "所有派生指标数据")
    @GetMapping("/all-derive-data")
    public List<DeriveData> allDeriveData() throws Exception {
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
