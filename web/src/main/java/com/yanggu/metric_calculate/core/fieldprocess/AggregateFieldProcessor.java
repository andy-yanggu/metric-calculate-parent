package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 聚合字段处理器继承自度量字段处理器, 增加了聚合类型
 */
@Data
@Slf4j
@NoArgsConstructor
public class AggregateFieldProcessor<M extends MergedUnit<M>> extends MetricFieldProcessor<Object> {

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 是否是自定义udaf
     */
    private Boolean isUdaf;

    /**
     * 用户自定义聚合函数的参数
     */
    private Map<String, Object> udafParams;

    private transient UnitFactory unitFactory;

    @Override
    public void init() throws Exception {
        //初始化度量字段表达式
        super.init();
    }

    @Override
    public M process(JSONObject input) throws Exception {
        //获取度量值
        Object execute = super.process(input);
        if (execute == null) {
            return null;
        }

        //生成MergedUnit
        if (Boolean.TRUE.equals(isUdaf)) {
            return (M) unitFactory.initInstanceByValue(aggregateType, execute);
        } else {
            return (M) unitFactory.initInstanceByValueForUdaf(aggregateType, execute, udafParams);
        }
    }

}
