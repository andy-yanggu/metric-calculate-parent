package com.yanggu.metriccalculate.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.unit.UnitFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 聚合字段处理器继承自度量字段处理器, 增加了聚合类型
 */
@Data
@Slf4j
public class AggregateFieldProcessor<M extends MergedUnit<M>> extends MetricFieldProcessor<Object> {

    /**
     * 聚合类型
     */
    private String aggregateType;

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
        return (M) UnitFactory.initInstanceByValue(aggregateType, execute);
    }

}
