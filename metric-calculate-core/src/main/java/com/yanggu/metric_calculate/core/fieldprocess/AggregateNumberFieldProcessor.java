package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 聚合数值类型处理器继承自度量字段处理器, 增加了聚合类型
 */
@Data
@Slf4j
@NoArgsConstructor
public class AggregateNumberFieldProcessor<M extends MergedUnit<M>> extends BaseAggregateFieldProcessor<M> {

    //@Override
    //public void init() throws Exception {
    //    //初始化度量字段表达式
    //    super.init();
    //}

    @Override
    public M process(JSONObject input) throws Exception {
        //获取度量值
        Object execute = super.process(input);
        if (execute == null) {
            return null;
        }

        //生成MergedUnit
        return (M) unitFactory.initInstanceByValue(aggregateType, execute, udafParams);
    }

}
