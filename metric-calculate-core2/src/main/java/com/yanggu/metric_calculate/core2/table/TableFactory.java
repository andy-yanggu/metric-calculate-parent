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

    /**
     * 创建新的Table
     *
     * @return
     */
    public Table<IN, ACC, OUT> createTable() {
        int windowType = derive.getWindowType();
        //滚动时间窗口
        if (windowType == 0) {
            TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTimeTable<>();
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            return tumblingTimeTable;
            //滑动时间窗口
        } else if (windowType == 1) {
            SlidingTimeTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTimeTable<>();
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            return slidingTimeTable;
            //滑动计数窗口
        } else if (windowType == 2) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindowTable<>();
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return slidingCountWindowTable;
            //状态窗口
        } else if (windowType == 3) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = new StatusWindowTable<>();
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return (Table<IN, ACC, OUT>) statusWindowTable;
            //全窗口
        } else if (windowType == 4) {
            GlobalTable<IN, ACC, OUT> globalTable = new GlobalTable<>();
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return globalTable;
            //CEP类型
        } else if (Boolean.TRUE.equals(deriveMetricCalculate.getIsCep())) {
            PatternTable<IN, ACC, OUT> patternTable = new PatternTable<>();
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(derive.getChainPattern().getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.init();
            return (Table<IN, ACC, OUT>) patternTable;
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    /**
     * 给Table实现类的相关字段赋值
     * @param table
     */
    public void setTable(Table<IN, ACC, OUT> table) {
        int windowType = derive.getWindowType();
        //滚动时间窗口
        if (windowType == 0) {
            TumblingTimeTimeTable<IN, ACC, OUT> tumblingTimeTable = ((TumblingTimeTimeTable<IN, ACC, OUT>) table);
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动时间窗口
        } else if (windowType == 1) {
            SlidingTimeTimeTable<IN, ACC, OUT> slidingTimeTable = ((SlidingTimeTimeTable<IN, ACC, OUT>) table);
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(deriveMetricCalculate.getTimeBaselineDimension());
            //滑动计数窗口
        } else if (windowType == 2) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = ((SlidingCountWindowTable<IN, ACC, OUT>) table);
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //状态窗口
        } else if (windowType == 3) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = ((StatusWindowTable<IN, ACC, OUT>) table);
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //全窗口
        } else if (windowType == 4) {
            GlobalTable<IN, ACC, OUT> globalTable = ((GlobalTable<IN, ACC, OUT>) table);
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //CEP类型
        } else if (Boolean.TRUE.equals(deriveMetricCalculate.getIsCep())) {
            PatternTable<IN, ACC, OUT> patternTable = ((PatternTable<IN, ACC, OUT>) table);
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(derive.getChainPattern().getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.init();
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

}
