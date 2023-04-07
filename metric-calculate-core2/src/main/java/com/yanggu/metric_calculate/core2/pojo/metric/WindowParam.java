package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 窗口相关参数
 */
@Data
public class WindowParam {

    /**
     * 窗口类型
     */
    private WindowTypeEnum windowType;

    /**
     * 时间周期
     */
    private Integer duration;

    /**
     * 时间单位
     */
    private TimeUnitEnum timeUnit;

    /**
     * 滑动计数窗口大小
     */
    private Integer limit;

    /**
     * 状态窗口表达式列表
     */
    private List<String> statusExpressList;

}
