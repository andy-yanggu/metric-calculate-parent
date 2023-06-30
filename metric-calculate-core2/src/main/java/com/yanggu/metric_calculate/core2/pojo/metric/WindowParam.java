package com.yanggu.metric_calculate.core2.pojo.metric;

import com.yanggu.metric_calculate.core2.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 窗口相关参数
 */
@Data
public class WindowParam implements Serializable {

    private static final long serialVersionUID = -4029143865929024435L;

    /**
     * 窗口类型
     */
    private WindowTypeEnum windowType;

    /**
     * 时间字段
     */
    private TimeColumn timeColumn;

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
    private Integer limit;

    private List<AviatorExpressParam> statusExpressParamList;

    /**
     * 状态窗口表达式列表
     */
    private List<String> statusExpressList;

    /**
     * 事件模式数据
     */
    private List<NodePattern> nodePatternList;

    /**
     * 会话窗口间隔
     */
    private Long gapTimeMillis;

}
