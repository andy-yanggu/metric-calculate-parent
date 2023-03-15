package com.yanggu.metric_calculate.core.kryo;


import cn.hutool.core.util.ArrayUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;

public class KryoUtil {

    private KryoUtil() {
    }

    private static KryoPool kryoPool;

    private static InputPool inputPool;

    private static OutputPool outputPool;

    static {
        kryoPool = new KryoPool(true, true, 100);
        inputPool = new InputPool(true, true, 100);
        outputPool = new OutputPool(true, true, 100);
    }

    public static void init(KryoPool kryoPool, InputPool inputPool, OutputPool outputPool) {
        KryoUtil.kryoPool = kryoPool;
        KryoUtil.inputPool = inputPool;
        KryoUtil.outputPool = outputPool;
    }

    /**
     * 序列化方法
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        Kryo kryo = kryoPool.obtain();
        Output output = outputPool.obtain();
        try {
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        } finally {
            kryoPool.free(kryo);
            outputPool.free(output);
        }
    }

    /**
     * 反序列化方法
     *
     * @param bytes
     * @return
     * @param <T>
     */
    public static <T> T deserialize(byte[] bytes) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        Kryo kryo = kryoPool.obtain();
        Input input = inputPool.obtain();
        input.setBuffer(bytes);
        try {
            return (T) kryo.readClassAndObject(input);
        } finally {
            inputPool.free(input);
            kryoPool.free(kryo);
        }
    }

}
