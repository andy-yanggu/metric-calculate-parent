/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;

import com.yanggu.metriccalculate.value.Cloneable;

import java.util.Collection;

public interface MixedUnit<U extends MixedUnit<U> & Cloneable<U>> extends MergedUnit<U> {

    U mixMerge(U mixUnit);

    U mixMerge(MergedUnit unit);

    Collection<Class<? extends MergedUnit>> supportUnit();

}
