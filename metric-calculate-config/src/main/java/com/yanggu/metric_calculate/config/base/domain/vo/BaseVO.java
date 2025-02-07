package com.yanggu.metric_calculate.config.base.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * Controller返回值数据基类
 * <p>继承该类即可</p>
 */
@Data
public class BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5814569213714989166L;

    /**
     * 用户id
     */
    @Schema(description = "用户id")
    private Long userId;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID")
    private Long createUserId;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createUser;

    /**
     * 修改人ID
     */
    @Schema(description = "修改人ID")
    private Long updateUserId;

    /**
     * 修改人
     */
    @Schema(description = "修改人")
    private String updateUser;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间")
    private Date updateTime;

    /**
     * 是否删除（0正常, 1删除）
     */
    @Schema(description = "是否删除（0正常, 1删除）")
    private Integer isDelete;

}