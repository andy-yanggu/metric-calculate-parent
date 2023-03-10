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

    private static KryoPool KRYO_POOL;

    private static InputPool INPUT_POOL;

    private static OutputPool OUTPUT_POOL;

    public static void init(KryoPool kryoPool, InputPool inputPool, OutputPool outputPool) {
        KRYO_POOL = kryoPool;
        INPUT_POOL = inputPool;
        OUTPUT_POOL = outputPool;
    }

    /**
     * 序列化方法
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        Kryo kryo = KRYO_POOL.obtain();
        Output output = OUTPUT_POOL.obtain();
        try {
            kryo.writeClassAndObject(output, object);
            return output.getBuffer();
        } finally {
            KRYO_POOL.free(kryo);
            OUTPUT_POOL.free(output);
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
        Kryo kryo = KRYO_POOL.obtain();
        Input input = INPUT_POOL.obtain();
        input.setBuffer(bytes);
        try {
            return (T) kryo.readClassAndObject(input);
        } finally {
            INPUT_POOL.free(input);
            KRYO_POOL.free(kryo);
        }
    }

}
