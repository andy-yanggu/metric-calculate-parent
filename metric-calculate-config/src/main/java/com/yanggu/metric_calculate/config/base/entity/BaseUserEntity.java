package com.yanggu.metric_calculate.config.base.entity;

import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 数据库实体类基类，适用于用户操作的数据表、增删改查
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUserEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -3534460034686573788L;

    /**
     * 创建人
     */
    @Column("create_user_id")
    private Long createUserId;

    /**
     * 修改人
     */
    @Column("update_user_id")
    private Long updateUserId;

}
