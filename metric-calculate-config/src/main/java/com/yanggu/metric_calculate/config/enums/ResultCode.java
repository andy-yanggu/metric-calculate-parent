package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("200", "成功"),
    NO_LOGIN("401", "未认证"),
    FAIL("500", "服务器异常, 异常信息: {}"),
    MODEL_EXIST("10000", "宽表名称或者中文名已经存在"),
    DERIVE_EXIST("10001", "派生指标名称或者中文名已经存在"),
    MODEL_COLUMN_NAME_ERROR("10002", "宽表字段名异常"),
    MODEL_COLUMN_EMPTY("10003", "宽表字段为空"),
    MODEL_COLUMN_NAME_DUPLICATE("10004", "宽表字段名重复"),
    MODEL_COLUMN_DISPLAY_NAME_DUPLICATE("10005", "宽表字段中文名重复"),
    AVIATOR_EXPRESS_CHECK_ERROR("10006", "Aviator表达式校验失败"),
    AVIATOR_EXPRESS_PARAM_MODEL_COLUMN_ERROR("10007", "Aviator表达式依赖宽表字段错误"),
    MODEL_ID_ERROR("10008", "宽表id: {}有误"),
    DERIVE_ID_ERROR("10009", "派生指标id: {}有误"),
    MODEL_TIME_COLUMN_NULL("10010", "宽表时间字段名为空"),
    AGGREGATE_FUNCTION_EXIST("10011", "聚合函数已经存在"),
    MODEL_DIMENSION_COLUMN_NULL("10012", "宽表维度字段名为空"),
    MIX_UDAF_PARAM_NAME_ERROR("10013", "混合聚合参数名称异常"),
    AGGREGATE_FUNCTION_CLASS_NOT_HAVE_ANNOTATION("10014", "{}不包含AggregateFunctionAnnotation注解"),
    AGGREGATE_FUNCTION_CLASS_TYPE_ERROR("10015", "{}不包含任何类型注解"),
    AVIATOR_FUNCTION_CLASS_NOT_HAVE_ANNOTATION("10016", "{}不包含AviatorFunctionAnnotation注解"),
    AVIATOR_FUNCTION_HAS_INSTANCE("10017", "Aviator函数下有实例, 不能删除"),
    JAR_STORE_ID_NULL("10018", "jar存储id不能为null"),
    BUILT_IN_AGGREGATE_FUNCTION_NOT_HAVE("10019", "内置的聚合函数不包含: {}"),
    JAR_NOT_HAVE_CLASS("10020", "jar包中不存在符合条件的类"),
    AGGREGATE_FUNCTION_NAME_NOT_UPDATE("10021", "聚合函数名称不允许修改"),
    AGGREGATE_FUNCTION_TYPE_ERROR("10022", "聚合函数类型错误"),
    AGGREGATE_FUNCTION_ID_ERROR("10023", "聚合函数id错误: {}"),
    BASE_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION("10024", "基本聚合参数引用聚合函数, 不能删除"),
    MAP_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION("10025", "映射聚合参数引用聚合函数, 不能删除"),
    MIX_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION("10026", "混合聚合参数引用聚合函数, 不能删除"),
    AVIATOR_FUNCTION_EXIST("10027", "Aviator函数已经存在"),
    DIMENSION_EXIST("10028", "维度名称或者中文名已经存在"),
    DIMENSION_BINDING_MODEL_COLUMN_NOT_DELETE("10029", "维度字段绑定了宽表字段, 不能删除"),
    MODEL_HAS_DERIVE_NOT_DELETE("10030", "宽表下有派生指标不能删除"),
    AVIATOR_EXPRESS_PARAM_USE_AVIATOR_FUNCTION_INSTANCE_NOT_DELETE("10031", "Aviator表达式使用该实例不能删除"),
    MODEL_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED("10032", "当宽表字段被派生指标使用时不允许修改或者删除"),
    MODEL_TIME_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED("10033", "当宽表时间字段被派生指标使用时不允许修改或者删除"),
    MODEL_DIMENSION_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED("10034", "当宽表维度字段被派生指标使用时不允许修改或者删除"),
    MODEL_COLUMN_NOT_DELETE_WHEN_DERIVE_USED("10035", "当宽表字段被派生指标使用时不允许删除"),
    MODEL_COLUMN_NAME_NOT_UPDATE_WHEN_DERIVE_USED("10036", "当宽表字段被派生指标使用时字段名不允许修改"),
    MODEL_COLUMN_DATA_TYPE_NOT_UPDATE_WHEN_DERIVE_USED("10037", "当宽表字段被派生指标使用时数据类型不允许修改"),
    MODEL_COLUMN_FIELD_TYPE_NOT_UPDATE_WHEN_DERIVE_USED("10038", "当宽表字段被派生指标使用时类型不允许修改"),
    AVIATOR_FUNCTION_ID_ERROR("10039", "Aviator函数id错误: {}"),
    AVIATOR_FUNCTION_PARAM_ERROR("10040", "提交的Aviator函数参数存在不存在的参数: {}"),
    ATOM_EXIST("10041", "原子指标已经存在"),
    ;

    private final String code;

    private final String message;

}
