package com.yanggu.metric_calculate.core.pojo.udaf_param;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 数值型、集合型、对象型、映射型聚合函数相关参数
 */
@Data
public class BaseUdafParam2 implements Serializable {

    @Serial
    private static final long serialVersionUID = -4563914919213961988L;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 度量字段表达式（metricExpress）：数值型单字段、对象型保留字段、集合型保留字段
     */
    private AviatorExpressParam metricExpressParam;

    /**
     * 多字段度量字段表达式：数值型多字段、集合型去重列表、集合型排序列表、对象型比较字段列表
     */
    private List<AviatorExpressParam> metricExpressParamList;
    /**
     * udaf函数中的参数（param）：例如排序列表，指定limit参数，限制条数。key和Java字段名称一致
     */
    private Map<String, Object> param;

}