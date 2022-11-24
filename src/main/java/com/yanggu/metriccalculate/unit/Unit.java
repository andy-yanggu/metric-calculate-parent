/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;

import com.yanggu.metriccalculate.value.Cloneable;

public interface Unit<U extends Unit<U>> extends Cloneable<U> {
    @Override
    U fastClone();
}
