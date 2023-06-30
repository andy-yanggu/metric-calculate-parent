package com.yanggu.metric_calculate.core2.pojo.udaf_param;


import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrderParam;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数值型、集合型、对象型聚合函数相关参数
 */
@Data
public class BaseUdafParam implements Serializable {

    private static final long serialVersionUID = 8183085404842634515L;

    /**
     * 聚合类型
     */
    private String aggregateType;

    private AviatorExpressParam metricExpressParam;

    /**
     * 度量字段表达式（metricExpress）：数值型需要，编写一个表达式，计算输出数值
     */
    private String metricExpress;

    private List<AviatorExpressParam> metricExpressParamList;

    /**
     * 多字段度量字段表达式：数值型需要。例如协方差需要两个参数
     */
    private List<String> metricExpressList;

    private AviatorExpressParam retainExpressParam;

    /**
     * 保留字段表达式（retainExpress）：对象型和集合型只保留指定字段的值
     */
    private String retainExpress;

    private List<AviatorExpressParam> objectiveCompareFieldParamList;

    /**
     * 对象型比较字段列表(对象型最大对象、最小对象)
     */
    private List<String> objectiveCompareFieldList;

    /**
     * 排序字段列表（sortFieldList）：类似SQL中的ORDER BY id ASC, user_name DESC，多字段排序。
     * <p>对象型（最大对象、最小对象）、集合型（排序列表）</p>
     */
    private List<FieldOrderParam> collectiveSortFieldList;

    private List<AviatorExpressParam> distinctFieldListParamList;

    /**
     * 去重字段列表（distinctFieldList）：根据多个字段进行去重。集合型（去重列表）
     */
    private List<String> distinctFieldList;

    /**
     * udaf函数中的参数（param）：例如排序列表，指定limit参数，限制条数。key和Java字段名称一致
     */
    private Map<String, Object> param;

}
