/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;

public interface CollectionUnit<T, U extends CollectionUnit<T, U>> extends MergedUnit<U> {

    U add(T value);

}
