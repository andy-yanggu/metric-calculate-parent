package com.yanggu.metric_calculate.core.kryo.pool;

import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;


public class OutputPool extends Pool<Output> {

    public OutputPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
    }

    @Override
    protected Output create() {
        return new Output(1024, -1);
    }

    @Override
    public void free(Output object) {
        super.reset(object);
        super.free(object);
    }

}
