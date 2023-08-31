package com.yanggu.metric_calculate.config.pojo.req;

import com.yanggu.metric_calculate.config.enums.WindowTypeEnum;
import lombok.Data;

@Data
public class DeriveQueryReq {

    private String deriveName;

    private String deriveDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

    private String dimensionColumnName;

    private String dimensionColumnDisplayName;

    private String dimensionName;

    private String dimensionDisplayName;

    private WindowTypeEnum windowType;

}
