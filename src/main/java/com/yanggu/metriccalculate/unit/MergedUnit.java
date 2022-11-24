/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;

import com.yanggu.metriccalculate.value.Cloneable;

public interface MergedUnit<U extends MergedUnit<U> & Cloneable<U>> extends Unit<U>, Mergeable<U> {
    @Override
    U fastClone();

    @Override
    U merge(U that);
}
