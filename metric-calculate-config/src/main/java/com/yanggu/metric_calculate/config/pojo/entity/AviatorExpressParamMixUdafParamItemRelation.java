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
 * Aviator表达式和混合类型参数中间表 实体类。
 *
 * @author MondayLi
 * @since 2023-08-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "aviator_express_param_mix_udaf_param_item_relation")
public class AviatorExpressParamMixUdafParamItemRelation implements Serializable {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

    /**
     * 混合类型参数id
     */
    private Integer mixUdafParamItemId;

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

}
