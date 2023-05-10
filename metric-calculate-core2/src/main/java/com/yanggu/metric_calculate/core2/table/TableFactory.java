package com.yanggu.metric_calculate.core2.table;


import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.WindowParam;
import lombok.Data;

import java.util.Map;

import static com.yanggu.metric_calculate.core2.enums.WindowTypeEnum.*;

@Data
public class TableFactory<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private WindowParam windowParam;

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 创建新的Table
     *
     * @return
     */
    public Table<IN, ACC, OUT> createTable() {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeTable<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeTable<>();
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            return tumblingTimeTable;
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeTable<IN, ACC, OUT> slidingTimeTable = new SlidingTimeTable<>();
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            return slidingTimeTable;
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindowTable<>();
            slidingCountWindowTable.setLimit(windowParam.getLimit());
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return slidingCountWindowTable;
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = new StatusWindowTable<>();
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            statusWindowTable.setFieldMap(fieldMap);
            statusWindowTable.setStatusExpressList(windowParam.getStatusExpressList());
            statusWindowTable.init();
            return statusWindowTable;
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalTable<IN, ACC, OUT> globalTable = new GlobalTable<>();
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return globalTable;
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternTable<IN, ACC, OUT> patternTable = new PatternTable<>();
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(windowParam.getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.setTimeFieldProcessor(timeFieldProcessor);
            patternTable.setTimeBaselineDimension(createTimeBaselineDimension());
            patternTable.init();
            return patternTable;
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    /**
     * 给Table实现类的相关字段赋值
     *
     * @param table
     */
    public void setTable(Table<IN, ACC, OUT> table) {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeTable<IN, ACC, OUT> tumblingTimeTable = ((TumblingTimeTable<IN, ACC, OUT>) table);
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeTable<IN, ACC, OUT> slidingTimeTable = ((SlidingTimeTable<IN, ACC, OUT>) table);
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindowTable<IN, ACC, OUT> slidingCountWindowTable = ((SlidingCountWindowTable<IN, ACC, OUT>) table);
            slidingCountWindowTable.setLimit(windowParam.getLimit());
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindowTable<IN, ACC, OUT> statusWindowTable = ((StatusWindowTable<IN, ACC, OUT>) table);
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            statusWindowTable.setFieldMap(fieldMap);
            statusWindowTable.setStatusExpressList(windowParam.getStatusExpressList());
            statusWindowTable.init();
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalTable<IN, ACC, OUT> globalTable = ((GlobalTable<IN, ACC, OUT>) table);
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternTable<IN, ACC, OUT> patternTable = ((PatternTable<IN, ACC, OUT>) table);
            patternTable.setFieldMap(fieldMap);
            patternTable.setNodePatternList(windowParam.getNodePatternList());
            patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            patternTable.setTimeFieldProcessor(timeFieldProcessor);
            patternTable.setTimeBaselineDimension(createTimeBaselineDimension());
            patternTable.init();
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    private TimeBaselineDimension createTimeBaselineDimension() {
        return new TimeBaselineDimension(windowParam.getDuration(), windowParam.getTimeUnit());
    }

}
