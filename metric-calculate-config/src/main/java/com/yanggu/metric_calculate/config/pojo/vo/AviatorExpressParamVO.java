package com.yanggu.metric_calculate.config.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Aviator表达式配置 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AviatorExpressParamVO extends BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 598313027699065442L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 表达式
     */
    private String express;

    /**
     * 依赖的宽表字段
     */
    private List<ModelColumnVO> modelColumnList;

    /**
     * 依赖的混合型实例
     */
    private List<MixUdafParamItemVO> mixUdafParamItemList;

    /**
     * 使用的Aviator函数实例列表
     */
    private List<AviatorFunctionInstanceVO> aviatorFunctionInstanceList;

}
