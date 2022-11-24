/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.obj;

import com.yanggu.metriccalculate.unit.MergedUnit;

public interface ObjectiveUnit<T, U extends ObjectiveUnit<T, U>> extends MergedUnit<U> {

    U value(T object);

}
