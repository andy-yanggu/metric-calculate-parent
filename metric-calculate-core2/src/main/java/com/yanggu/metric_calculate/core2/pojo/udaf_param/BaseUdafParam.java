package com.yanggu.metric_calculate.core2.pojo.udaf_param;


import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数值型、集合型、对象型聚合函数相关参数
 */
@Data
public class BaseUdafParam {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    private String metricExpress;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    private List<String> metricExpressList;

    private List<String> retainExpressList;

    /**
     * 保留字段表达式（retainExpress）：对象型和集合型只保留指定字段的值
     */
    private String retainExpress;

    /**
     * 对象型比较字段列表(对象型最大对象、最小对象)
     */
    private List<String> objectiveCompareFieldList;

    /**
     * 排序字段列表（sortFieldList）：类似SQL中的ORDER BY id ASC, user_name DESC，多字段排序。
     * <p>对象型（最大对象、最小对象）、集合型（排序列表）</p>
     */
    private List<FieldOrderParam> collectiveSortFieldList;

    /**
     * 去重字段列表（distinctFieldList）：根据多个字段进行去重。集合型（去重列表）
     */
    private List<String> distinctFieldList;

    /**
     * udaf函数中的参数（param）：例如排序列表，指定limit参数，限制条数。key和Java字段名称一致
     */
    private Map<String, Object> param;

}
