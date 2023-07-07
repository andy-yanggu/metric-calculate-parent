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
 * 窗口相关参数 实体类。
 *
 * @author MondayLi
 * @since 2023-07-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "window_param", schema = "metric_calculate_config")
public class WindowParam implements Serializable {

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 派生指标id
     */
    private Integer deriveId;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

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
