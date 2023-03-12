package com.yanggu.metric_calculate.config;

import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleRedisStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeriveMetricMiddleStoreConfig {

    @Bean
    DeriveMetricMiddleStore redisDeriveMetricMiddleStore() {
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleRedisStore();
        deriveMetricMiddleStore.init();
        return deriveMetricMiddleStore;
    }

}
