/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.pattern;

import com.yanggu.metriccalculate.fieldprocess.Cond;
import com.yanggu.metriccalculate.fieldprocess.TimeBaselineDimension;

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

    public TimeBaselineCond timeBaseline(TimeBaselineDimension timeBaseline) {
        setTimeBaseline(timeBaseline);
        return this;
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
