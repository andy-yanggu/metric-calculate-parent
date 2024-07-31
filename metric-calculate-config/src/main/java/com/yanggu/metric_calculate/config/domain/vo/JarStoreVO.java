package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.config.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * jar包存储 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JarStoreVO extends BaseVO implements Serializable {

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
