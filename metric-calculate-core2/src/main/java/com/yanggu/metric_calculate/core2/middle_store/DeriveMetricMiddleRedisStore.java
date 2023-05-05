package com.yanggu.metric_calculate.core2.middle_store;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Slf4j
public class DeriveMetricMiddleRedisStore extends AbstractDeriveMetricMiddleStore {

    private RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void init() {
        RedisTemplate<String, byte[]> kryoRedisTemplate = SpringUtil.getBean("kryoRedisTemplate");
        this.setRedisTemplate(kryoRedisTemplate);
        AbstractDeriveMetricMiddleStore.DeriveMetricMiddleStoreHolder.getStoreMap().put("REDIS_STRING", this);
    }

    @Override
    public <IN, ACC, OUT> MetricCube<IN, ACC, OUT> get(DimensionSet dimensionSet) {
        byte[] result = redisTemplate.opsForValue().get(dimensionSet.getRealKey());
        return super.deserialize(result);
    }

    @Override
    public Map<DimensionSet, MetricCube> batchGet(List<DimensionSet> dimensionSetList) {
        List<String> collect = dimensionSetList.stream()
                .map(DimensionSet::getRealKey)
                .distinct()
                .collect(Collectors.toList());
        List<byte[]> bytesList = redisTemplate.opsForValue().multiGet(collect);
        Map<DimensionSet, MetricCube> map = new HashMap<>();
        if (CollUtil.isEmpty(bytesList)) {
            return map;
        }
        for (DimensionSet dimensionSet : dimensionSetList) {
            byte[] bytes = bytesList.get(collect.indexOf(dimensionSet.getRealKey()));
            MetricCube deserialize = super.deserialize(bytes);
            if (deserialize != null) {
                map.put(dimensionSet, deserialize);
            }
        }
        return map;
    }

    @Override
    public <IN, ACC, OUT> void update(MetricCube<IN, ACC, OUT> updateMetricCube) {
        byte[] serialize = super.serialize(updateMetricCube);
        redisTemplate.opsForValue().set(updateMetricCube.getRealKey(), serialize);
    }

    @Override
    public void batchUpdate(List<MetricCube> metricCubes) {
        Map<String, byte[]> collect = metricCubes.stream()
                .collect(Collectors.toMap(MetricCube::getRealKey, super::serialize));
        redisTemplate.opsForValue().multiSet(collect);
    }

}
