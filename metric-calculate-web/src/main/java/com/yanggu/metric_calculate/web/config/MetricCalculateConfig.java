package com.yanggu.metric_calculate.web.config;

import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleRedisStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent;
import com.yanggu.metric_calculate.web.pojo.PutRequest;
import com.yanggu.metric_calculate.web.pojo.QueryRequest;
import com.yanggu.metric_calculate.web.util.TLogThreadPoolTaskExecutor;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.dromara.hutool.core.thread.NamedThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionProvider;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 指标计算配置类
 */
@Slf4j
@Configuration
public class MetricCalculateConfig {

    /**
     * 派生指标中间存储配置
     */
    @Bean
    public DeriveMetricMiddleStore redisDeriveMetricMiddleStore(LettuceConnectionFactory lettuceConnectionFactory) {
        LettuceConnectionProvider connectionProvider = (LettuceConnectionProvider) FieldUtil.getFieldValue(lettuceConnectionFactory, "connectionProvider");
        LettucePoolingClientConfiguration clientConfiguration = (LettucePoolingClientConfiguration) lettuceConnectionFactory.getClientConfiguration();
        GenericObjectPool<StatefulRedisConnection<byte[], byte[]>> genericObjectPool = ConnectionPoolSupport.createGenericObjectPool(() -> connectionProvider.getConnection(StatefulRedisConnection.class),
                clientConfiguration.getPoolConfig(), false);
        DeriveMetricMiddleRedisStore deriveMetricMiddleStore = new DeriveMetricMiddleRedisStore();
        deriveMetricMiddleStore.setRedisConnectionPool(genericObjectPool);
        deriveMetricMiddleStore.init();
        log.info("派生指标外部存储初始化完成: 指标存储类: {}", deriveMetricMiddleStore.getClass().getName());
        return deriveMetricMiddleStore;
    }

    /**
     * 攒批查询组件
     */
    @Bean
    public AccumulateBatchComponent<QueryRequest> queryComponent(
            DeriveMetricMiddleStore deriveMetricMiddleStore,
            @Value("${metric-calculate.accumulate-batch-component.read.thread-num}") Integer threadNum,
            @Value("${metric-calculate.accumulate-batch-component.read.limit}") Integer limit,
            @Value("${metric-calculate.accumulate-batch-component.read.interval}") Integer interval) throws Exception {
        Consumer<List<QueryRequest>> batchGetConsumer = queryRequests -> {
            List<DimensionSet> collect = queryRequests.stream()
                    .map(QueryRequest::getDimensionSet)
                    //去一下重
                    .distinct()
                    .collect(Collectors.toList());

            //批量查询
            Map<DimensionSet, MetricCube> map = null;
            try {
                map = deriveMetricMiddleStore.batchGet(collect);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (map == null) {
                map = Collections.emptyMap();
            }

            //批量查询完成后, 进行回调通知
            for (QueryRequest queryRequest : queryRequests) {
                MetricCube historyMetricCube = map.get(queryRequest.getDimensionSet());
                queryRequest.getQueryFuture().complete(historyMetricCube);
            }
        };
        log.info("攒批读组件初始化完成, 并行度: {}, 攒批大小: {}, 攒批时间: {}毫秒", threadNum, limit, interval);
        return new AccumulateBatchComponent<>("攒批读组件", threadNum, limit, interval, batchGetConsumer);
    }

    /**
     * 批量更新组件
     */
    @Bean
    public AccumulateBatchComponent<PutRequest> putComponent(
            DeriveMetricMiddleStore deriveMetricMiddleStore,
            @Value("${metric-calculate.accumulate-batch-component.write.thread-num}") Integer threadNum,
            @Value("${metric-calculate.accumulate-batch-component.write.limit}") Integer limit,
            @Value("${metric-calculate.accumulate-batch-component.write.interval}") Integer interval) {
        Consumer<List<PutRequest>> batchUpdateConsumer = putRequests -> {
            List<MetricCube> collect = putRequests.stream()
                    .map(PutRequest::getMetricCube)
                    .collect(Collectors.toList());
            //TODO 需要考虑请求合并

            //批量更新
            try {
                deriveMetricMiddleStore.batchUpdate(collect);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //批量更新完成后, 进行回调通知
            for (PutRequest putRequest : putRequests) {
                MetricCube metricCube = putRequest.getMetricCube();
                putRequest.getResultFuture().complete(metricCube.query());
            }
        };
        log.info("攒批写组件初始化完成, 并行度: {}, 攒批大小: {}, 攒批时间: {}毫秒", threadNum, limit, interval);
        return new AccumulateBatchComponent<>("攒批写组件", threadNum, limit, interval, batchUpdateConsumer);
    }

    /**
     * 配置TLog线程池
     */
    @Bean
    public TLogThreadPoolTaskExecutor tLogThreadPoolExecutor(@Value("${metric-calculate.t-log-thread-pool.core-size}") Integer coreSize,
                                                             @Value("${metric-calculate.t-log-thread-pool.max-size}") Integer maxSize,
                                                             @Value("${metric-calculate.t-log-thread-pool.keep-alive-second}") Integer keepAliveSeconds,
                                                             @Value("${metric-calculate.t-log-thread-pool.queue-length}") Integer queueLength,
                                                             @Value("${metric-calculate.t-log-thread-pool.thread-name-prefix}") String threadNamePrefix) {
        log.info("指标计算TLog线程池初始化完成: 核心大小: {}, 最大大小: {}, 存活时间: {}秒, 队列大小: {}, 线程名前缀: {}", coreSize, maxSize, keepAliveSeconds, queueLength, threadNamePrefix);
        return new TLogThreadPoolTaskExecutor(coreSize, maxSize, keepAliveSeconds, queueLength, new NamedThreadFactory(threadNamePrefix, false), new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
