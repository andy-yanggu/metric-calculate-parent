package com.yanggu.metric_calculate.core.kryo.pool;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.util.Pool;


public class InputPool extends Pool<Input> {

    public InputPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
    }

    @Override
    protected Input create() {
        return new Input();
    }

    @Override
    public void free(Input object) {
        super.reset(object);
        super.free(object);
    }

}
