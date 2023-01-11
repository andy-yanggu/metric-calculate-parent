package com.yanggu.metric_calculate.core.pojo;

import lombok.Data;

/**
 * 全局指标
 */
@Data
public class Global {

    /**
     * 全局指标id
     */
    private Long id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 指标存储相关信息
     */
    private Store store;

}
