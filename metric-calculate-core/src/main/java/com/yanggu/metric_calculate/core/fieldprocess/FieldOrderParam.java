package com.yanggu.metric_calculate.core.fieldprocess;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldOrderParam {

    /**
     * 计算表达式
     */
    private String express;

    /**
     * 是否降序, true降序, false升序
     */
    private Boolean desc = true;

}