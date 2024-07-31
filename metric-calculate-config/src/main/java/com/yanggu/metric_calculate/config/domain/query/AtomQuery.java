package com.yanggu.metric_calculate.config.domain.query;

import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.domain.vo.AtomVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class AtomQuery extends PageQuery<AtomVO> {

    @Serial
    private static final long serialVersionUID = 8038463967027058396L;

    private String atomName;

    private String atomDisplayName;

    private String modelName;

    private String modelDisplayName;

    private String aggregateFunctionName;

    private String timeFormat;

    private String timeColumnName;

    private String timeColumnDisplayName;

}
