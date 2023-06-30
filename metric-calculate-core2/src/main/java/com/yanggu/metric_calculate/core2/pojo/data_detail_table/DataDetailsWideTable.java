package com.yanggu.metric_calculate.core2.pojo.data_detail_table;

import com.yanggu.metric_calculate.core2.pojo.metric.Composite;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import lombok.Data;

import java.util.List;


/**
 * 数据明细宽表
 */
@Data
public class DataDetailsWideTable {

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
    private List<Fields> fieldList;

    /**
     * 派生指标
     */
    private List<Derive> deriveList;

    /**
     * 复合指标
     */
    private List<Composite> compositeList;

    /**
     * 全局指标
     */
    private List<Global> globalList;

    /**
     * 自定义udf-jar的路径
     */
    private List<String> aviatorFunctionJarPathList;

    /**
     * 自定义udaf-jar的路径
     */
    private List<String> udafJarPathList;

}
