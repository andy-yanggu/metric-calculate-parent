package com.yanggu.metric_calculate.config.pojo.entity;


import com.mybatisflex.annotation.Column;
import lombok.Data;

import java.util.Date;

/**
 * 数据库实体基类
 */
@Data
public class BaseEntity {

    /**
     * 用户id
     */
    @Column(value = "user_id", tenantId = true)
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    @Column(onInsertValue = "0", isLogicDelete = true)
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(onInsertValue = "CURRENT_TIMESTAMP", onUpdateValue = "CURRENT_TIMESTAMP")
    private Date updateTime;

}
