package com.yanggu.metric_calculate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.core2.util.QueryRequest;
import com.yanggu.metric_calculate.pojo.PutRequest;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetricCalculateService {

    @Autowired
    private MetricConfigService metricConfigService;

    @Autowired
    @Qualifier("queryComponent")
    private AccumulateBatchComponent2<QueryRequest> queryComponent;

    @Autowired
    @Qualifier("putComponent")
    private AccumulateBatchComponent2<PutRequest> putComponent;

    /**
     * 无状态-计算接口
     *
     * @param detail
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> noStateExecute(JSONObject detail) {
        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);

        //无状态计算派生指标
        return calcDerive(detail, dataWideTable, false);
    }

    /**
     * 有状态计算
     *
     * @param detail
     * @return
     */
    public List<DeriveMetricCalculateResult<Object>> stateExecute(JSONObject detail) {
        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        return calcDerive(detail, dataWideTable, true);
    }

    /**
     * 攒批查询
     *
     * @param input
     * @return
     */
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateExecuteAccumulateBatch(JSONObject input) {

        DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult =
                                        new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        ApiResponse<List<DeriveMetricCalculateResult>> apiResponse = new ApiResponse<>();
        //设置超时出咯
        deferredResult.onTimeout(() -> {
            apiResponse.setMessage("请求超时, 请重试");
            apiResponse.setStatus("500");
            deferredResult.setResult(apiResponse);
        });

        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(input);
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(apiResponse);
            return deferredResult;
        }

        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            QueryRequest queryRequest = getQueryRequest(input, deriveMetricCalculate);
            //进行攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture =
                    deriveMetricCalculate.noStateFutureExec(input, queryRequest.getQueryFuture());
            completableFutureList.add(completableFuture);
        }

        //所有查询完成后执行
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .thenAccept(temp -> {
                    List<DeriveMetricCalculateResult> deriveMetricCalculateResultList = new ArrayList<>();
                    for (CompletableFuture<DeriveMetricCalculateResult> completableFuture : completableFutureList) {
                        DeriveMetricCalculateResult deriveMetricCalculateResult;
                        try {
                            deriveMetricCalculateResult = completableFuture.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (deriveMetricCalculateResult != null) {
                            deriveMetricCalculateResultList.add(deriveMetricCalculateResult);
                        }
                    }
                    apiResponse.setData(deriveMetricCalculateResultList);
                    deferredResult.setResult(apiResponse);
                });

        return deferredResult;
    }

    /**
     * 攒批更新
     *
     * @param input
     * @return
     */
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> stateExecuteAccumulateBatch(JSONObject input) {
        DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> deferredResult =
                new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        //获取指标计算类
        MetricCalculate metricCalculate = getMetricCalculate(input);

        ApiResponse<List<DeriveMetricCalculateResult>> apiResponse = new ApiResponse<>();

        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            deferredResult.setResult(apiResponse);
            return deferredResult;
        }

        List<CompletableFuture<DeriveMetricCalculateResult>> completableFutureList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            //先执行前置过滤条件
            Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(input);
            if (Boolean.FALSE.equals(filter)) {
                continue;
            }
            QueryRequest queryRequest = getQueryRequest(input, deriveMetricCalculate);
            //攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<DeriveMetricCalculateResult> completableFuture = queryRequest.getQueryFuture()
                    .thenCompose(historyMetricCube -> {
                        //添加度量值
                        historyMetricCube = deriveMetricCalculate.addInput(input, historyMetricCube);
                        PutRequest putRequest = new PutRequest();
                        putRequest.setMetricCube(historyMetricCube);
                        putRequest.setInput(input);
                        putRequest.setResultFuture(new CompletableFuture<>());
                        //进行攒批更新
                        putComponent.add(putRequest);
                        return putRequest.getResultFuture();
                    });
            completableFutureList.add(completableFuture);
        }

        //当所有的更新都完成时, 进行输出
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .whenComplete((data, exception) -> {
                    List<DeriveMetricCalculateResult> collect = completableFutureList.stream()
                            .map(temp -> {
                                try {
                                    return temp.get();
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        //按照key进行排序
                        collect.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
                    }
                    apiResponse.setData(collect);
                    deferredResult.setResult(apiResponse);
                });
        return deferredResult;
    }

    private QueryRequest getQueryRequest(JSONObject input, DeriveMetricCalculate deriveMetricCalculate) {
        DimensionSet process = deriveMetricCalculate.getDimensionSetProcessor().process(input);
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setDimensionSet(process);
        queryRequest.setQueryFuture(new CompletableFuture<>());
        return queryRequest;
    }

    private List<DeriveMetricCalculateResult<Object>> calcDerive(JSONObject detail,
                                                                 MetricCalculate dataWideTable,
                                                                 boolean update) {
        //进行字段计算
        detail = dataWideTable.getParam(detail);
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }
        List<DeriveMetricCalculateResult<Object>> deriveList = new CopyOnWriteArrayList<>();
        JSONObject finalDetail = detail;
        deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
            DeriveMetricCalculateResult<Object> result;
            if (update) {
                result = deriveMetricCalculate.stateExec(finalDetail);
            } else {
                result = deriveMetricCalculate.noStateExec(finalDetail);
            }
            if (result != null) {
                deriveList.add(result);
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("派生指标计算后的数据: {}", JSONUtil.toJsonStr(deriveList));
        }
        //按照key进行排序
        if (CollUtil.isNotEmpty(deriveList)) {
            //按照指标id进行排序
            deriveList.sort(Comparator.comparingInt(temp -> Integer.parseInt(temp.getKey().split("_")[1])));
        }
        return deriveList;
    }

    private MetricCalculate getMetricCalculate(JSONObject detail) {
        Long tableId = detail.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId, 原始数据: " + JSONUtil.toJsonStr(detail));
        }
        return metricConfigService.getMetricCalculate(tableId);
    }

}
