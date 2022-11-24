/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;


public interface UnlimitedMergedUnit<M extends UnlimitedMergedUnit<M>> extends MergedUnit<M> {
    M unlimitedMerge(M that);
}