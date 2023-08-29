package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * jar包存储 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "jar_store")
public class JarStore extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2963846824216193064L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * jar包url
     */
    private String jarUrl;

}
