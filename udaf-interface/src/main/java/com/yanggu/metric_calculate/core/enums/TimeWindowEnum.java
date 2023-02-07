package com.yanggu.metric_calculate.core.enums;

public enum TimeWindowEnum {

    /**
     * 时间切片
     * <p>适用于数值型和排序、去重的聚合逻辑</p>
     * <p>不适用于不能乱序的聚合逻辑, 例如上一次, 最近5次</p>
     * <p>例如最近2小时, 切割成1小时的小区间, 分成[0, 1)、[1, 2)等</p>
     * <p>例如数据的时间是13:01, 将数据聚合到[13, 14)这个小区间</p>
     * <p>然后查询[12, 14)和[13, 15)这两个区间的数据</p>
     */
    TIME_SPLIT_WINDOW,

    /**
     * 滑动时间
     * <p>适用于不能乱序的聚合逻辑</p>
     * <p>不使用时间切片的方式, 而是先划分时间区间, 然后在各自的区间内聚合计算</p>
     *
     */
    TIME_SLIDING_WINDOW,

    /**
     * CEP事件模式
     */
    PATTERN,

    ;

}
