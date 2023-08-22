package com.yanggu.metric_calculate.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 宽表字段类型
 */
@Getter
@AllArgsConstructor
public enum ModelColumnFieldType {

    /**
     * 真实字段
     */
    REAL,

    /**
     * 虚拟字段
     */
    VIRTUAL,
    ;

}
