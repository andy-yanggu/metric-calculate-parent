package com.yanggu.metric_calculate.config.domain.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.yanggu.metric_calculate.config.base.domain.entity.BaseUserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 窗口参数状态窗口表达式列表关系表 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table(value = "window_param_status_express_param_list_relation")
public class WindowParamStatusExpressParamListRelationEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 8764702669717667059L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 窗口参数id
     */
    private Integer windowParamId;

    /**
     * Aviator表达式id
     */
    private Integer aviatorExpressParamId;

}
