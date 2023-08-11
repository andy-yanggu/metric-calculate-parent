package com.yanggu.metric_calculate.web.pojo.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.yanggu.metric_calculate.core2.window.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashMap;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@Schema(description = "指标数据")
public class UpdateMetricData<IN, ACC, OUT> {

    @NotNull(message = "数据明细宽表id不能为空")
    @Schema(description = "数据明细宽表id", required = true)
    private Long tableId;

    @NotNull(message = "派生指标id不能为空")
    @Schema(description = "派生指标id", required = true)
    private Long deriveId;

    @NotEmpty(message = "维度值不能为空")
    @Schema(description = "维度值", required = true)
    private LinkedHashMap<String, Object> dimensionMap;

    /**
     * JsonTypeInfo注解指定反序列化抽象类具体的子类
     */
    @NotNull(message = "指标数据不能为空")
    @Schema(description = "指标数据", required = true)
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
