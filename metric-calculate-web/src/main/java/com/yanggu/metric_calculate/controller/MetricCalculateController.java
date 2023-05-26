package com.yanggu.metric_calculate.controller;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.service.MetricCalculateService;
import com.yanggu.metric_calculate.util.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@Api(tags = "指标计算接口")
@RequestMapping("/metric-calculate")
public class MetricCalculateController {

    @Autowired
    private MetricCalculateService metricCalculateService;

    @ApiOperation("有状态-计算接口")
    @PostMapping("/state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> stateExecute(
                                                            @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.stateExecute(detail);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口")
    @PostMapping("/no-state-calculate")
    public ApiResponse<List<DeriveMetricCalculateResult<Object>>> noStateExecute(
                                                            @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {
        List<DeriveMetricCalculateResult<Object>> resultList = metricCalculateService.noStateExecute(detail);
        return ApiResponse.success(resultList);
    }

    @ApiOperation("无状态-计算接口（攒批查询）")
    @PostMapping("/no-state-calculate-accumulate-batch")
    public DeferredResult<ApiResponse<List<DeriveMetricCalculateResult>>> noStateExecuteAccumulateBatch(
                                                                @ApiParam("明细宽表数据") @RequestBody JSONObject input) {
        return metricCalculateService.noStateExecuteAccumulateBatch(input);
    }

    @ApiOperation("有状态-计算接口（攒批查询和攒批更新）")
    @PostMapping("/state-calculate-accumulate-batch")
    public DeferredResult<List<DeriveMetricCalculateResult>> stateExecuteAccumulateBatch(
                                                             @ApiParam("明细宽表数据") @RequestBody JSONObject detail) {

        DeferredResult<List<DeriveMetricCalculateResult>> deferredResult = new DeferredResult<>(TimeUnit.SECONDS.toMillis(60L));

        //获取指标计算类
        //MetricCalculate dataWideTable = getMetricCalculate(detail);

        //计算派生指标
        //List<Tuple> tupleList = new ArrayList<>();
        //List<DeriveMetricCalculate> deriveMetricCalculateList = dataWideTable.getDeriveMetricCalculateList();
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

}
