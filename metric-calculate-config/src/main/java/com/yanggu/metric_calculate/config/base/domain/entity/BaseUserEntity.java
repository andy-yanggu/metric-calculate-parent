package com.yanggu.metric_calculate.config.base.domain.entity;


import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库实体基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUserEntity extends BaseEntity {

    /**
     * 用户id
     */
    @Column(value = "user_id", tenantId = true)
    private Integer userId;

}
