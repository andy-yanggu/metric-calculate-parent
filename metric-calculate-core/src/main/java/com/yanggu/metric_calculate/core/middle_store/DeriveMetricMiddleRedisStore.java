package com.yanggu.metric_calculate.core.middle_store;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.kryo.CoreKryoFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtils;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class DeriveMetricMiddleRedisStore<M extends MergedUnit<M> & Value<?>> implements DeriveMetricMiddleStore<M> {

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
    public Map<DimensionSet, MetricCube<Table, Long, ?, ?>> batchGet(List<MetricCube<Table, Long, M, ?>> cubeList) {
        Map<DimensionSet, MetricCube<Table, Long, ?, ?>> map = new HashMap<>();
        List<String> collect = cubeList.stream().map(MetricCube::getRealKey).distinct().collect(Collectors.toList());
        List<byte[]> bytesList = redisTemplate.opsForValue().multiGet(collect);
        for (MetricCube<Table, Long, M, ?> metricCube : cubeList) {
            byte[] bytes = bytesList.get(collect.indexOf(metricCube.getRealKey()));
            Kryo kryo = kryoPool.borrow();
            try {
                MetricCube deserialize = deserialize(kryo, bytes);
                if (deserialize != null) {
                    map.put(metricCube.getDimensionSet(), deserialize);
                }
            } finally {
                kryoPool.release(kryo);
            }
        }
        return map;
    }

    @Override
    public void update(MetricCube cube) {
        Kryo kryo = kryoPool.borrow();
        try {
            byte[] bytes = serialize(kryo, cube);
            redisTemplate.opsForValue().set(cube.getRealKey(), bytes);
        } finally {
            kryoPool.release(kryo);
        }
    }

    @Override
    public void batchUpdate(List<MetricCube<Table, Long, M, ?>> metricCubes) {
        Map<String, byte[]> collect = metricCubes.stream()
                .collect(Collectors.toMap(MetricCube::getRealKey, tempCube -> {
                    Kryo kryo = kryoPool.borrow();
                    try {
                        return serialize(kryo, tempCube);
                    } finally {
                        kryoPool.release(kryo);
                    }
                }));
        redisTemplate.opsForValue().multiSet(collect);
    }

    private MetricCube deserialize(Kryo kryo, byte[] bytes) {
        if (bytes == null || bytes.length <= 5) {
            return null;
        }
        Input input = new Input(bytes);
        return (MetricCube) kryo.readClassAndObject(input);
    }

    @SneakyThrows
    private byte[] serialize(Kryo kryo, MetricCube unit) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Output output = new Output(byteArrayOutputStream);
            kryo.writeClassAndObject(output, unit);
            output.close();
            return byteArrayOutputStream.toByteArray();
        }
    }

}
