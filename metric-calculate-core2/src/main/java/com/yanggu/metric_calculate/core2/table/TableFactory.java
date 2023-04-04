package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import lombok.Data;

import java.util.Map;

@Data
public class TableFactory<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private Derive derive;

    private DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate;

    public Table<IN, ACC, OUT> createTable() {
        int windowType = derive.getWindowType();
        //滚动时间窗口
        if (windowType == 0) {
            TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTimeTable<>();
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动时间窗口
        } else if (windowType == 1) {
            SlidingTimeTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTimeTable<>();
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动计数窗口
        } else if (windowType == 2) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindowTable<>();
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //状态窗口
        } else if (windowType == 3) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = new StatusWindowTable<>();
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //CEP类型
        } else if (windowType == 4) {
            PatternTable<IN, ACC, OUT> patternTable = new PatternTable<>();
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(derive.getChainPattern().getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.init();
            return (Table<IN, ACC, OUT>) patternTable;
            //全窗口
        } else if (windowType == 5) {
            GlobalTable<IN, ACC, OUT> globalTable = new GlobalTable<>();
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
        } else {
            throw new RuntimeException("窗口类型异常");
        }
        return null;
    }

}
