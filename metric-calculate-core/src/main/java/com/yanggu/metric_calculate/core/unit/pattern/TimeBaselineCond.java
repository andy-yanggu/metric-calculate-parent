package com.yanggu.metric_calculate.core.unit.pattern;

import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;

public class TimeBaselineCond implements Cond<Long> {

    private TimeBaselineDimension timeBaseline;

    private long expectInterval;

    public TimeBaselineCond() {
    }

    public TimeBaselineCond(TimeBaselineDimension timeBaseline) {
        setTimeBaseline(timeBaseline);
    }

    public TimeBaselineDimension timeBaseline() {
        return timeBaseline;
    }

    protected void setTimeBaseline(TimeBaselineDimension timeBaseline) {
        this.timeBaseline = timeBaseline;
        this.expectInterval = timeBaseline.realLength();
    }

    @Override
    public boolean cond(Long realInterval) {
        return realInterval != null && (realInterval > 0 && realInterval <= expectInterval);
    }

}
