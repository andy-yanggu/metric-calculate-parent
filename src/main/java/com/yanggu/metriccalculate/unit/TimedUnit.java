/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;


import com.yanggu.metriccalculate.value.TimeReferable;

public interface TimedUnit<U extends UnlimitedMergedUnit<U>> extends Unit<U>, TimeReferable {
}