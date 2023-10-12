package com.yanggu.metric_calculate.config.pojo.dto;

import com.yanggu.metric_calculate.config.enums.TimeUnitEnum;
import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelExport;
import com.yanggu.metric_calculate.config.util.excel.annotation.ExcelImport;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 窗口相关参数 实体类。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WindowParamDto extends BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 8390869746036594699L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 窗口类型
     */
    @ExcelExport(name = "窗口类型", sort = 1)
    @ExcelImport(name = "窗口类型")
    private WindowTypeEnum windowType;

    /**
     * 宽表时间字段id
     */
    private Integer modelTimeColumnId;

    /**
     * 时间字段
     */
    private ModelTimeColumnDto modelTimeColumn;

    /**
     * 时间周期
     */
    @ExcelExport(name = "时间周期", sort = 3)
    @ExcelImport(name = "时间周期")
    private Integer duration;

    /**
     * 时间单位
     */
    @ExcelExport(name = "时间单位", sort = 2)
    @ExcelImport(name = "时间单位")
    private TimeUnitEnum timeUnit;

    /**
     * 滑动计数窗口大小
     */
    @ExcelExport(name = "滑动计数窗口大小", sort = 4)
    @ExcelImport(name = "滑动计数窗口大小")
    private Integer slidingCount;

    /**
     * 状态窗口表达式列表
     */
    private List<AviatorExpressParamDto> statusExpressParamList;

    /**
     * 事件模式数据
     */
    private List<NodePatternDto> nodePatternList;

    /**
     * 会话窗口间隔（时间单位毫秒值）
     */
    @ExcelExport(name = "会话窗口间隔", sort = 5)
    @ExcelImport(name = "会话窗口间隔")
    private Long gapTimeMillis;

}