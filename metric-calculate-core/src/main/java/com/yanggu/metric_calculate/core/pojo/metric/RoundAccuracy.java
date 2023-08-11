package com.yanggu.metric_calculate.core.pojo.metric;

import com.yanggu.metric_calculate.core.enums.AccuracyEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 精度信息
 */
@Data
@Accessors(chain = true)
public class RoundAccuracy implements Serializable {

    private static final long serialVersionUID = 4279405219240970430L;

    /**
     * 是否使用精度, true使用, false不使用
     */
    private Boolean useAccuracy;

    /**
     * 保留小数位数, 0保留整数
     */
    private Integer length;

    /**
     * 1四舍五入、2向上保留
     */
    private AccuracyEnum type;

}
