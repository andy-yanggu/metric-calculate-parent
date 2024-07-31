package com.yanggu.metric_calculate.config.domain.vo;

import com.yanggu.metric_calculate.config.base.domain.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator函数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorFunctionVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3108156531262807116L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 唯一标识
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
     * 是否内置: 0否, 1是
     */
    private Boolean isBuiltIn;

    /**
     * jar存储id
     */
    private Integer jarStoreId;

    /**
     * 不是内置的聚合函数为外置jar
     */
    private JarStoreVO jarStore;

    /**
     * Aviator函数成员变量列表
     */
    private List<AviatorFunctionFieldVO> aviatorFunctionFieldList;

}
