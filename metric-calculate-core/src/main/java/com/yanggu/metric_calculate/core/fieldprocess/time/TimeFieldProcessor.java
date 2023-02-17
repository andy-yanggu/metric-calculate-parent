package com.yanggu.metric_calculate.core.fieldprocess.time;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 时间字段处理器, 从输入的明细数据中, 提取出时间戳
 */
@Data
@Slf4j
@NoArgsConstructor
public class TimeFieldProcessor<T> implements FieldProcessor<T, Long> {

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
    public Long process(T input) {
        Object data = JSONUtil.parseObj(input).get(timeColumnName);
        if (data == null) {
            throw new RuntimeException(StrUtil.format("时间字段没有值, "
                    + "时间字段名: {}, 原始数据: {}", timeColumnName, JSONUtil.toJsonStr(input)));
        }
        String dateStr = data.toString();
        if (StrUtil.equals(timeFormat.toUpperCase(), TIMESTAMP)) {
            return Long.parseLong(dateStr);
        } else {
            return DateUtil.parse(dateStr, timeFormat).getTime();
        }
    }

}
