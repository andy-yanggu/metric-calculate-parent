

package com.yanggu.metric_calculate.core.fieldprocess;

import java.io.Serializable;

public interface Cond<T> extends Serializable {

    boolean cond(T object);

}
