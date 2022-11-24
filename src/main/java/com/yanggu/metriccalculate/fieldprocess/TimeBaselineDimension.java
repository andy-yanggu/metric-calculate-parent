package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import com.yanggu.client.magiccube.enums.TimeUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 时间聚合粒度
 * 时间单位和时间长度
 */
@Data
@Slf4j
@NoArgsConstructor
public class TimeBaselineDimension {

    /**
     * 时间长度
     */
    private Integer length;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    /**
     * 时间字段处理器, 从原始数据中提取出时间戳
     */
    private TimeFieldProcessor timeExtractor;

    public TimeBaselineDimension(Integer length, TimeUnit unit, TimeFieldProcessor timeExtractor) {
        this.length = length;
        this.unit = unit;
        this.timeExtractor = timeExtractor;
    }

    /**
     * 当前数据聚合的时间戳
     * 例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 返回2022-11-21 00:00:00的时间戳
     *
     * @param jsonObject
     * @return
     */
    public Long getCurrentAggregateTimestamp(JSONObject jsonObject) {
        Long timestamp = timeExtractor.process(jsonObject);
        return DateUtil.truncate(new Date(timestamp), unit.getDateField()).getTime();
    }

    /**
     * 包含左区间, 不包含右区间
     * 例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 时间长度为7, 也就是过去7天
     * 时间区间为[2022-11-15 00:00:00, 2022-11-22 00:00:00), 左闭右开
     */
    public Tuple getTimeWindow(JSONObject jsonObject) {
        Long timestamp = timeExtractor.process(jsonObject);
        Long windowEnd = DateUtil.ceiling(new Date(timestamp), unit.getDateField()).getTime() + 1;
        long windowStart = windowEnd - realLength();
        return new Tuple(windowStart, windowEnd);
    }

    /**
     * 偏移的时间戳
     */
    public Long realLength() {
        return this.unit.toMillis(this.length);
    }

}
