package com.yanggu.metric_calculate.config.pojo.query;

import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.pojo.entity.AtomEntity;
import lombok.Data;

@Data
public class AtomQuery extends PageQuery<AtomEntity> {

    private String atomName;

    private String atomDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

}
