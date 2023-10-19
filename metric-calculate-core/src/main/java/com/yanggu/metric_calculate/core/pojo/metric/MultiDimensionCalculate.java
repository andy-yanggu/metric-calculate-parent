package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import lombok.Data;

import java.util.List;

/**
 * 多维度计算
 */
@Data
public class MultiDimensionCalculate {

    /**
     * 维度字段
     */
    private List<ModelDimensionColumn> modelDimensionColumn;

    /**
     * 计算表达式
     */
    private String calculateExpression;

}
