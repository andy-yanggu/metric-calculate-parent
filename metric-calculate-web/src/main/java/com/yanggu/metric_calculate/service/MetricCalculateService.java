package com.yanggu.metric_calculate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent;
import com.yanggu.metric_calculate.pojo.PutRequest;
import com.yanggu.metric_calculate.pojo.QueryRequest;
import com.yanggu.metric_calculate.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

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
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);

        //无状态计算派生指标
        return calcDerive(input, metricCalculate, false);
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

        Map<DimensionSet, DeriveMetricCalculate> map = new HashMap<>();

        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
            map.put(dimensionSet, deriveMetricCalculate);
        }

        List<DimensionSet> dimensionSetList = new ArrayList<>(map.keySet());

        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(dimensionSetList);
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        List<DeriveMetricCalculateResult<Object>> deriveMetricCalculateResultList = new ArrayList<>();
        for (DimensionSet dimensionSet : dimensionSetList) {
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            DeriveMetricCalculate deriveMetricCalculate = map.get(dimensionSet);
            DeriveMetricCalculateResult<Object> deriveMetricCalculateResult = deriveMetricCalculate.noStateExec(input, historyMetricCube);
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
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateCalculateAccumulateBatch(JSONObject input) {
        DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult = createDeferredResult(5000L);

        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(ApiResponse.success(null));
            return deferredResult;
        }

        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            QueryRequest queryRequest = getQueryRequest(detail, deriveMetricCalculate);
            //进行攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture = queryRequest.getQueryFuture()
                    .thenApply(historyMetricCube -> deriveMetricCalculate.noStateExec(input, historyMetricCube));
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
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);

        //计算派生指标
        return calcDerive(input, metricCalculate, true);
    }

    /**
     * 有状态计算(批读、批写)
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> stateCalculateBatch(JSONObject input) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);

        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }

        Map<DimensionSet, DeriveMetricCalculate> map = new HashMap<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            //执行前置过滤条件
            Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(input);
            if (Boolean.FALSE.equals(filter)) {
                continue;
            }

            //提取出维度字段
            DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
            map.put(dimensionSet, deriveMetricCalculate);
        }

        if (CollUtil.isEmpty(map)) {
            return Collections.emptyList();
        }

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
            historyMetricCube = deriveMetricCalculate.addInput(input, historyMetricCube);
            updateMetricCubeList.add(historyMetricCube);
            DeriveMetricCalculateResult query = historyMetricCube.query(input);
            if (query != null) {
                resultList.add(query);
            }
        }

        deriveMetricMiddleStore.batchUpdate(updateMetricCubeList);
        return resultList;
    }

    /**
     * 攒批更新
     *
     * @param input
     * @return
     */
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> stateCalculateAccumulateBatch(JSONObject input) {
        DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult = createDeferredResult(5000L);

        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(ApiResponse.success(null));
            return deferredResult;
        }

        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            //先执行前置过滤条件
            Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(detail);
            if (Boolean.FALSE.equals(filter)) {
                continue;
            }
            QueryRequest queryRequest = getQueryRequest(detail, deriveMetricCalculate);
            //攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture = queryRequest.getQueryFuture()
                    .thenCompose(historyMetricCube -> {
                        //添加度量值
                        historyMetricCube = deriveMetricCalculate.addInput(detail, historyMetricCube);
                        PutRequest putRequest = new PutRequest();
                        putRequest.setMetricCube(historyMetricCube);
                        putRequest.setInput(detail);
                        putRequest.setResultFuture(new CompletableFuture<>());
                        //进行攒批更新
                        putComponent.add(putRequest);
                        return putRequest.getResultFuture();
                    });
            completableFutureList.add(completableFuture);
        }

        //当所有的更新都完成时, 进行输出
        setDeferredResult(deferredResult, completableFutureList);
        return deferredResult;
    }

    private DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> createDeferredResult(Long duration) {
        DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult =
                new DeferredResult<>(TimeUnit.MILLISECONDS.toMillis(duration));
        //设置超时处理
        deferredResult.onTimeout(() -> {
            ApiResponse<List<DeriveMetricCalculateResult>> apiResponse = new ApiResponse<>();
            apiResponse.setMessage("请求超时, 请重试");
            apiResponse.setStatus("500");
            deferredResult.setResult(apiResponse);
        });
        return deferredResult;
    }

    private void setDeferredResult(DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult,
                                   List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList) {
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenAccept(tempObj -> {
                    List<DeriveMetricCalculateResult> collect = new ArrayList<>();
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
                    deferredResult.setResult(ApiResponse.success(collect));
                });
    }

    private QueryRequest getQueryRequest(JSONObject input, DeriveMetricCalculate deriveMetricCalculate) {
        DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setDimensionSet(dimensionSet);
        queryRequest.setQueryFuture(new CompletableFuture<>());
        return queryRequest;
    }

    private List<DeriveMetricCalculateResult<Object>> calcDerive(JSONObject input,
                                                                 MetricCalculate metricCalculate,
                                                                 boolean update) {
        //进行字段计算
        JSONObject detail = metricCalculate.getParam(input);
        log.info("输入明细数据: {}, 计算后的输入明细数据: {}, 是否更新: {}", JSONUtil.toJsonStr(input), JSONUtil.toJsonStr(detail), update);
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }
        List<DeriveMetricCalculateResult<Object>> deriveList = new CopyOnWriteArrayList<>();
        deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
            DeriveMetricCalculateResult<Object> result;
            if (update) {
                result = deriveMetricCalculate.stateExec(detail);
            } else {
                result = deriveMetricCalculate.noStateExec(detail);
            }
            if (result != null) {
                deriveList.add(result);
            }
        });
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
