package com.yanggu.metric_calculate.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序方式枚举
 */
@Getter
@AllArgsConstructor
public enum SortType {

    ROW_NUMBER,

    RANK,

    DENSE_RANK

}