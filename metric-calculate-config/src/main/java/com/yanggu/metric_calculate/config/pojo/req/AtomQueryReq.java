package com.yanggu.metric_calculate.config.pojo.req;

import lombok.Data;

@Data
public class AtomQueryReq {

    private String atomName;

    private String atomDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

}
