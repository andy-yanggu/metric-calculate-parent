package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * Aviator函数实例 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "aviator_function_instance")
public class AviatorFunctionInstance extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -566558748751401811L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * Aviator函数id
     */
    private Integer aviatorFunctionId;

    /**
     * Aviator函数参数的JSON数据
     */
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> param;

    /**
     * 对应的AggregateFunction
     */
    @RelationManyToOne(selfField = "aviatorFunctionId", targetField = "id")
    private AviatorFunction aviatorFunction;

}
