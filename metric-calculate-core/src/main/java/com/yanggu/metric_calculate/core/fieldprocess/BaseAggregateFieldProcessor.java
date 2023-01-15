package com.yanggu.metric_calculate.core.fieldprocess;

import cn.hutool.core.util.StrUtil;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 聚合型处理器继承自度量字段处理器
 * <p>数值型、对象型、集合型继承该抽象类</p>
 */
@Data
@Slf4j
@NoArgsConstructor
public abstract class BaseAggregateFieldProcessor<M extends MergedUnit<M>> extends MetricFieldProcessor<Object> {

    /**
     * 聚合类型
     */
    protected String aggregateType;

    protected Class<? extends MergedUnit<?>> mergeUnitClazz;

    /**
     * 是否是自定义udaf
     */
    protected Boolean isUdaf;

    /**
     * 用户自定义聚合函数的参数
     */
    protected Map<String, Object> udafParams;

    /**
     * 用于生成MergeUnit
     */
    protected UnitFactory unitFactory;

    @Override
    public void init() throws Exception {
        //初始化度量字段表达式
        super.init();

        if (StrUtil.isBlank(aggregateType)) {
            throw new RuntimeException("聚合类型为空");
        }

        if (unitFactory == null) {
            throw new RuntimeException("UnitFactory为空");
        }

        if (mergeUnitClazz == null) {
            throw new RuntimeException("需要设置mergeUnitClazz");
        }
    }

}
