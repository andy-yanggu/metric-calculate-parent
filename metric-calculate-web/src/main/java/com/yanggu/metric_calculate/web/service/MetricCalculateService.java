package com.yanggu.metric_calculate.web.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent;
import com.yanggu.metric_calculate.web.pojo.PutRequest;
import com.yanggu.metric_calculate.web.pojo.QueryRequest;
import com.yanggu.metric_calculate.web.pojo.vo.Result;
import com.yanggu.metric_calculate.web.util.TLogThreadPoolTaskExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.yanggu.metric_calculate.web.enums.ResultCode.TIME_OUT;

/**
 * 指标计算Service
 */
@Slf4j
@Service
public class MetricCalculateService {

    @Autowired
    private MetricConfigDataService metricConfigDataService;

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Autowired
    @Qualifier("queryComponent")
    private AccumulateBatchComponent<QueryRequest> queryComponent;

    @Autowired
    @Qualifier("putComponent")
    private AccumulateBatchComponent<PutRequest> putComponent;

    @Autowired
    @Qualifier("tLogThreadPoolExecutor")
    private TLogThreadPoolTaskExecutor threadPoolExecutor;

    /**
     * 无状态(多线程)
     * <p>QPS低使用该接口</p>
     * <p>外部数据多线程读取</p>
     * <p>吞吐量低, 响应时间短</p>
     *
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> noStateCalculateThread(JSONObject input) {
        //无状态计算派生指标
        return calcDerive(input, false);
    }

    /**
     * 无状态(批处理)
     * <p>QPS中使用该接口</p>
     * <p>外部数据进行批量读取</p>
     * <p>吞吐量中, 响应时间中</p>
     *
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> noStateCalculateBatch(JSONObject input) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        log.info("输入明细数据: {}, 计算后的输入明细数据: {}", JSONUtil.toJsonStr(input), JSONUtil.toJsonStr(detail));
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }

        Map<DimensionSet, DeriveMetricCalculate> map = getDimensionSetDeriveMetricCalculateMap(deriveMetricCalculateList, input);

        List<DimensionSet> dimensionSetList = new ArrayList<>(map.keySet());

        //批查询指标数据
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(dimensionSetList);
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        List<DeriveMetricCalculateResult<Object>> deriveMetricCalculateResultList = new ArrayList<>();
        for (DimensionSet dimensionSet : dimensionSetList) {
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            DeriveMetricCalculate deriveMetricCalculate = map.get(dimensionSet);
            DeriveMetricCalculateResult<Object> deriveMetricCalculateResult =
                    deriveMetricCalculate.noStateExec(input, historyMetricCube, dimensionSet);
            if (deriveMetricCalculateResult != null) {
                deriveMetricCalculateResultList.add(deriveMetricCalculateResult);
            }
        }
        return deriveMetricCalculateResultList;
    }

    /**
     * 无状态(内存攒批读)
     * <p>QPS高使用该接口</p>
     * <p>使用内存队列进行攒批</p>
     * <p>吞吐量大, 响应时间长</p>
     *
     * @param input
     * @return
     */
    public DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> noStateCalculateAccumulateBatch(JSONObject input) {
        DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> deferredResult = createDeferredResult(5000L);

        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(Result.ok(null));
            return deferredResult;
        }

        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            QueryRequest queryRequest = getQueryRequest(detail, deriveMetricCalculate);
            //进行攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture = queryRequest.getQueryFuture()
                    .thenApply(historyMetricCube -> {
                        return deriveMetricCalculate.noStateExec(input, historyMetricCube, queryRequest.getDimensionSet());
                    });
            completableFutureList.add(completableFuture);
        }

        //所有查询完成后执行
        setDeferredResult(deferredResult, completableFutureList);
        return deferredResult;
    }

    /**
     * 有状态计算(多线程)
     *
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> stateCalculateThread(JSONObject input) {
        //计算派生指标
        return calcDerive(input, true);
    }

    /**
     * 有状态计算(批读、批写)
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> stateCalculateBatch(JSONObject input) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }

        Map<DimensionSet, DeriveMetricCalculate> map = getDimensionSetDeriveMetricCalculateMap(deriveMetricCalculateList, detail);

        List<DimensionSet> dimensionSetList = new ArrayList<>(map.keySet());
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(dimensionSetList);
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        List<MetricCube> updateMetricCubeList = new ArrayList<>();
        List<DeriveMetricCalculateResult<Object>> resultList = new ArrayList<>();
        for (DimensionSet dimensionSet : dimensionSetList) {
            DeriveMetricCalculate deriveMetricCalculate = map.get(dimensionSet);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            //为false的消费逻辑
            Consumer<MetricCube> filterFalseConsumer = metricCube -> {
                DeriveMetricCalculateResult query = deriveMetricCalculate.query(metricCube, input);
                if (query != null) {
                    resultList.add(query);
                }
            };
            //为true的消费逻辑
            Consumer<MetricCube> filterTrueConsumer = newMetricCube -> {
                if (!newMetricCube.isEmpty()) {
                    updateMetricCubeList.add(newMetricCube);
                    DeriveMetricCalculateResult query = newMetricCube.query();
                    if (query != null) {
                        resultList.add(query);
                    }
                }
            };
            //执行有状态计算
            deriveMetricCalculate.stateExec(detail, historyMetricCube, dimensionSet, filterFalseConsumer, filterTrueConsumer);
        }

        deriveMetricMiddleStore.batchUpdate(updateMetricCubeList);
        return resultList;
    }

    /**
     * 攒批查询和攒批更新
     *
     * @param input
     * @return
     */
    public DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> stateCalculateAccumulateBatch(JSONObject input) {
        DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> deferredResult = createDeferredResult(5000L);

        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(Result.ok(null));
            return deferredResult;
        }

        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            QueryRequest queryRequest = getQueryRequest(detail, deriveMetricCalculate);
            //攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture = queryRequest.getQueryFuture()
                    .thenCompose(historyMetricCube -> {
                        CompletableFuture<DeriveMetricCalculateResult> future = new CompletableFuture<>();
                        //为false的消费逻辑
                        Consumer<MetricCube> filterFalseConsumer = metricCube -> {
                            DeriveMetricCalculateResult result = deriveMetricCalculate.query(metricCube, input);
                            future.complete(result);
                        };
                        //为true的消费逻辑
                        Consumer<MetricCube> filterTrueConsumer = newMetricCube -> {
                            if (newMetricCube.isEmpty()) {
                                future.complete(null);
                                return;
                            }
                            PutRequest putRequest = new PutRequest();
                            putRequest.setMetricCube(newMetricCube);
                            putRequest.setInput(detail);
                            putRequest.setResultFuture(future);
                            //进行攒批更新
                            putComponent.add(putRequest);
                        };
                        //执行有状态计算
                        deriveMetricCalculate.stateExec(detail, historyMetricCube, queryRequest.getDimensionSet(), filterFalseConsumer, filterTrueConsumer);
                        return future;
                    });
            completableFutureList.add(completableFuture);
        }

        if (CollUtil.isEmpty(completableFutureList)) {
            return deferredResult;
        }

        //当所有的更新都完成时, 进行输出
        setDeferredResult(deferredResult, completableFutureList);
        return deferredResult;
    }

    private Map<DimensionSet, DeriveMetricCalculate> getDimensionSetDeriveMetricCalculateMap(List<DeriveMetricCalculate> deriveMetricCalculateList, JSONObject detail) {
        Map<DimensionSet, DeriveMetricCalculate> map = new HashMap<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            //提取出维度字段
            DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(detail);
            map.put(dimensionSet, deriveMetricCalculate);
        }
        return map;
    }

    private DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> createDeferredResult(Long duration) {
        DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> deferredResult =
                new DeferredResult<>(TimeUnit.MILLISECONDS.toMillis(duration));
        //设置超时处理
        deferredResult.onTimeout(() -> {
            Result<List<DeriveMetricCalculateResult<Object>>> result = Result.create(false, null, TIME_OUT);
            deferredResult.setResult(result);
        });
        return deferredResult;
    }

    private void setDeferredResult(DeferredResult<Result<List<DeriveMetricCalculateResult<Object>>>> deferredResult,
                                   List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList) {
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenAccept(tempObj -> {
                    List<DeriveMetricCalculateResult<Object>> collect = new ArrayList<>();
                    for (CompletableFuture<DeriveMetricCalculateResult> completableFuture : completableFutureList) {
                        DeriveMetricCalculateResult deriveMetricCalculateResult;
                        try {
                            deriveMetricCalculateResult = completableFuture.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (deriveMetricCalculateResult != null) {
                            collect.add(deriveMetricCalculateResult);
                        }
                    }
                    if (CollUtil.isNotEmpty(collect)) {
                        //按照key进行排序
                        collect.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
                    }
                    deferredResult.setResult(Result.ok(collect));
                });
    }

    private QueryRequest getQueryRequest(JSONObject detail, DeriveMetricCalculate deriveMetricCalculate) {
        DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(detail);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setDimensionSet(dimensionSet);
        queryRequest.setQueryFuture(new CompletableFuture<>());
        return queryRequest;
    }

    private List<DeriveMetricCalculateResult<Object>> calcDerive(JSONObject input,
                                                                 boolean update) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        log.info("输入明细数据: {}, 计算后的输入明细数据: {}, 是否更新: {}", JSONUtil.toJsonStr(input), JSONUtil.toJsonStr(detail), update);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }
        List<DeriveMetricCalculateResult<Object>> deriveList = new CopyOnWriteArrayList<>();
        CompletableFuture[] array = deriveMetricCalculateList.stream()
            .map(deriveMetricCalculate ->
                CompletableFuture.runAsync(() -> {
                    DeriveMetricCalculateResult<Object> temp;
                    if (update) {
                        temp = deriveMetricCalculate.stateExec(detail);
                    } else {
                        temp = deriveMetricCalculate.noStateExec(detail);
                    }
                    if (temp != null) {
                        deriveList.add(temp);
                    }
                }, threadPoolExecutor)
            )
            .toArray(CompletableFuture[]::new);

        //等待所有线程完成
        CompletableFuture.allOf(array).join();

        log.info("输入的明细数据: {}, 派生指标计算后的数据: {}", JSONUtil.toJsonStr(detail), JSONUtil.toJsonStr(deriveList));
        //按照key进行排序
        if (CollUtil.isNotEmpty(deriveList)) {
            //按照指标key进行排序
            deriveList.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
        }
        return deriveList;
    }

    private MetricCalculate getMetricCalculate(JSONObject input) {
        Long tableId = input.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId, 原始数据: " + JSONUtil.toJsonStr(input));
        }
        return metricConfigDataService.getMetricCalculate(tableId);
    }

}
