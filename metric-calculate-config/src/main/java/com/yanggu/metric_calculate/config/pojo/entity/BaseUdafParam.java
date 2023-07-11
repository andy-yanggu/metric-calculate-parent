package com.yanggu.metric_calculate.config.pojo.entity;

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
 * 数值型、集合型、对象型聚合函数相关参数 实体类。
 *
 * @author MondayLi
 * @since 2023-07-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "base_udaf_param")
public class BaseUdafParam implements Serializable {

    
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 聚合函数id
     */
    private Integer aggregateFunctionId;

    /**
     * Aviator函数参数的JSON数据
     */
    private String param;

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
