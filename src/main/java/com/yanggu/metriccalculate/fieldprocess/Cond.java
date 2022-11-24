/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.fieldprocess;

import java.io.Serializable;

public interface Cond<T> extends Serializable {

    boolean cond(T object);

}
