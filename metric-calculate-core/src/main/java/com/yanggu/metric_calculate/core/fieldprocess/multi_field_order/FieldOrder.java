package com.yanggu.metric_calculate.core.fieldprocess.multi_field_order;

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
     * 是否降序, true降序, false升序
     */
    private Boolean desc;

}
