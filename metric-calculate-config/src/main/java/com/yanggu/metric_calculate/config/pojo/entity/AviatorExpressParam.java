package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aviator表达式配置 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "aviator_express_param")
public class AviatorExpressParam implements Serializable {

    private static final long serialVersionUID = -393023162832756338L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 表达式
     */
    private String express;

    /**
     * 用户id
     */
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

    /**
     * 使用的Aviator函数实例列表
     */
    @RelationManyToMany(
            joinTable = "aviator_express_param_aviator_function_instance_relation",
            selfField = "id", joinSelfColumn = "aviator_express_param_id",
            targetField = "id", joinTargetColumn = "aviator_function_instance_id"
    )
    private List<AviatorFunctionInstance> aviatorFunctionInstanceList;

    /**
     * 依赖的宽表字段
     */
    @RelationManyToMany(
            joinTable = "aviator_express_param_model_column_relation",
            selfField = "id", joinSelfColumn = "aviator_express_param_id",
            targetField = "id", joinTargetColumn = "model_column_id"
    )
    private List<ModelColumn> modelColumnList;

}
