package com.yanggu.metric_calculate.core.field_process.dimension;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONUtil;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 维度字段处理器，从原始数据中提取出维度数据
 */
@Data
@Slf4j
@NoArgsConstructor
public class DimensionSetProcessor implements FieldProcessor<Map<String, Object>, DimensionSet> {

    /**
     * 指标标识(数据明细宽表id-指标id)
     */
    private String key;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 维度字段
     */
    private List<ModelDimensionColumn> modelDimensionColumnList;

    public DimensionSetProcessor(List<ModelDimensionColumn> modelDimensionColumnList) {
        this.modelDimensionColumnList = modelDimensionColumnList;
    }

    @Override
    public void init() {
        if (CollUtil.isNotEmpty(modelDimensionColumnList)) {
            //按照ColumnIndex进行升序排序
            this.modelDimensionColumnList = modelDimensionColumnList.stream()
                    .sorted(Comparator.comparingInt(ModelDimensionColumn::getColumnIndex))
                    .toList();
        }
    }

    @Override
    public DimensionSet process(Map<String, Object> input) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        if (CollUtil.isNotEmpty(modelDimensionColumnList)) {
            for (ModelDimensionColumn modelDimensionColumn : modelDimensionColumnList) {
                Object result = input.get(modelDimensionColumn.getColumnName());
                if (result == null) {
                    throw new RuntimeException(StrUtil.format("没有对应的维度值, 字段名称: {}, 原始数据: {}",
                            modelDimensionColumn.getColumnName(), JSONUtil.toJsonStr(input)));
                }
                map.put(modelDimensionColumn.getDimensionName(), result);
            }
        }
        return new DimensionSet(key, metricName, map);
    }

}
