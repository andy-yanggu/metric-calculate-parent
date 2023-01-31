package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 时间聚合粒度
 * 时间单位和时间长度
 */
@Data
@Slf4j
@Accessors(chain = true)
@NoArgsConstructor
public class TimeBaselineDimension implements Serializable {

    /**
     * 时间长度
     */
    private Integer length;

    /**
     * 时间单位
     */
    private TimeUnit unit;

    public TimeBaselineDimension(Integer length, TimeUnit unit) {
        this.length = length;
        this.unit = unit;
    }

    /**
     * 当前数据聚合的时间戳
     * 例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 返回2022-11-21 00:00:00的时间戳
     *
     * @return
     */
    public Long getCurrentAggregateTimestamp(Long timestamp) {
        return DateUtil.truncate(new Date(timestamp), unit.getDateField()).getTime();
    }

    /**
     * 包含左区间, 不包含右区间
     * <p>例如数据时间为2022-11-21 14:00:00, 时间单位为DAY, 时间长度为7, 也就是过去7天</p>
     * <p>时间区间为[2022-11-15 00:00:00, 2022-11-22 00:00:00), 左闭右开</p>
     * <p>如果时间聚合粒度不是1, 就是滑动窗口, 窗口滑动步长就是1个时间单位</p>
     */
    public List<TimeWindow> getTimeWindow(Long timestamp) {
        Long windowEnd = DateUtil.ceiling(new Date(timestamp), unit.getDateField()).getTime() + 1;
        Long windowSize = realLength();
        Long slidingSize = this.unit.getMillis();
        long windowStart = windowEnd - windowSize;
        List<TimeWindow> windows = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            windows.add(new TimeWindow(windowStart + i * slidingSize, windowStart + i * slidingSize + windowSize));
        }
        if (log.isDebugEnabled()) {
            for (TimeWindow window : windows) {
                log.debug("窗口开始时间: {}, 结束时间: {}", DateUtil.formatDateTime(new Date(window.getWindowStart())),
                        DateUtil.formatDateTime(new Date(window.getWindowEnd())));
            }
        }
        return windows;
    }

    /**
     * 偏移的时间戳
     */
    public Long realLength() {
        return this.unit.toMillis(this.length);
    }

}
