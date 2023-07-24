package com.yanggu.metric_calculate.config.pojo.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 宽表
 */
@Data
public class ModelDto {

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 宽表名称
     */
    private String name;

    /**
     * 中文名称
     */
    private String displayName;

    /**
     * 描述
     */
    private String description;

    /**
     * 目录id
     */
    private Integer directoryId;

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

    /**
     * 宽表字段列表
     */
    private List<ModelColumnDto> modelColumnList;

    /**
     * 时间字段列表
     */
    private List<ModelTimeColumnDto> modelTimeColumnList;

    /**
     * 维度字段列表
     */
    private List<ModelDimensionColumnDto> modelDimensionColumnList;

}
