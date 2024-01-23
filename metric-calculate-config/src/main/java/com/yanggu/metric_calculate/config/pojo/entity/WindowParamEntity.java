package com.yanggu.metric_calculate.config.pojo.entity;

import com.mybatisflex.annotation.*;
import com.yanggu.metric_calculate.config.base.entity.BaseUserEntity;
import com.yanggu.metric_calculate.config.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 窗口相关参数 实体类。
 */
@Data
@Table(value = "window_param")
@EqualsAndHashCode(callSuper = true)
public class WindowParamEntity extends BaseUserEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7153009252948514786L;

    /**
     * 主键自增
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 窗口类型
     */
    private WindowTypeEnum windowType;

    /**
     * 宽表时间字段id
     */
    private Integer modelTimeColumnId;

    /**
     * 时间字段
     */
    @RelationManyToOne(selfField = "modelTimeColumnId", targetField = "id")
    private ModelTimeColumnEntity modelTimeColumn;

    /**
     * 时间周期
     */
    private Integer duration;

    /**
     * 时间单位
     */
    private TimeUnitEnum timeUnit;

    /**
     * 滑动计数窗口大小
     */
    private Integer slidingCount;

    /**
     * 状态窗口表达式列表
     */
    @RelationOneToMany(
            joinTable = "window_param_status_express_param_list_relation",
            selfField = "id", joinSelfColumn = "window_param_id",
            targetField = "id", joinTargetColumn = "aviator_express_param_id"
    )
    private List<AviatorExpressParamEntity> statusExpressParamList;

    /**
     * 事件模式数据
     */
    @RelationOneToMany(selfField = "id", targetField = "windowParamId", orderBy = "sort")
    private List<NodePatternEntity> nodePatternList;

    /**
     * 会话窗口间隔（时间单位毫秒值）
     */
    private Long gapTimeMillis;

}
