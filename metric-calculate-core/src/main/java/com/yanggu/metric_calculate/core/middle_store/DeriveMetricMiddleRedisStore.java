package com.yanggu.metric_calculate.core.middle_store;


import cn.hutool.extra.spring.SpringUtil;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
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
    public MetricCube get(MetricCube cube) {
        byte[] result = redisTemplate.opsForValue().get(cube.getRealKey());
        return super.deserialize(result);
    }

    @Override
    public Map<DimensionSet, MetricCube> batchGet(List<MetricCube> cubeList) {
        Map<DimensionSet, MetricCube> map = new HashMap<>();
        List<String> collect = cubeList.stream()
                .map(MetricCube::getRealKey)
                .distinct()
                .collect(Collectors.toList());
        List<byte[]> bytesList = redisTemplate.opsForValue().multiGet(collect);
        for (MetricCube metricCube : cubeList) {
            byte[] bytes = bytesList.get(collect.indexOf(metricCube.getRealKey()));
            MetricCube deserialize = super.deserialize(bytes);
            if (deserialize != null) {
                map.put(metricCube.getDimensionSet(), deserialize);
            }
        }
        return map;
    }

    @Override
    public void update(MetricCube cube) {
        byte[] serialize = super.serialize(cube);
        redisTemplate.opsForValue().set(cube.getRealKey(), serialize);
    }

    @Override
    public void batchUpdate(List<MetricCube> metricCubes) {
        Map<String, byte[]> collect = metricCubes.stream()
                .collect(Collectors.toMap(MetricCube::getRealKey, super::serialize));
        redisTemplate.opsForValue().multiSet(collect);
    }

}
