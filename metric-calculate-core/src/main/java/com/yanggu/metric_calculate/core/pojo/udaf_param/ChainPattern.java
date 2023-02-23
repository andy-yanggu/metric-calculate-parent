package com.yanggu.metric_calculate.core.pojo.udaf_param;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class ChainPattern {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 事件模式数据
     */
    private List<NodePattern> nodePatternList;

}