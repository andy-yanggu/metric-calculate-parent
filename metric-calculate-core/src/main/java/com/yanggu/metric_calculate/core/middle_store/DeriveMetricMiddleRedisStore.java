package com.yanggu.metric_calculate.core.middle_store;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import io.lettuce.core.KeyValue;
import io.lettuce.core.Value;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class DeriveMetricMiddleRedisStore extends AbstractDeriveMetricMiddleStore {

    private GenericObjectPool<StatefulRedisConnection<byte[], byte[]>> redisConnectionPool;

    @Override
    public void init() {
        if (redisConnectionPool == null) {
            throw new RuntimeException("redisConnectionPool不能为空");
        }
    }

    @Override
    public <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet) throws Exception {
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        try {
            byte[] result = connection.sync().get(dimensionSet.getRealKey().getBytes());
            return super.deserialize(result);
        } finally {
            redisConnectionPool.returnObject(connection);
        }
    }

    @Override
    public Map<DimensionSet, MetricCube> batchGet(List<DimensionSet> dimensionSetList) throws Exception {
        byte[][] keyStringArray = dimensionSetList.stream()
                .map(DimensionSet::getRealKey)
                .distinct()
                .map(String::getBytes)
                .toArray(byte[][]::new);
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        List<KeyValue<byte[], byte[]>> dataList;
        try {
            dataList = connection.sync().mget(keyStringArray);
        } finally {
            redisConnectionPool.returnObject(connection);
        }
        Map<DimensionSet, MetricCube> map = new HashMap<>();
        if (CollUtil.isEmpty(dataList)) {
            return map;
        }
        Map<byte[], byte[]> collect = dataList.stream()
                .filter(Value::hasValue)
                .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        for (DimensionSet dimensionSet : dimensionSetList) {
            byte[] bytes = collect.get(dimensionSet.getRealKey().getBytes());
            MetricCube deserialize = super.deserialize(bytes);
            if (deserialize != null) {
                map.put(dimensionSet, deserialize);
            }
        }
        return map;
    }

    @Override
    public <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube) throws Exception {
        byte[] serialize = super.serialize(updateMetricCube);
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        try {
            connection.sync().set(updateMetricCube.getRealKey().getBytes(), serialize);
        } finally {
            redisConnectionPool.returnObject(connection);
        }
    }

    @Override
    public void batchUpdate(List<MetricCube> metricCubes) throws Exception {
        Map<byte[], byte[]> collect = metricCubes.stream()
                .collect(Collectors.toMap(temp -> temp.getRealKey().getBytes(), super::serialize));
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        try {
            connection.sync().mset(collect);
        } finally {
            redisConnectionPool.returnObject(connection);
        }
    }

    @Override
    public void deleteData(DimensionSet dimensionSet) throws Exception {
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        try {
            connection.sync().del(dimensionSet.getRealKey().getBytes());
        } finally {
            redisConnectionPool.returnObject(connection);
        }
    }

    @Override
    public void batchDeleteData(List<DimensionSet> dimensionSetList) throws Exception {
        byte[][] collect = dimensionSetList.stream()
                .map(DimensionSet::getRealKey)
                .distinct()
                .map(String::getBytes)
                .toArray(byte[][]::new);
        StatefulRedisConnection<byte[], byte[]> connection = redisConnectionPool.borrowObject();
        try {
            connection.sync().del(collect);
        } finally {
            redisConnectionPool.returnObject(connection);
        }
    }

}
