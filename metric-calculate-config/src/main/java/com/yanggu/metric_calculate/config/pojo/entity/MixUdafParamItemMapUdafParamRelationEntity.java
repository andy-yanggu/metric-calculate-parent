package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 混合聚合参数选项-映射聚合参数中间表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "mix_udaf_param_item_map_udaf_param_relation")
public class MixUdafParamItemMapUdafParamRelationEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5197004334239854422L;
    
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 混合聚合参数选项id
     */
    private Integer mixUdafParamItemId;

    /**
     * 映射聚合参数id
     */
    private Integer mapUdafParamId;

}
