package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.pojo.PutRequest;
import com.yanggu.metric_calculate.pojo.QueryRequest;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "指标计算接口")
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Autowired
    private MetricConfigController metricConfigController;

    /**
     * 攒批查询
     */
    private AccumulateBatchComponent2<QueryRequest> queryComponent;

    /**
     * 攒批更新
     */
    private AccumulateBatchComponent2<PutRequest> putComponent;

    @PostConstruct
    public void init() {
        //批量查询组件
        //queryComponent = new AccumulateBatchComponent2<>("攒批读组件", RuntimeUtil.getProcessorCount(), 20, 2000,
        //        queryRequests -> {
        //            List<MetricCube> collect = queryRequests.stream()
        //                    .map(QueryRequest::getMetricCube)
        //                    .collect(Collectors.toList());
        //
        //            //TODO 需要考虑请求合并
        //            //批量查询
        //            Map<DimensionSet, MetricCube> map = deriveMetricMiddleStore.batchGet(collect);
        //
        //            //批量查询完成后, 进行回调通知
        //            for (QueryRequest queryRequest : queryRequests) {
        //                MetricCube historyMetricCube = map.get(queryRequest.getMetricCube().getDimensionSet());
        //                MetricCube<Table, Long, ?, ?> newMetricCube = queryRequest.getMetricCube();
        //                if (historyMetricCube == null) {
        //                    historyMetricCube = newMetricCube;
        //                } else {
        //                    historyMetricCube.merge(newMetricCube);
        //                    //删除过期数据
        //                    historyMetricCube.eliminateExpiredData();
        //                }
        //                queryRequest.getQueryFuture().complete(historyMetricCube);
        //            }
        //        });
        //
        ////批量更新组件
        //putComponent = new AccumulateBatchComponent2<>("攒批写组件", RuntimeUtil.getProcessorCount(), 20, 2000,
        //        putRequests -> {
        //            List<MetricCube> collect = putRequests.stream()
        //                    .map(PutRequest::getMetricCube)
        //                    .collect(Collectors.toList());
        //
        //            //TODO 需要考虑请求合并
        //            //Map<String, Optional<MetricCube>> collect1 = collect.stream().collect(Collectors.groupingBy(KeyReferable::getRealKey,
        //                    //Collectors.reducing((metricCube, metricCube2) -> (MetricCube) metricCube.merge(metricCube2))));
        //
        //            //批量更新
        //            deriveMetricMiddleStore.batchUpdate(collect);
        //            //批量更新完成后, 进行回调通知
        //            for (PutRequest putRequest : putRequests) {
        //                CompletableFuture<List<DeriveMetricCalculateResult>> completableFuture = putRequest.getResultFuture();
        //                completableFuture.complete(putRequest.getDeriveMetricCalculate().query(putRequest.getMetricCube()));
        //            }
        //        });
    }

    @ApiOperation("有状态-计算接口")
    @PostMapping("/state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> stateExecute(@ApiParam("明细宽表数据") @RequestBody JSONObject detail) {

        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        List<DeriveMetricCalculateResult<Object>> deriveMetricCalculateResultList = calcDerive(detail, dataWideTable, true);

        //设置返回值
        ApiResponse<List<DeriveMetricCalculateResult<Object>>> response = new ApiResponse<>();
        response.setData(deriveMetricCalculateResultList);

        return response;
    }

    @ApiOperation("有状态-计算接口（攒批查询和攒批更新）")
    @PostMapping("/state-calculate-accumulate-batch")
    public DeferredResult<List<DeriveMetricCalculateResult>> stateExecuteAccumulateBatch(
            @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {

        DeferredResult<List<DeriveMetricCalculateResult>> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        List<Tuple> tupleList = new ArrayList<>();
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        //deriveMetricCalculateList.forEach(tempDerive -> {
        //    MetricCube<Table, Long, ?, ?> exec = tempDerive.exec(detail);
        //    if (exec != null) {
        //        tupleList.add(new Tuple(tempDerive, exec));
        //    }
        //});
        //if (CollUtil.isEmpty(tupleList)) {
        //    return deferredResult;
        //}
        //
        ////进行攒批查询
        //List<CompletableFuture<List<DeriveMetricCalculateResult>>> resultFutureList = new ArrayList<>();
        //for (Tuple tuple : tupleList) {
        //    DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate = tuple.get(0);
        //    MetricCube<Table, Long, ?, ?> metricCube = tuple.get(1);
        //    QueryRequest queryRequest = new QueryRequest();
        //    queryRequest.setMetricCube(metricCube);
        //    queryRequest.setQueryFuture(new CompletableFuture<>());
        //    //进行攒批查询
        //    queryComponent.add(queryRequest);
        //    CompletableFuture<List<DeriveMetricCalculateResult>> resultFuture = queryRequest.getQueryFuture()
        //            .thenCompose(v1 -> {
        //                PutRequest putRequest = new PutRequest();
        //                putRequest.setMetricCube(v1);
        //                putRequest.setDeriveMetricCalculate(deriveMetricCalculate);
        //                putRequest.setResultFuture(new CompletableFuture<>());
        //                //进行攒批更新
        //                putComponent.add(putRequest);
        //                return putRequest.getResultFuture();
        //            });
        //    resultFutureList.add(resultFuture);
        //}
        //
        ////当所有的更新都完成时, 进行输出
        //CompletableFuture.allOf(resultFutureList.toArray(new CompletableFuture[0]))
        //        .whenComplete((data, exception) -> {
        //            List<DeriveMetricCalculateResult> collect = resultFutureList.stream()
        //                    .flatMap(temp -> {
        //                        try {
        //                            return temp.get().stream();
        //                        } catch (Throwable e) {
        //                            throw new RuntimeException(e);
        //                        }
        //                    })
        //                    .collect(Collectors.toList());
        //            if (CollUtil.isNotEmpty(collect)) {
        //                //按照key进行排序
        //                collect.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
        //            }
        //            deferredResult.setResult(collect);
        //        });
        return deferredResult;
    }

    @ApiOperation("无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> noStateExecute(@ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);

        //无状态计算派生指标
        List<DeriveMetricCalculateResult<Object>> deriveMetricCalculateResultList = calcDerive(detail, dataWideTable, false);

        ApiResponse<List<DeriveMetricCalculateResult<Object>>> response = new ApiResponse<>();
        response.setData(deriveMetricCalculateResultList);

        return response;
    }

    @ApiOperation("无状态-计算接口（攒批查询）")
    @PostMapping("/no-state-calculate-accumulate-batch")
    public ApiResponse<List<DeriveMetricCalculateResult>> noStateExecuteAccumulateBatch(
            @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        //获取指标计算类
        MetricCalculate dataWideTable = getMetricCalculate(detail);
        ApiResponse<List<DeriveMetricCalculateResult>> apiResponse = new ApiResponse<>();
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return apiResponse;
        }

        //List<MetricCube> collect = deriveMetricCalculateList.parallelStream()
        //        .map(tempDerive -> tempDerive.getQueryMetricCube(detail))
        //        .collect(Collectors.toList());
        return null;
    }

    /**
     * 全量铺底接口, 计算宽表下的所有指标
     */
    @ApiOperation("全量铺底（计算所有派生指标数据）")
    @PostMapping("/full-update-derive")
    public ApiResponse<Object> fullUpdate(@RequestBody List<JSONObject> dataList) {
        MetricCalculate metricCalculate = getMetricCalculate(dataList.get(0));

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return apiResponse;
        }
        Set<DimensionSet> dimensionSets = new HashSet<>();
        List<Tuple> tupleList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            for (JSONObject input : dataList) {
                Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(input);
                if (Boolean.TRUE.equals(filter)) {
                    DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
                    dimensionSets.add(dimensionSet);
                    Tuple tuple = new Tuple(deriveMetricCalculate, input, dimensionSet);
                    tupleList.add(tuple);
                }
            }
        }

        if (CollUtil.isEmpty(dimensionSets)) {
            return apiResponse;
        }

        //批量读取数据
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(new ArrayList<>(dimensionSets));

        for (Tuple tuple : tupleList) {
            DeriveMetricCalculate deriveMetricCalculate = tuple.get(0);
            JSONObject input = tuple.get(1);
            DimensionSet dimensionSet = tuple.get(2);
            MetricCube metricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            if (metricCube == null) {
                metricCube = deriveMetricCalculate.createMetricCube(dimensionSet);
            }
            //TODO 缺少了删除数据逻辑
            metricCube.getTable().put(input);
        }

        //批量更新
        deriveMetricMiddleStore.batchUpdate(new ArrayList<>(dimensionSetMetricCubeMap.values()));

        return apiResponse;
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
        return metricConfigController.getMetricCalculate(tableId);
    }

}
