package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * jar包存储 实体类。
 */
@Data
public class JarStoreDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -88858660037080529L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * jar包url
     */
    private String jarUrl;

}
