package com.yanggu.metric_calculate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.util.concurrent.Striped;
import com.yanggu.metric_calculate.client.magiccube.MagicCubeClient;
import com.yanggu.metric_calculate.core2.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.util.AccumulateBatchComponent2;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.pojo.PutRequest;
import com.yanggu.metric_calculate.pojo.QueryRequest;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

@Slf4j
@Api(tags = "指标计算接口")
@RestController
@RequestMapping("/metric-calculate")
public class MetricCalculateController implements ApplicationRunner {

    private final Map<Long, MetricCalculate> metricMap = new ConcurrentHashMap<>();

    private final Striped<ReadWriteLock> readWriteLockStriped = Striped.lazyWeakReadWriteLock(20);

    @Autowired
    private MagicCubeClient magiccubeClient;

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

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

    private List<DeriveMetricCalculateResult<Object>> calcDerive(JSONObject detail,
                                                                 MetricCalculate dataWideTable,
                                                                 boolean update) {
        List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
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
        ReadWriteLock readWriteLock = readWriteLockStriped.get(tableId);
        Lock readLock = readWriteLock.readLock();

        //先上读锁
        readLock.lock();
        MetricCalculate dataWideTable;
        try {
            dataWideTable = metricMap.get(tableId);
            //如果缓存中存在直接return
            if (dataWideTable != null) {
                return dataWideTable;
            }
        } finally {
            //释放读锁
            readLock.unlock();
        }
        Lock writeLock = readWriteLock.writeLock();
        //如果缓存中不存在上写锁
        writeLock.lock();
        try {
            //double check防止多次读取数据
            dataWideTable = metricMap.get(tableId);
            if (dataWideTable == null) {
                dataWideTable = buildMetric(tableId);
                metricMap.put(tableId, dataWideTable);
            }
        } finally {
            //释放写锁
            writeLock.unlock();
        }
        return dataWideTable;
    }

    private MetricCalculate buildMetric(Long tableId) {
        //根据明细宽表id查询指标数据和宽表数据
        DataDetailsWideTable tableData = magiccubeClient.getTableAndMetricByTableId(tableId);
        if (tableData == null || tableData.getId() == null) {
            log.error("指标中心没有配置明细宽表, 明细宽表的id: {}", tableId);
            throw new RuntimeException("指标中心没有配置明细宽表, 明细宽表的id: " + tableId);
        }
        return MetricUtil.initMetricCalculate(tableData);
    }

    /**
     * 从数据库加载指标定义
     */
    private void queryMetric() {
        log.info("load metric from DB");
        //获取所有宽表id
        List<Long> allTableId = magiccubeClient.getAllTableId();
        allTableId.parallelStream().forEach(tempTableId -> {
            ReadWriteLock readWriteLock = readWriteLockStriped.get(tempTableId);
            Lock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            try {
                MetricCalculate metricCalculate = buildMetric(tempTableId);
                metricMap.put(tempTableId, metricCalculate);
            } finally {
                writeLock.unlock();
            }
        });
        //删除metricMap不存在allTableId的元素
        List<Long> oldIdList = new ArrayList<>(metricMap.keySet());
        if (allTableId.retainAll(oldIdList)) {
            allTableId.forEach(metricMap::remove);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        queryMetric();
    }

}
