package com.yanggu.metric_calculate.config.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 实体类。
 */
@Data
public class MixUdafParamItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3433426491472772510L;

    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 混合聚合函数参数id
     */
    private Integer mixUdafParamId;

    private BaseUdafParamDTO baseUdafParam;

    private MapUdafParamDTO mapUdafParam;

    /**
     * 索引
     */
    private Integer sort;

}