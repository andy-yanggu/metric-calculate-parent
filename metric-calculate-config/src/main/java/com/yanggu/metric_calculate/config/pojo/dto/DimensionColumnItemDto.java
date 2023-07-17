package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 维度字段选项 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DimensionColumnItemDto implements Serializable {

    private static final long serialVersionUID = 5533781924640114353L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 维度字段id
     */
    private Integer dimensionColumnId;

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
