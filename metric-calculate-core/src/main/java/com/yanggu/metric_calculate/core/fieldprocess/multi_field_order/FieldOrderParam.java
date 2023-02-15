package com.yanggu.metric_calculate.core.fieldprocess.multi_field_order;

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
     * 是否升序, true升序, false降序
     * <p>默认升序</p>
     */
    private Boolean asc = true;

}