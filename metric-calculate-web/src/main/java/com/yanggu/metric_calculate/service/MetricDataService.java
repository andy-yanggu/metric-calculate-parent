package com.yanggu.metric_calculate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.pojo.UpdateMetricData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 指标数据Service
 */
@Service
public class MetricDataService {

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Autowired
    private MetricConfigDataService metricConfigDataService;

    /**
     * 查询派生指标数据
     *
     * @param tableId
     * @param deriveId
     * @param dimensionMap
     * @return
     */
    public <IN, ACC, OUT> DeriveMetricCalculateResult<OUT> queryDeriveData(Long tableId,
                                                                           Long deriveId,
                                                                           LinkedHashMap<String, Object> dimensionMap) {
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate =
                metricConfigDataService.getDeriveMetricCalculateById(tableId, deriveId);
        DimensionSet dimensionSet = getDimensionSet(tableId, deriveId, dimensionMap);
        return deriveMetricCalculate.query(dimensionSet);
    }

    public List<DeriveMetricCalculateResult<Object>> queryDeriveCurrentData(Long tableId,
                                                                            List<Long> deriveIdList,
                                                                            JSONObject input) {

        //获宽表下的派生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricConfigDataService.getDeriveMetricCalculateList(tableId, deriveIdList);

        List<DeriveMetricCalculateResult<Object>> list = new ArrayList<>();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return list;
        }

        List<DimensionSet> dimensionSetList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
            dimensionSetList.add(dimensionSet);
        }

        //根据维度进行批量查询
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(dimensionSetList);
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        for (DimensionSet dimensionSet : dimensionSetList) {
            MetricCube metricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            if (metricCube != null) {
                //根据明细数据进行查询
                DeriveMetricCalculateResult<Object> result = metricCube.query(input);
                if (result != null) {
                    list.add(result);
                }
            }
        }
        return list;
    }

    public void fillDeriveDataById(Long tableId, Long deriveId, List<JSONObject> dataList) {
        fullFillDeriveDataByDeriveIdList(dataList, tableId, Collections.singletonList(deriveId));
    }

    /**
     * 全量铺底接口, 计算宽表下的所有派生指标
     */
    public void fullFillDeriveData(List<JSONObject> dataList, Long tableId) {
        List<Long> allDeriveIdList = metricConfigDataService.getAllDeriveIdList(tableId);
        fullFillDeriveDataByDeriveIdList(dataList, tableId, allDeriveIdList);
    }

    /**
     * 计算宽表下的指定派生指标
     */
    public void fullFillDeriveDataByDeriveIdList(List<JSONObject> dataList, Long tableId, List<Long> deriveIdList) {
        MetricCalculate metricCalculate = metricConfigDataService.getMetricCalculate(tableId);

        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return;
        }

        //过滤出指定的派生指标
        deriveMetricCalculateList = deriveMetricCalculateList.stream()
                .filter(tempDerive -> deriveIdList.contains(tempDerive.getId()))
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return;
        }
        Set<DimensionSet> dimensionSets = new HashSet<>();
        List<Tuple> tupleList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            for (JSONObject input : dataList) {
                //进行字段计算
                JSONObject detail = metricCalculate.getParam(input);
                Boolean filter = deriveMetricCalculate.getFilterFieldProcessor().process(detail);
                if (Boolean.TRUE.equals(filter)) {
                    DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(detail);
                    dimensionSets.add(dimensionSet);
                    Tuple tuple = new Tuple(deriveMetricCalculate, detail, dimensionSet);
                    tupleList.add(tuple);
                }
            }
        }

        if (CollUtil.isEmpty(dimensionSets)) {
            return;
        }

        //批量读取数据
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(new ArrayList<>(dimensionSets));
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = new HashMap<>();
        }

        for (Tuple tuple : tupleList) {
            DeriveMetricCalculate deriveMetricCalculate = tuple.get(0);
            JSONObject detail = tuple.get(1);
            DimensionSet dimensionSet = tuple.get(2);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            historyMetricCube = deriveMetricCalculate.addInput(detail, historyMetricCube);
            //TODO 缺少了删除数据逻辑
            dimensionSetMetricCubeMap.put(dimensionSet, historyMetricCube);
        }

        //批量更新
        deriveMetricMiddleStore.batchUpdate(new ArrayList<>(dimensionSetMetricCubeMap.values()));
    }

    /**
     * 删除派生指标数据
     *
     * @param tableId
     * @param deriveId
     * @param dimensionMap
     */
    public void deleteDeriveData(Long tableId, Long deriveId, LinkedHashMap<String, Object> dimensionMap) {
        DimensionSet dimensionSet = getDimensionSet(tableId, deriveId, dimensionMap);
        deriveMetricMiddleStore.deleteData(dimensionSet);
    }

    /**
     * 更新派生指标数据
     */
    public void correctDeriveData(UpdateMetricData updateMetricData) {
        if (updateMetricData.getWindow().isEmpty()) {
            throw new RuntimeException("传入的window为空");
        }
        DimensionSet dimensionSet = getDimensionSet(updateMetricData.getTableId(), updateMetricData.getDeriveId(), updateMetricData.getDimensionMap());
        MetricCube metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setWindow(updateMetricData.getWindow());
        deriveMetricMiddleStore.update(metricCube);
    }

    /**
     * 获取维度数据
     *
     * @param tableId
     * @param deriveId
     * @param dimensionMap
     * @return
     */
    private DimensionSet getDimensionSet(Long tableId, Long deriveId, LinkedHashMap<String, Object> dimensionMap) {
        //获取派生指标
        Derive derive = metricConfigDataService.getDerive(tableId, deriveId);
        if (derive == null) {
            throw new RuntimeException("传入的tableId: " + tableId + "或者deriveId: " + deriveId + "有误");
        }
        DimensionSet dimension = new DimensionSet();
        dimension.setKey(tableId + "_" + derive.getId());
        dimension.setMetricName(derive.getName());
        dimension.setDimensionMap(dimensionMap);
        return dimension;
    }

}
