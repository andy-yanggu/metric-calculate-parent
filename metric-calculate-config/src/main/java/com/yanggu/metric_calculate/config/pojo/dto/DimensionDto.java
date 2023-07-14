package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 维度表 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionDto implements Serializable {

    private static final long serialVersionUID = -2963633798023385948L;

    private Integer id;

    /**
     * 维度名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 备注
     */
    private String description;

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
