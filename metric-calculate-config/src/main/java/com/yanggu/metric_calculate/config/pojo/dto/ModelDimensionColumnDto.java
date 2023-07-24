package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 维度字段 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelDimensionColumnDto implements Serializable {

    private static final long serialVersionUID = 3720677788471627294L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 宽表字段id
     */
    private Integer modelColumnId;

    /**
     * 宽表字段名称
     */
    private String modelColumnName;

    /**
     * 宽表字段
     */
    private ModelColumn modelColumn;

    /**
     * 维度id
     */
    private Integer dimensionId;

    /**
     * 维度
     */
    private DimensionDto dimension;

    /**
     * 索引
     */
    private Integer sort;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
