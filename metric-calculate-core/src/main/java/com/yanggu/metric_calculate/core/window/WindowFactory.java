package com.yanggu.metric_calculate.core.window;


import com.yanggu.metric_calculate.core.enums.WindowTypeEnum;
import com.yanggu.metric_calculate.core.field_process.aggregate.AggregateFieldProcessor;
import com.yanggu.metric_calculate.core.field_process.time.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
import com.yanggu.metric_calculate.core.pojo.window.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.window.WindowParam;
import lombok.Data;

import java.util.Map;

import static com.yanggu.metric_calculate.core.enums.WindowTypeEnum.*;

/**
 * 窗口工厂类
 * <p>构造窗口和给窗口字段赋值</p>
 *
 * @param <IN>
 * @param <ACC>
 * @param <OUT>
 */
@Data
public class WindowFactory<IN, ACC, OUT> {

    private Map<String, Class<?>> fieldMap;

    private WindowParam windowParam;

    private AggregateFieldProcessor<IN, ACC, OUT> aggregateFieldProcessor;

    private AviatorFunctionFactory aviatorFunctionFactory;

    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 创建新的Window
     *
     * @return
     */
    public AbstractWindow<IN, ACC, OUT> createWindow() {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = new TumblingTimeWindow<>();
            setTumblingTimeWindow(tumblingTimeTable);
            return tumblingTimeTable;
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = new SlidingTimeWindow<>();
            setSlidingTimeWindow(slidingTimeTable);
            return slidingTimeTable;
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindow<IN, ACC, OUT> slidingCountWindowTable = new SlidingCountWindow<>();
            setSlidingCountWindow(slidingCountWindowTable);
            return slidingCountWindowTable;
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindow<IN, ACC, OUT> statusWindowTable = new StatusWindow<>();
            setStatusWindow(statusWindowTable);
            statusWindowTable.init();
            return statusWindowTable;
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalWindow<IN, ACC, OUT> globalTable = new GlobalWindow<>();
            setGlobalWindow(globalTable);
            return globalTable;
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternWindow<IN, ACC, OUT> patternTable = new PatternWindow<>();
            setPatternWindow(patternTable);
            patternTable.init();
            return patternTable;
            //会话窗口
        } else if (windowType == SESSION_WINDOW) {
            SessionWindow<IN, ACC, OUT> sessionWindow = new SessionWindow<>();
            setSessionWindow(sessionWindow);
            sessionWindow.init();
            return sessionWindow;
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    /**
     * 给Window实现类的相关字段赋值
     *
     * @param window
     */
    public void setWindow(AbstractWindow<IN, ACC, OUT> window) {
        WindowTypeEnum windowType = windowParam.getWindowType();
        //滚动时间窗口
        if (windowType == TUMBLING_TIME_WINDOW) {
            TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable = ((TumblingTimeWindow<IN, ACC, OUT>) window);
            setTumblingTimeWindow(tumblingTimeTable);
            //滑动时间窗口
        } else if (windowType == SLIDING_TIME_WINDOW) {
            SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable = ((SlidingTimeWindow<IN, ACC, OUT>) window);
            setSlidingTimeWindow(slidingTimeTable);
            //滑动计数窗口
        } else if (windowType == SLIDING_COUNT_WINDOW) {
            SlidingCountWindow<IN, ACC, OUT> slidingCountWindowTable = ((SlidingCountWindow<IN, ACC, OUT>) window);
            setSlidingCountWindow(slidingCountWindowTable);
            //状态窗口
        } else if (windowType == STATUS_WINDOW) {
            StatusWindow<IN, ACC, OUT> statusWindowTable = ((StatusWindow<IN, ACC, OUT>) window);
            setStatusWindow(statusWindowTable);
            statusWindowTable.init();
            //全窗口
        } else if (windowType == GLOBAL_WINDOW) {
            GlobalWindow<IN, ACC, OUT> globalTable = ((GlobalWindow<IN, ACC, OUT>) window);
            setGlobalWindow(globalTable);
            //CEP类型
        } else if (windowType == EVENT_WINDOW) {
            PatternWindow<IN, ACC, OUT> patternTable = ((PatternWindow<IN, ACC, OUT>) window);
            setPatternWindow(patternTable);
            patternTable.init();
            //会话窗口
        } else if (windowType == SESSION_WINDOW) {
            SessionWindow<IN, ACC, OUT> sessionWindow = (SessionWindow<IN, ACC, OUT>) window;
            setSessionWindow(sessionWindow);
            sessionWindow.init();
        } else {
            throw new RuntimeException("窗口类型异常");
        }
    }

    private void setGlobalWindow(GlobalWindow<IN, ACC, OUT> globalTable) {
        globalTable.setAggregateFieldProcessor(aggregateFieldProcessor);
    }

    private void setSlidingCountWindow(SlidingCountWindow<IN, ACC, OUT> slidingCountWindowTable) {
        slidingCountWindowTable.setLimit(windowParam.getSlidingCount());
        slidingCountWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
    }

    private void setTumblingTimeWindow(TumblingTimeWindow<IN, ACC, OUT> tumblingTimeTable) {
        tumblingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
        tumblingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
        tumblingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
    }

    private void setPatternWindow(PatternWindow<IN, ACC, OUT> patternTable) {
        patternTable.setFieldMap(fieldMap);
        patternTable.setNodePatternList(windowParam.getNodePatternList());
        patternTable.setAggregateFieldProcessor(aggregateFieldProcessor);
        patternTable.setTimeFieldProcessor(timeFieldProcessor);
        patternTable.setTimeBaselineDimension(createTimeBaselineDimension());
        patternTable.setAviatorFunctionFactory(aviatorFunctionFactory);
    }

    private void setStatusWindow(StatusWindow<IN, ACC, OUT> statusWindowTable) {
        statusWindowTable.setAggregateFieldProcessor(aggregateFieldProcessor);
        statusWindowTable.setFieldMap(fieldMap);
        statusWindowTable.setStatusExpressParamList(windowParam.getStatusExpressParamList());
        statusWindowTable.setAviatorFunctionFactory(aviatorFunctionFactory);
    }

    private void setSlidingTimeWindow(SlidingTimeWindow<IN, ACC, OUT> slidingTimeTable) {
        slidingTimeTable.setAggregateFieldProcessor(aggregateFieldProcessor);
        slidingTimeTable.setTimeFieldProcessor(timeFieldProcessor);
        slidingTimeTable.setTimeBaselineDimension(createTimeBaselineDimension());
    }

    private void setSessionWindow(SessionWindow<IN, ACC, OUT> sessionWindow) {
        sessionWindow.setTimeFieldProcessor(timeFieldProcessor);
        sessionWindow.setGapTimeMillis(windowParam.getGapTimeMillis());
    }

    private TimeBaselineDimension createTimeBaselineDimension() {
        return new TimeBaselineDimension(windowParam.getDuration(), windowParam.getTimeUnit());
    }

}
