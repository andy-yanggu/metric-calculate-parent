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
 * 宽表字段 实体类。
 *
 * @author 杨顾
 * @since 2023-07-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "model_column", schema = "metric_calculate_config")
public class ModelColumn implements Serializable {

    
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 中文名
     */
    private String displayName;

    /**
     * 数据类型(STRING、BOOLEAN、LONG、DOUBLE)
     */
    private String dataType;

    /**
     * 描述
     */
    private String description;

    /**
     * 字段类型(REAL、VIRTUAL)
     */
    private String fieldType;

    /**
     * 宽表id
     */
    private Integer modelId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 索引
     */
    private Integer index;

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
