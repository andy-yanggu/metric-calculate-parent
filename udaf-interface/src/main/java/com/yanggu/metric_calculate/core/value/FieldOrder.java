package com.yanggu.metric_calculate.core.value;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldOrder {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 是否降序, true降序, false升序
     */
    private Boolean desc = true;

}