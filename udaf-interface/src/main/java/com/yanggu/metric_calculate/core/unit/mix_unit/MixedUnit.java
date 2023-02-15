package com.yanggu.metric_calculate.core.unit.mix_unit;

import com.yanggu.metric_calculate.core.unit.MergedUnit;

import java.util.Map;

/**
 * 混合类型的聚合函数
 * <p>SUM(IF(type = ‘信用卡’, amount, 0)) / SUM(amount) 就是占比</p>
 * <p>信用卡消费金额占总渠道的比例</p>
 *
 * @param <U>
 */
public interface MixedUnit<U extends MixedUnit<U>> extends MergedUnit<U> {

    U addMergeUnit(Map<String, MergedUnit<?>> unitMap);

}