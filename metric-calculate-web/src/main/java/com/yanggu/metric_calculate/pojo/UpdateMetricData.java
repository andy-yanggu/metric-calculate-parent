package com.yanggu.metric_calculate.pojo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.yanggu.metric_calculate.core2.window.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@ApiModel("指标数据")
public class UpdateMetricData<IN, ACC, OUT> {

    @NotNull(message = "数据明细宽表id不能为空")
    @ApiModelProperty(value = "数据明细宽表id", required = true)
    private Long tableId;

    @NotNull(message = "派生指标id不能为空")
    @ApiModelProperty(value = "派生指标id", required = true)
    private Long deriveId;

    @NotEmpty(message = "维度值不能为空")
    @ApiModelProperty(value = "维度值", required = true)
    private LinkedHashMap<String, Object> dimensionMap;

    /**
     * JsonTypeInfo注解指定反序列化抽象类具体的子类
     */
    @NotNull(message = "指标数据不能为空")
    @ApiModelProperty(value = "指标数据", required = true)
    @JsonTypeInfo(use = NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = GlobalWindow.class, name = "GLOBAL_WINDOW"),
            @JsonSubTypes.Type(value = PatternWindow.class, name = "EVENT_WINDOW"),
            @JsonSubTypes.Type(value = SlidingCountWindow.class, name = "SLIDING_COUNT_WINDOW"),
            @JsonSubTypes.Type(value = SlidingTimeWindow.class, name = "SLIDING_TIME_WINDOW"),
            @JsonSubTypes.Type(value = StatusWindow.class, name = "STATUS_WINDOW"),
            @JsonSubTypes.Type(value = TumblingTimeWindow.class, name = "TUMBLING_TIME_WINDOW"),
    })
    private AbstractWindow<IN, ACC, OUT> window;

}
