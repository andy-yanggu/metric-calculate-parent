package com.yanggu.metric_calculate.core2.window;


import com.yanggu.metric_calculate.core2.aviator_function.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core2.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core2.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core2.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core2.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core2.pojo.metric.WindowParam;
import lombok.Data;

import java.util.Map;

import static com.yanggu.metric_calculate.core2.enums.WindowTypeEnum.*;

@Data
public class WindowFactory<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private WindowParam windowParam;

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 创建新的Table
     *
     * @return
     */
    public AbstractWindow<IN, ACC, OUT> createTable() {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeWindow<>();
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            return tumblingTimeTable;
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = new SlidingTimeWindow<>();
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            return slidingTimeTable;
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindow<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindow<>();
            slidingCountWindowTable.setLimit(windowParam.getLimit());
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return slidingCountWindowTable;
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindow<IN, ACC, OUT> statusWindowTable = new StatusWindow<>();
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            statusWindowTable.setFieldMap(fieldMap);
            statusWindowTable.setStatusExpressList(windowParam.getStatusExpressList());
            statusWindowTable.init();
            return statusWindowTable;
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalWindow<IN, ACC, OUT> globalTable = new GlobalWindow<>();
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            return globalTable;
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternWindow<IN, ACC, OUT> patternTable = new PatternWindow<>();
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
     * @param window
     */
    public void setTable(AbstractWindow<IN, ACC, OUT> window) {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = ((TumblingTimeWindow<IN, ACC, OUT>) window);
            tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            tumblingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            tumblingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = ((SlidingTimeWindow<IN, ACC, OUT>) window);
            slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            slidingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
            slidingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindow<IN, ACC, OUT> slidingCountWindowTable = ((SlidingCountWindow<IN, ACC, OUT>) window);
            slidingCountWindowTable.setLimit(windowParam.getLimit());
            slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindow<IN, ACC, OUT> statusWindowTable = ((StatusWindow<IN, ACC, OUT>) window);
            statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            statusWindowTable.setFieldMap(fieldMap);
            statusWindowTable.setStatusExpressList(windowParam.getStatusExpressList());
            statusWindowTable.init();
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalWindow<IN, ACC, OUT> globalTable = ((GlobalWindow<IN, ACC, OUT>) window);
            globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternWindow<IN, ACC, OUT> patternTable = ((PatternWindow<IN, ACC, OUT>) window);
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
