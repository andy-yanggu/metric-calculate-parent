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
 * 映射聚合参数，value的聚合函数参数。只能是数值型、集合型、对象型中间表 实体类。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "map_udaf_param_value_agg_relation")
public class MapUdafParamValueAggRelation implements Serializable {

    
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 映射聚合函数参数id
     */
    private Integer mapUdafParamId;

    /**
     * 基本聚合函数参数id
     */
    private Integer baseUdafParamId;

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
