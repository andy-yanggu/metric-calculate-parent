package com.yanggu.metric_calculate.core.value;

import java.io.Serializable;

public interface TimeReferable extends Serializable {
    long referenceTime();

    void referenceTime(long referenceTime);
}
