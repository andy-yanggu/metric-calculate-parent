package com.yanggu.metric_calculate.core.pojo.data_detail_table;

import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import lombok.Data;

import java.util.List;


/**
 * 数据明细宽表
 */
@Data
public class Model {

    /**
     * 数据明细宽表id
     */
    private Long id;

    /**
     * 宽表名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 宽表字段
     */
    private List<ModelColumn> modelColumnList;

    /**
     * 派生指标
     */
    private List<DeriveMetrics> deriveMetricsList;

    /**
     * 自定义udf-jar的路径
     */
    private List<String> aviatorFunctionJarPathList;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

}
