package com.yanggu.metric_calculate.core.field_process.time;

import com.yanggu.metric_calculate.core.field_process.FieldProcessor;
import com.yanggu.metric_calculate.core.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONUtil;

import java.util.Map;


/**
 * 时间字段处理器, 从输入的明细数据中, 提取出时间戳
 *
 * @param timeFormat     时间格式
 * @param timeColumnName 时间字段名称
 */
@Slf4j
public record TimeFieldProcessor(String timeFormat,
                                 String timeColumnName)
        implements FieldProcessor<Map<String, Object>, Long> {

    private static final String TIMESTAMP = "TIMESTAMP";

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
    public Long process(Map<String, Object> input) {
        Object data = input.get(timeColumnName);
        if (data == null) {
            throw new RuntimeException(StrUtil.format("时间字段没有值, "
                    + "时间字段名: {}, 原始数据: {}", timeColumnName, JSONUtil.toJsonStr(input)));
        }
        if (StrUtil.equals(timeFormat, TIMESTAMP)) {
            if (data instanceof Long) {
                return ((Long) data);
            } else if (data instanceof Number tempNumber) {
                return tempNumber.longValue();
            } else {
                return Long.parseLong(data.toString());
            }
        } else {
            return DateUtils.parse(data.toString(), timeFormat);
        }
    }

}
