/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.pattern;

import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.Value;

public interface EventState<T, E extends EventState<T, E>> extends MergedUnit<E>, Value<T> {
}
