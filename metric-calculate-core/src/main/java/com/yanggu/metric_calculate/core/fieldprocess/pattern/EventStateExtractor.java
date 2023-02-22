package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.aggregate.BaseAggregateFieldProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.filter.FilterFieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.*;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Data
public class EventStateExtractor<T> implements AggregateFieldProcessor<T, MatchState<TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>>>> {

    private UnitFactory unitFactory;

    private ChainPattern chainPattern;

    private Map<String, Class<?>> fieldMap;

    private TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, FilterFieldProcessor<T>> filterFieldProcessorMap;

    /**
     * 需要进行二次聚合计算
     * <p>例如滑动计数窗口函数, 最近5次, 求平均值</p>
     * <p>CEP, 按照最后一条数据进行聚合计算</p>
     */
    private BaseAggregateFieldProcessor<T, ?> externalAggregateFieldProcessor;

    @Override
    public void init() throws Exception {
        List<ChainPattern.Node> nodes = chainPattern.getNodes();
        TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, FilterFieldProcessor<T>> tempFilterFieldProcessorMap =
                new TreeMap<>();

        for (int i = 0; i < nodes.size(); i++) {
            ChainPattern.Node node = nodes.get(i);
            String name = node.getName();
            FilterFieldProcessor<T> filterFieldProcessor =
                    FieldProcessorUtil.getFilterFieldProcessor(fieldMap, node.getMatchExpress());
            KeyValue<Key<Integer>, CloneWrapper<String>> keyValue = new KeyValue<>(new Key<>(i), CloneWrapper.wrap(name));
            tempFilterFieldProcessorMap.put(keyValue, filterFieldProcessor);
        }
        this.filterFieldProcessorMap = tempFilterFieldProcessorMap;

        //TODO 配置额外字段处理器

    }

    @Override
    public MatchState<TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>>> process(T event) {
        TreeMap<KeyValue<Key<Integer>, CloneWrapper<String>>, CloneWrapper<T>> dataMap = new TreeMap<>();
        filterFieldProcessorMap.forEach((keyValue, filterProcessor) -> {
            Boolean process = filterProcessor.process(event);
            if (process.equals(true)) {
                dataMap.put(keyValue, CloneWrapper.wrap(event));
            }
        });

        return new MatchState<>(dataMap);
    }

    @Override
    public String getAggregateType() {
        return null;
    }

    @Override
    public Class<? extends MergedUnit<?>> getMergeUnitClazz() {
        return null;
    }

    @Override
    public Object callBack(Object input) {
        List<T> tempValueList = (List<T>) input;
        MergedUnit mergedUnit = tempValueList.stream()
                .map(tempValue -> externalAggregateFieldProcessor.process(tempValue))
                .reduce(MergedUnit::merge)
                .orElseThrow(() -> new RuntimeException("MergeUnit的merge方法执行失败"));
        return ValueMapper.value(((Value<?>) mergedUnit));
    }

}