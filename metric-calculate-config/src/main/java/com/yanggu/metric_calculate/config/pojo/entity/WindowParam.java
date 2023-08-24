package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 窗口相关参数 实体类。
 */
@Data
@Table(value = "window_param")
@EqualsAndHashCode(callSuper = true)
public class WindowParam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7153009252948514786L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

}
