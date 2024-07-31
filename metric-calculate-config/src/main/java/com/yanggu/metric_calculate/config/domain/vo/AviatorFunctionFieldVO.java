package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.config.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * Aviator函数字段模板 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionFieldVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1248441229349580401L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * Aviator函数id
     */
    private Integer aviatorFunctionId;

    /**
     * 索引
     */
    private Integer sort;

}