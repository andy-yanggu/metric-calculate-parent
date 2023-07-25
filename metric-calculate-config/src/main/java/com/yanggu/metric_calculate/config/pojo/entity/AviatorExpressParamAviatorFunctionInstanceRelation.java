package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Aviator函数和Aviator函数实例中间表 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("aviator_express_param_aviator_function_instance_relation")
public class AviatorExpressParamAviatorFunctionInstanceRelation {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * Aviator表达式id
     */
    @Column("aviator_express_param_id")
    private Integer aviatorExpressParamId;

    /**
     * Aviator函数实例id
     */
    @Column("aviator_function_instance_id")
    private Integer aviatorFunctionInstanceId;

    /**
     * 用户id
     */
    @Column("user_id")
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
