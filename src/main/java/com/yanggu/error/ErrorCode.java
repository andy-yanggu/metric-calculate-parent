package com.yanggu.error;

public enum ErrorCode {
    RULE_MARK_ERROR(500, 110001, "服务内部错误", "服务内部错误"),
    METRIC_NOT_EXIST(500, 110002, "指标不存在", "指标不存在"),
    RULE_RELATION_EMPTY(500, 110003, "规则未关联指标", "规则未关联指标"),
    RULE_NOT_EXIST(500, 110004, "规则不存在", "规则不存在"),
    MARKETING_EVENT_NOT_EXIST(500, 110005, "营销活动不存在", "营销活动不存在"),
    CREATE_MARKETING_EVENT_ERROR(500, 110006, "创建营销活动失败", "创建营销活动 [%s] 失败：%s"),
    UPDATE_MARKETING_EVENT_ERROR(500, 110007, "更新营销活动失败", "更新营销活动 [%s] 失败：%s"),
    DELETE_MARKETING_EVENT_ERROR(500, 110008, "删除营销活动失败", "删除营销活动失败：%s"),
    GENERATE_RULE_EXPRESS_ERROR(500, 110009, "生成规则表达式失败", "%s"),
    CREATE_RULE_ERROR(500, 110010, "创建规则失败", "创建规则 [%s] 失败：%s"),
    UPDATE_RULE_ERROR(500, 110011, "更新规则失败", "更新规则 [%s] 失败：%s"),
    DELETE_RULE_ERROR(500, 110012, "删除规则失败", "删除规则失败：%s"),
    ENABLED_RULE_EMPTY(500, 110013, "该活动下无启用规则，上线失败", "该活动下无启用规则，上线失败"),
    MARKETING_START_END_TIME_EMPTY(500, 110014, "活动开始时间及结束时间不能为空", "活动开始时间及结束时间不能为空"),
    START_TIME_AFTER_END_TIME(500, 110015, "活动开始时间不能晚于结束时间", "活动开始时间不能晚于结束时间"),
    NAME_EMPTY(500, 110016, "名称不能为空", "名称不能为空"),
    TRIGGER_LIMIT_DUPLICATE(500, 110017, "同一活动的多个触达限制不能设置相同周期", "同一活动的多个触达限制不能设置相同周期"),
    IMPORT_DATA_FORMAT_ERROR(500, 110018, "导入文件解析错误", "导入文件解析错误"),
    TRIGGER_TIMES_LARGER_THAN_ZERO(500, 110019, "触达次数需大于 0", "触达次数需大于 0"),
    TRIGGER_PERIOD_ERROR(500, 110020, "触达限制周期设置错误", "%s触达次数不能大于%s触达次数");

    private final Integer httpStatus;
    private final Integer code;
    private final String info;
    private final String format;

    ErrorCode(Integer httpStatus, Integer code, String info, String format) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.info = info;
        this.format = format;
    }

    public ErrorCodeException formatAsException(Object... args) {
        String errorMessage;
        if (args.length == 0 && format == null) {
            errorMessage = null;
        } else if (format == null) {
            errorMessage =
                    String.format(
                            "No formation for error type %s", this.getClass().getCanonicalName());
        } else {
            errorMessage = String.format(format, args);
        }
        return new ErrorCodeException(this, errorMessage);
    }

    public ErrorCodeException asException() {
        return new ErrorCodeException(this);
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getFormat() {
        return format;
    }
}
