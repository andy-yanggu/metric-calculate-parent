

package com.yanggu.metric_calculate.core.unit.pattern;

import java.io.Serializable;

public interface Cond<T> extends Serializable {

    boolean cond(T object);

}
