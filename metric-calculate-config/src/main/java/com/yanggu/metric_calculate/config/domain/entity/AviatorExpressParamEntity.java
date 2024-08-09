package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationManyToMany;
import com.mybatisflex.annotation.RelationOneToMany;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式配置 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aviator_express_param")
public class AviatorExpressParamEntity extends BaseUserEntity implements Serializable {

    @Serial
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
     * 依赖的宽表字段
     */
    @RelationManyToMany(
            joinTable = "aviator_express_param_model_column_relation",
            selfField = "id", joinSelfColumn = "aviator_express_param_id",
            targetField = "id", joinTargetColumn = "model_column_id"
    )
    private List<ModelColumnEntity> modelColumnList;

    /**
     * 依赖的混合型实例
     */
    @RelationOneToMany(
            joinTable = "aviator_express_param_mix_udaf_param_item_relation",
            selfField = "id", joinSelfColumn = "aviator_express_param_id",
            targetField = "id", joinTargetColumn = "mix_udaf_param_item_id"
    )
    private List<MixUdafParamItemEntity> mixUdafParamItemList;

    /**
     * 使用的Aviator函数实例列表
     */
    @RelationManyToMany(
            joinTable = "aviator_express_param_aviator_function_instance_relation",
            selfField = "id", joinSelfColumn = "aviator_express_param_id",
            targetField = "id", joinTargetColumn = "aviator_function_instance_id"
    )
    private List<AviatorFunctionInstanceEntity> aviatorFunctionInstanceList;

}
