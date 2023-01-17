package com.yanggu.metric_calculate.core.cube;

import java.io.Serializable;

public interface TimeReferable extends Serializable {

    long getReferenceTime();

    void setReferenceTime(long referenceTime);

}
