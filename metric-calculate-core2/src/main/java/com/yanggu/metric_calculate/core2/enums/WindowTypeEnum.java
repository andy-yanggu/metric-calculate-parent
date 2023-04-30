package com.yanggu.metric_calculate.core2.enums;

/**
 * 窗口类型枚举
 */
public enum WindowTypeEnum {

    /**
     * 滚动时间窗口
     */
    TUMBLING_TIME_WINDOW,

    /**
     * 滑动时间窗口
     */
    SLIDING_TIME_WINDOW,

    /**
     * 滑动计数窗口
     */
    SLIDING_COUNT_WINDOW,

    /**
     * 状态窗口
     */
    STATUS_WINDOW,

    /**
     * 全窗口
     */
    GLOBAL_WINDOW,

    /**
     * 会话窗口
     */
    SESSION_WINDOW,

    /**
     * 事件窗口
     */
    EVENT_WINDOW,
    ;

}
