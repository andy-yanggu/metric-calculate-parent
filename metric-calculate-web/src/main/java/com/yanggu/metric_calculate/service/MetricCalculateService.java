package com.yanggu.metric_calculate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.pojo.PutRequest;
import com.yanggu.metric_calculate.pojo.QueryRequest;
import com.yanggu.metric_calculate.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    @Qualifier("queryComponent")
    private AccumulateBatchComponent2<QueryRequest> queryComponent;

    @Autowired
    @Qualifier("putComponent")
    private AccumulateBatchComponent2<PutRequest> putComponent;

    /**
     * 无状态-计算接口
     *
     * @param input
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> noStateExecute(JSONObject input) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);

        //无状态计算派生指标
        return calcDerive(input, metricCalculate, false);
    }

    /**
     * 有状态计算
     *
     * @param detail
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> stateExecute(JSONObject detail) {
        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(detail);

        //计算派生指标
        return calcDerive(detail, metricCalculate, true);
    }

    /**
     * 攒批查询
     *
     * @param input
     * @return
     */
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateExecuteAccumulateBatch(JSONObject input) {
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
            CompletableFuture<DeriveMetricCalculateResult> completableFuture =
                    deriveMetricCalculate.noStateFutureExec(detail, queryRequest.getQueryFuture());
            completableFutureList.add(completableFuture);
        }

        //所有查询完成后执行
        setDeferredResult(deferredResult, completableFutureList);
        return deferredResult;
    }

    /**
     * 攒批更新
     *
     * @param input
     * @return
     */
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> stateExecuteAccumulateBatch(JSONObject input) {
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
        DimensionSet process = deriveMetricCalculate.getDimensionSetProcessor().process(input);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setDimensionSet(process);
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
