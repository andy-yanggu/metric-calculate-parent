package com.yanggu.metric_calculate.pojo;

import com.yanggu.metric_calculate.core2.table.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;

@Data
@ApiModel("指标数据")
public class TableData {

    @NotNull
    @ApiModelProperty("数据明细宽表id")
    private Long tableId;

    @NotNull
    @ApiModelProperty("派生指标id")
    private Long deriveId;

    @NotNull
    @ApiModelProperty("维度值")
    private LinkedHashMap<String, Object> dimensionMap;

    @NotNull
    @ApiModelProperty("窗口值")
    private Table table;

}
