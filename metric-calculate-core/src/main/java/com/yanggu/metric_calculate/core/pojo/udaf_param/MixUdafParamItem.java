package com.yanggu.metric_calculate.core.pojo.udaf_param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MixUdafParamItem implements Serializable {

    private static final long serialVersionUID = -9143234002581892763L;

    /**
     * 名称
     */
    private String name;

    /**
     * 基本聚合函数参数
     */
    private BaseUdafParam baseUdafParam;

    /**
     * 索引
     */
    private Integer sort;

}
