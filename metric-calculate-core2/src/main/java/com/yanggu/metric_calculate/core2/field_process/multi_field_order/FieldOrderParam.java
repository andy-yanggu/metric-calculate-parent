package com.yanggu.metric_calculate.core2.field_process.multi_field_order;

import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 字段排序配置类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldOrderParam implements Serializable {

    private static final long serialVersionUID = 7805565551770455038L;

    private AviatorExpressParam aviatorExpressParam;

    /**
     * 是否升序, true升序, false降序
     * <p>默认升序</p>
     */
    private Boolean isAsc = true;

}