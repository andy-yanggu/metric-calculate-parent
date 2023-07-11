package com.yanggu.metric_calculate.config.pojo.dto;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Aviator表达式配置 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AviatorExpressParamDto implements Serializable {

    private static final long serialVersionUID = 598313027699065442L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 表达式
     */
    private String express;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除(缺省为0,即未删除)
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
