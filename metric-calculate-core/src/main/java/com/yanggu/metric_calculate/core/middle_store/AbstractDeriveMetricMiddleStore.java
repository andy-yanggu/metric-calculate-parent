package com.yanggu.metric_calculate.core.middle_store;


import cn.hutool.core.util.ArrayUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeriveMetricMiddleStore implements DeriveMetricMiddleStore {

    protected KryoPool kryoPool;

    public static final String DEFAULT_IMPL = "MEMORY";

    /**
     * DeriveMetricMiddleStore实现类初始化完成后, 需要放入其中
     */
    public static final Map<String, DeriveMetricMiddleStore> STORE_MAP = new HashMap<>();

    static {
        //默认放入memory
        DeriveMetricMiddleHashMapStore memory = new DeriveMetricMiddleHashMapStore();
        memory.init();
        STORE_MAP.put(DEFAULT_IMPL, memory);
    }

    @Override
    public void setKryoPool(KryoPool kryoPool) {
        this.kryoPool = kryoPool;
    }

    protected <T> T deserialize(byte[] bytes) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        Kryo kryo = kryoPool.borrow();
        try (Input input = new Input(bytes)) {
            return (T) kryo.readClassAndObject(input);
        } finally {
            kryoPool.release(kryo);
        }
    }

    @SneakyThrows
    protected byte[] serialize(Object object) {
        Kryo kryo = kryoPool.borrow();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            kryo.writeClassAndObject(output, object);
            return byteArrayOutputStream.toByteArray();
        } finally {
            kryoPool.release(kryo);
        }
    }

}
