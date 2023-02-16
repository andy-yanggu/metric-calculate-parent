package com.yanggu.metric_calculate.core.middle_store;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.kryo.CoreKryoFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtils;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Data
@Slf4j
public class DeriveMetricMiddleRedisStore implements DeriveMetricMiddleStore {

    private KryoPool kryoPool;

    private RedisTemplate<String, byte[]> redisTemplate;

    private List<Class<? extends MergedUnit>> classList;

    @Override
    public void init() {
        kryoPool = KryoUtils.createRegisterKryoPool(new CoreKryoFactory(classList));
    }

    @Override
    public MetricCube get(MetricCube cube) {
        byte[] result = redisTemplate.opsForValue().get(cube.getRealKey());
        if (result == null) {
            return null;
        }
        Kryo kryo = kryoPool.borrow();
        try {
            return deserialize(kryo, result);
        } finally {
            kryoPool.release(kryo);
        }
    }

    @Override
    public void update(MetricCube cube) {
        Kryo kryo = kryoPool.borrow();
        try {
            byte[] bytes = serialize(kryo, cube);
            redisTemplate.opsForValue().set(cube.getRealKey(), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            kryoPool.release(kryo);
        }
    }

    private MetricCube deserialize(Kryo kryo, byte[] bytes) {
        if (bytes == null || bytes.length <= 5) {
            return null;
        }
        Input input = new Input(bytes);
        return (MetricCube) kryo.readClassAndObject(input);
    }

    private byte[] serialize(Kryo kryo, MetricCube unit) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Output output = new Output(byteArrayOutputStream);
            kryo.writeClassAndObject(output, unit);
            output.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

}
