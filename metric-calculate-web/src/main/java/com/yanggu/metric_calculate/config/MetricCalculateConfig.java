package com.yanggu.metric_calculate.config;

import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleRedisStore;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.core2.util.QueryRequest;
import com.yanggu.metric_calculate.pojo.PutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 指标配置类
 */
@Configuration
public class MetricCalculateConfig {

    /**
     * 派生指标中间存储配置
     *
     * @return
     */
    @Bean
    public DeriveMetricMiddleStore redisDeriveMetricMiddleStore() {
        DeriveMetricMiddleStore deriveMetricMiddleStore = new DeriveMetricMiddleRedisStore();
        deriveMetricMiddleStore.init();
        return deriveMetricMiddleStore;
    }

    /**
     * 攒批查询组件
     */
    @Bean
    public AccumulateBatchComponent2<QueryRequest> queryComponent(
                        DeriveMetricMiddleStore deriveMetricMiddleStore,
                        @Value("${metric-calculate.accumulate-batch-component.read.thread-num}") Integer threadNum,
                        @Value("${metric-calculate.accumulate-batch-component.read.limit}") Integer limit,
                        @Value("${metric-calculate.accumulate-batch-component.read.interval}") Integer interval) {
        Consumer<List<QueryRequest>> batchGetConsumer = queryRequests -> {
            List<DimensionSet> collect = queryRequests.stream()
                    .map(QueryRequest::getDimensionSet)
                    //去一下重
                    .distinct()
                    .collect(Collectors.toList());

            //批量查询
            Map<DimensionSet, MetricCube> map = deriveMetricMiddleStore.batchGet(collect);

            //批量查询完成后, 进行回调通知
            for (QueryRequest queryRequest : queryRequests) {
                MetricCube historyMetricCube = map.get(queryRequest.getDimensionSet());
                queryRequest.getQueryFuture().complete(historyMetricCube);
            }
        };
        return new AccumulateBatchComponent2<>("攒批读组件", threadNum, limit, interval, batchGetConsumer);
    }

    /**
     * 批量更新组件
     */
    @Bean
    public AccumulateBatchComponent2<PutRequest> putComponent(
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
            deriveMetricMiddleStore.batchUpdate(collect);
            //批量更新完成后, 进行回调通知
            for (PutRequest putRequest : putRequests) {
                MetricCube metricCube = putRequest.getMetricCube();
                CompletableFuture<DeriveMetricCalculateResult> resultFuture = putRequest.getResultFuture();
                resultFuture.complete(metricCube.query(putRequest.getInput()));
            }
        };
        return new AccumulateBatchComponent2<>("攒批写组件", threadNum, limit, interval, batchUpdateConsumer);
    }

}
