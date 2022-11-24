/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;

public interface Mergeable<M extends Mergeable> {
    M merge(M that);
}
