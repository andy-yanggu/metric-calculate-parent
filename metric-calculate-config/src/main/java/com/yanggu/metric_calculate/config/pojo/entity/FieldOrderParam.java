package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.RelationOneToOne;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 字段排序配置类 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "field_order_param")
public class FieldOrderParam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1261890367844204905L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 表达式id
     */
    private Integer aviatorExpressParamId;

    @RelationOneToOne(selfField = "aviatorExpressParamId", targetField = "id")
    private AviatorExpressParam aviatorExpressParam;

    /**
     * 是否升序, true升序, false降序
     */
    private Boolean isAsc;

}
