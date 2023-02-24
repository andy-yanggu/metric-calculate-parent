package com.yanggu.metric_calculate.core.field_process.multi_field_order;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FieldOrder {

    /**
     * 排序字段的值
     */
    private Object result;

    /**
     * 是否升序, true升序, false降序
     * <p>默认升序</p>
     */
    private Boolean asc = true;

}
