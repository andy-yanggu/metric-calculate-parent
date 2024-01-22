package com.yanggu.metric_calculate.config.pojo.query;

import lombok.Data;

@Data
public class AtomQuery {

    private String atomName;

    private String atomDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

}
