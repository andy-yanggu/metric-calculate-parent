package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelDimensionColumn;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.ModelTimeColumn;
import com.yanggu.metric_calculate.core.pojo.udaf_param.AggregateFunctionParam;
import com.yanggu.metric_calculate.core.pojo.window.WindowParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * 派生指标
 */
@Data
public class DeriveMetrics implements Serializable {

    @Serial
    private static final long serialVersionUID = 7997617771105250097L;

    /**
     * 派生指标id
     */
    private Long id;

    /**
     * 宽表id
     */
    private Long modelId;

    /**
     * 派生指标名称
     */
    private String name;

    /**
     * 派生指标中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 维度字段
     */
    private List<ModelDimensionColumn> dimensionList;

    /**
     * 时间字段
     */
    private ModelTimeColumn timeColumn;

    /**
     * 前置过滤条件
     * <p>Aviator表达式参数</p>
     */
    private AviatorExpressParam filterExpressParam;

    /**
     * 聚合函数参数
     */
    private AggregateFunctionParam aggregateFunctionParam;

    /**
     * 窗口相关参数
     */
    private WindowParam windowParam;

    /**
     * 精度相关
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 是否包含当前笔, 默认包含
     */
    private Boolean includeCurrent = true;

}
