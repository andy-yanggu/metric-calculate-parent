package com.yanggu.metric_calculate.config.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 维度字段选项 实体类。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "dimension_column_item")
public class DimensionColumnItem implements Serializable {

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
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
    @Column(isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
