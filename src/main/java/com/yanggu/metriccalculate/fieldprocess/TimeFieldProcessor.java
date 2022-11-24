package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 时间字段处理器, 从输入的明细数据中, 提取出时间戳
 */
@Data
@Slf4j
@NoArgsConstructor
public class TimeFieldProcessor implements FieldExtractProcessor<JSONObject, Long> {

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
    public Long process(JSONObject input) {
        Object data = input.get(timeColumnName);
        if (data == null) {
            throw new RuntimeException(StrUtil.format("时间字段没有值, "
                    + "时间字段名: {}, 原始数据: {}", timeColumnName, JSONUtil.toJsonStr(input)));
        }
        String dateStr = data.toString();
        if (StrUtil.equalsIgnoreCase(timeFormat, "TIMESTAMP")) {
            return Long.parseLong(dateStr);
        } else {
            return DateUtil.parse(dateStr, timeFormat).getTime();
        }
    }

}
