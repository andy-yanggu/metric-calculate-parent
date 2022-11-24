/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.value;

import java.io.Serializable;

public interface TimeReferable extends Serializable {
    long referenceTime();

    void referenceTime(long referenceTime);
}
