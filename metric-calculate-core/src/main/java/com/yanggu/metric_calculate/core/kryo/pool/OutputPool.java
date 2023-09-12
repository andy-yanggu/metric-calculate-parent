package com.yanggu.metric_calculate.core.kryo.pool;

import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;


public class OutputPool extends Pool<Output> {

    public OutputPool(int maximumCapacity) {
        super(true, true, maximumCapacity);
    }

    @Override
    protected Output create() {
        return new Output(1024, -1);
    }

}
