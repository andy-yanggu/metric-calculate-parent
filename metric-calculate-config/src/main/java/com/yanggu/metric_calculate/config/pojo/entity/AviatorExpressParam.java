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
 * Aviator表达式配置 实体类。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "aviator_express_param")
public class AviatorExpressParam implements Serializable {

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
