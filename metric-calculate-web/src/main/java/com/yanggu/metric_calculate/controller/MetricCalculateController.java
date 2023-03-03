package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.magiccube.MagicCubeClient;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleRedisStore;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.table.Table;
import com.yanggu.metric_calculate.core.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import com.yanggu.metric_calculate.pojo.PutRequest;
import com.yanggu.metric_calculate.pojo.QueryRequest;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

@Slf4j
@Api(tags = "指标计算接口")
@RestController
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    private final Map<Long, MetricCalculate<JSONObject>> metricMap = new ConcurrentHashMap<>();

    private final Striped<Lock> lockStriped = Striped.lazyWeakLock(20);

    @Autowired
    private MagicCubeClient magiccubeClient;

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
        DeriveMetricMiddleHashMapStore deriveMetricMiddleHashMapStore = new DeriveMetricMiddleHashMapStore();
        deriveMetricMiddleHashMapStore.init();

        //批量查询组件
        queryComponent = new AccumulateBatchComponent2<>("赞批读组件", RuntimeUtil.getProcessorCount(), 20, 2000,
                queryRequests -> {
                    List<MetricCube> collect = queryRequests.stream()
                            .map(QueryRequest::getMetricCube)
                            .collect(Collectors.toList());

                    //批量查询
                    Map<DimensionSet, MetricCube> map = deriveMetricMiddleHashMapStore.batchGet(collect);

                    //批量查询完成后, 进行回调通知
                    for (QueryRequest queryRequest : queryRequests) {
                        MetricCube historyMetricCube = map.get(queryRequest.getMetricCube().getDimensionSet());
                        MetricCube<Table, Long, ?, ?> newMetricCube = queryRequest.getMetricCube();
                        if (historyMetricCube == null) {
                            historyMetricCube = newMetricCube;
                        } else {
                            historyMetricCube.merge(newMetricCube);
                            //删除过期数据
                            historyMetricCube.eliminateExpiredData();
                        }
                        queryRequest.getQueryFuture().complete(historyMetricCube);
                    }
                });

        //批量更新组件
        putComponent = new AccumulateBatchComponent2<>("攒批写组件", RuntimeUtil.getProcessorCount(), 20, 2000,
                putRequests -> {
                    List<MetricCube> collect = putRequests.stream()
                            .map(PutRequest::getMetricCube)
                            .collect(Collectors.toList());

                    //批量更新
                    deriveMetricMiddleHashMapStore.batchUpdate(collect);
                    //批量更新完成后, 进行回调通知
                    for (PutRequest putRequest : putRequests) {
                        CompletableFuture<List<DeriveMetricCalculateResult>> completableFuture = putRequest.getResultFuture();
                        completableFuture.complete(putRequest.getDeriveMetricCalculate().query(putRequest.getMetricCube()));
                    }
                });
    }

    //定期刷新指标元数据
    @Scheduled(fixedRate = 1000 * 60)
    public void scheduledRefreshMetric() {
        queryMetric();
    }

    @ApiOperation("刷新指标接口")
    @GetMapping("/manualRefreshMetric")
    public ApiResponse<?> manualRefreshMetric() {
        queryMetric();
        return ApiResponse.success();
    }

    @ApiOperation("有状态-计算接口")
    @PostMapping("/state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult>> stateExecute(@ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        //获取指标计算类
        MetricCalculate<JSONObject> dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        List<DeriveMetricCalculateResult> deriveMetricCalculateResultList = calcDeriveState(detail, dataWideTable);

        ApiResponse<List<DeriveMetricCalculateResult>> response = new ApiResponse<>();
        response.setData(deriveMetricCalculateResultList);

        return response;
    }

    @ApiOperation("有状态-计算接口（攒批查询和攒批更新）")
    @PostMapping("/state-calculate-accumulate-batch")
    public DeferredResult<List<DeriveMetricCalculateResult>> stateExecuteAccumulateBatch(
            @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {

        DeferredResult<List<DeriveMetricCalculateResult>> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        //获取指标计算类
        MetricCalculate<JSONObject> dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        List<Tuple> tupleList = new ArrayList<>();
        List<DeriveMetricCalculate<JSONObject, ?>> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        deriveMetricCalculateList.forEach(tempDerive -> {
            MetricCube<Table, Long, ?, ?> exec = tempDerive.exec(detail);
            if (exec != null) {
                tupleList.add(new Tuple(tempDerive, exec));
            }
        });
        if (CollUtil.isEmpty(tupleList)) {
            return deferredResult;
        }

        //进行攒批查询
        List<CompletableFuture<List<DeriveMetricCalculateResult>>> resultFutureList = new ArrayList<>();
        for (Tuple tuple : tupleList) {
            DeriveMetricCalculate<JSONObject, ?> deriveMetricCalculate = tuple.get(0);
            MetricCube<Table, Long, ?, ?> metricCube = tuple.get(1);
            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setMetricCube(metricCube);
            queryRequest.setQueryFuture(new CompletableFuture<>());
            //进行攒批查询
            queryComponent.add(queryRequest);
            CompletableFuture<List<DeriveMetricCalculateResult>> resultFuture = queryRequest.getQueryFuture()
                    .thenCompose(v1 -> {
                        PutRequest putRequest = new PutRequest();
                        putRequest.setMetricCube(v1);
                        putRequest.setDeriveMetricCalculate(deriveMetricCalculate);
                        putRequest.setResultFuture(new CompletableFuture<>());
                        //进行攒批更新
                        putComponent.add(putRequest);
                        return putRequest.getResultFuture();
                    });
            resultFutureList.add(resultFuture);
        }

        //当所有的更新都完成时, 进行输出
        CompletableFuture.allOf(resultFutureList.toArray(new CompletableFuture[0]))
                .whenComplete((data, exception) -> {
                    List<DeriveMetricCalculateResult> collect = resultFutureList.stream()
                            .flatMap(temp -> {
                                try {
                                    return temp.get().stream();
                                } catch (Throwable e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(collect)) {
                        //按照key进行排序
                        collect.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
                    }
                    deferredResult.setResult(collect);
                });
        return deferredResult;
    }

    @ApiOperation("无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult>> noStateExecute(@ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        //获取指标计算类
        MetricCalculate<JSONObject> dataWideTable = getMetricCalculate(detail);

        //无状态计算派生指标
        List<DeriveMetricCalculateResult> deriveMetricCalculateResultList = calcDeriveNoState(detail, dataWideTable);

        ApiResponse<List<DeriveMetricCalculateResult>> response = new ApiResponse<>();
        response.setData(deriveMetricCalculateResultList);

        return response;
    }

    private MetricCalculate<JSONObject> getMetricCalculate(JSONObject detail) {
        Long tableId = detail.getLong("tableId");
        if (tableId == null) {
            throw new RuntimeException("没有传入tableId");
        }
        MetricCalculate<JSONObject> dataWideTable = metricMap.get(tableId);
        if (dataWideTable == null) {
            dataWideTable = buildMetric(tableId);
        }
        return dataWideTable;
    }

    private List<DeriveMetricCalculateResult> calcDeriveState(JSONObject detail,
                                                              MetricCalculate<JSONObject> dataWideTable) {
        List<DeriveMetricCalculate<JSONObject, ?>> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }
        List<DeriveMetricCalculateResult> deriveList = new CopyOnWriteArrayList<>();
        deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
            List<DeriveMetricCalculateResult> tempList = deriveMetricCalculate.updateMetricCube(detail);
            if (CollUtil.isNotEmpty(tempList)) {
                deriveList.addAll(tempList);
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("派生指标计算后的数据: {}", JSONUtil.toJsonStr(deriveList));
        }
        if (CollUtil.isNotEmpty(deriveList)) {
            deriveList.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
        }
        return deriveList;
    }

    private List<DeriveMetricCalculateResult> calcDeriveNoState(JSONObject detail,
                                                                MetricCalculate<JSONObject> dataWideTable) {
        List<DeriveMetricCalculate<JSONObject, ?>> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return Collections.emptyList();
        }
        List<DeriveMetricCalculateResult> deriveDataList = new CopyOnWriteArrayList<>();
        deriveMetricCalculateList.parallelStream().forEach(deriveMetricCalculate -> {
            List<DeriveMetricCalculateResult> tempList = deriveMetricCalculate.noStateCalc(detail);
            if (CollUtil.isNotEmpty(tempList)) {
                deriveDataList.addAll(tempList);
            }
        });
        if (log.isDebugEnabled()) {
            log.debug("派生指标计算后的数据: {}", JSONUtil.toJsonStr(deriveDataList));
        }
        if (CollUtil.isNotEmpty(deriveDataList)) {
            deriveDataList.sort(Comparator.comparing(DeriveMetricCalculateResult::getKey));
        }
        return deriveDataList;
    }

    /**
     * 从数据库加载指标定义
     */
    private void queryMetric() {
        log.info("load metric from DB");
        if (CollUtil.isEmpty(metricMap)) {
            return;
        }
        Set<Long> tableIdSet = metricMap.keySet();
        tableIdSet.parallelStream().forEach(this::buildMetric);
    }

    private MetricCalculate<JSONObject> buildMetric(Long tableId) {
        Lock lock = lockStriped.get(tableId);
        lock.lock();
        try {
            //根据明细宽表id查询指标数据和宽表数据
            DataDetailsWideTable tableData = magiccubeClient.getTableAndMetricByTableId(tableId);
            if (tableData == null || tableData.getId() == null) {
                log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
                throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
            }
            //使用redis作为中间存储
            DeriveMetricMiddleRedisStore deriveMetricMiddleRedisStore = new DeriveMetricMiddleRedisStore();
            deriveMetricMiddleRedisStore.init();
            MetricCalculate<JSONObject> metricCalculate = MetricUtil.initMetricCalculate(tableData);
            metricMap.put(tableId, metricCalculate);
            return metricCalculate;
        } finally {
            lock.unlock();
        }
    }

}
