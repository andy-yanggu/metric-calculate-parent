package com.yanggu.metric_calculate.core.kryo;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.reflect.FieldUtil;

/**
 * Kryo序列化和反序列化工具类
 */
public class KryoUtil {

    private final KryoPool kryoPool;

    private final InputPool inputPool;

    private final OutputPool outputPool;

    public KryoUtil(KryoPool kryoPool, InputPool inputPool, OutputPool outputPool) {
        this.kryoPool = kryoPool;
        this.inputPool = inputPool;
        this.outputPool = outputPool;
    }

    /**
     * 序列化方法
     *
     * @param object
     * @return
     */
    public byte[] serialize(Object object) {
        if (object == null) {
            throw new RuntimeException("传入的对象为空");
        }
        Kryo kryo = kryoPool.obtain();
        Output output = outputPool.obtain();
        try {
            kryo.writeClassAndObject(output, object);
            return output.toBytes();
        } finally {
            kryoPool.free(kryo);
            outputPool.free(output);
            //兼容低版本的kryo重置kryo和output对象
            kryo.reset();
            output.setPosition(0);
            FieldUtil.setFieldValue(output, "total", 0L);
        }
    }

    /**
     * 反序列化方法
     *
     * @param bytes
     * @param <T>
     * @return
     */
    public <T> T deserialize(byte[] bytes) {
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
            //兼容低版本的kryo重置kryo和input对象
            kryo.reset();
            input.setPosition(0);
            FieldUtil.setFieldValue(input, "total", 0L);
        }
    }

}
