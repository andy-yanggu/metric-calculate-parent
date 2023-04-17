package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import lombok.Data;

import java.util.Map;

import static com.yanggu.metric_calculate.core2.enums.WindowTypeEnum.*;

@Data
public class TableFactory<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private Derive derive;

    private DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate;

    /**
     * 创建新的Table
     *
     * @return
     */
    public Table<IN, OUT> createTable() {
        WindowTypeEnum windowType = derive.getWindowParam().getWindowType();
        AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor = deriveMetricCalculate.getAggregateFieldProcessor();
        //CEP类型
        if (Boolean.TRUE.equals(deriveMetricCalculate.getIsCep())) {
            PatternTable<IN, ACC, OUT> patternTable = new PatternTable<>();
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(derive.getAggregateFunctionParam().getChainPattern().getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            patternTable.init();
            return (Table<IN, OUT>) patternTable;
            //滚动时间窗口
        } else if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTimeTable<>();
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            return tumblingTimeTable;
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTimeTable<>();
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            return slidingTimeTable;
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindowTable<>();
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return slidingCountWindowTable;
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = new StatusWindowTable<>();
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return (Table<IN, OUT>) statusWindowTable;
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalTable<IN, ACC, OUT> globalTable = new GlobalTable<>();
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return globalTable;
            //CEP类型
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    /**
     * 给Table实现类的相关字段赋值
     * @param table
     */
    public void setTable(Table<IN, OUT> table) {
        WindowTypeEnum windowType = derive.getWindowParam().getWindowType();
        AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor = deriveMetricCalculate.getAggregateFieldProcessor();
        //CEP类型
        if (Boolean.TRUE.equals(deriveMetricCalculate.getIsCep())) {
            PatternTable<IN, ACC, OUT> patternTable = ((PatternTable<IN, ACC, OUT>) table);
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(derive.getAggregateFunctionParam().getChainPattern().getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            patternTable.init();
            //滚动时间窗口
        } else if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = ((TumblingTimeTimeTable<IN, ACC, OUT>) table);
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeTimeTable<IN, ACC, OUT> slidingTimeTable = ((SlidingTimeTimeTable<IN, ACC, OUT>) table);
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = ((SlidingCountWindowTable<IN, ACC, OUT>) table);
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = ((StatusWindowTable<IN, ACC, OUT>) table);
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalTable<IN, ACC, OUT> globalTable = ((GlobalTable<IN, ACC, OUT>) table);
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //CEP类型
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

}
