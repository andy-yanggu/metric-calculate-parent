package com.yanggu.metric_calculate.core.kryo.pool;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.util.Pool;


public class InputPool extends Pool<Input> {

    public InputPool(int maximumCapacity) {
        super(true, true, maximumCapacity);
    }

    @Override
    protected Input create() {
        return new Input();
    }

}
