package com.yanggu.metric_calculate.core2.field_process.time;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core2.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 时间字段处理器, 从输入的明细数据中, 提取出时间戳
 */
@Data
@Slf4j
@NoArgsConstructor
public class TimeFieldProcessor implements FieldProcessor<JSONObject, Long> {

    private static final String TIMESTAMP = "TIMESTAMP";

    /**
     * 时间格式
     */
    private String timeFormat;

    /**
     * 时间字段名称
     */
    private String timeColumnName;

    public TimeFieldProcessor(String timeFormat, String timeColumnName) {
        this.timeFormat = timeFormat;
        this.timeColumnName = timeColumnName;
    }

    @Override
    public void init() {
        if (StrUtil.isBlank(timeColumnName)) {
            throw new RuntimeException("时间字段的值为空");
        }
        if (StrUtil.isBlank(timeFormat)) {
            throw new RuntimeException("时间格式为空");
        }
    }

    @Override
    public Long process(JSONObject input) {
        Object data = input.get(timeColumnName);
        if (data == null) {
            throw new RuntimeException(StrUtil.format("时间字段没有值, "
                    + "时间字段名: {}, 原始数据: {}", timeColumnName, JSONUtil.toJsonStr(input)));
        }
        if (StrUtil.equals(timeFormat, TIMESTAMP)) {
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
