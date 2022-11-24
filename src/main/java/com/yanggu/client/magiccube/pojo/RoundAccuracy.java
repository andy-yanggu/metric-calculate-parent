package com.yanggu.client.magiccube.pojo;

import com.yanggu.client.magiccube.enums.AccuracyEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 精度信息
 */
@Data
@Accessors(chain = true)
public class RoundAccuracy {

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
