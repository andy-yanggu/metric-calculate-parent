package com.yanggu.metric_calculate.core.field_process.time;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;


/**
 * 时间字段处理器, 从输入的明细数据中, 提取出时间戳
 */
@Slf4j
@Getter
@EqualsAndHashCode
public class TimeFieldProcessor implements FieldProcessor<JSONObject, Long> {

    private static final String TIMESTAMP = "TIMESTAMP";

    /**
     * 时间格式
     */
    private final String timeFormat;

    /**
     * 时间字段名称
     */
    private final String timeColumnName;

    public TimeFieldProcessor(String timeFormat, String timeColumnName) {
        this.timeFormat = timeFormat;
        this.timeColumnName = timeColumnName;
    }

    @Override
    public void init() {
        if (StrUtil.isBlank(timeFormat)) {
            throw new RuntimeException("时间格式为空");
        }
        if (StrUtil.isBlank(timeColumnName)) {
            throw new RuntimeException("时间字段的值为空");
        }
    }

    @Override
    public Long process(JSONObject input) {
        Object data = input.get(timeColumnName);
        if (data == null) {
            throw new RuntimeException(StrUtil.format("时间字段没有值, "
                    + "时间字段名: {}, 原始数据: {}", timeColumnName, JSONUtil.toJsonStr(input)));
        }
        if (StrUtil.equals(timeFormat.toUpperCase(), TIMESTAMP)) {
            if (data instanceof Long) {
                return (Long) data;
            } else {
                return Long.parseLong(data.toString());
            }
        } else {
            return DateUtils.parse(data.toString(), timeFormat);
        }
    }

}
