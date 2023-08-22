package com.yanggu.metric_calculate.config.config;

import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleRedisStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionProvider;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

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

}
