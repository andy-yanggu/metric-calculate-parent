package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 聚合函数参数配置类 实体类。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregateFunctionParamDto implements Serializable {

    private static final long serialVersionUID = -6999604291880819075L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 基本类型聚合函数参数
     */
    private BaseUdafParam baseUdafParam;

    /**
     * 映射类型聚合函数参数
     */
    private MapUdafParam mapUdafParam;

    /**
     * 混合类型聚合函数参数
     */
    private MixUdafParam mixUdafParam;

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
