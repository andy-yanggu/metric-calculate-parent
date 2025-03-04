package com.yanggu.metric_calculate.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序方式枚举
 * <p>类似窗口函数的rank函数，三种排序语义</p>
 */
@Getter
@AllArgsConstructor
public enum SortType {

    ROW_NUMBER,

    RANK,

    DENSE_RANK

}