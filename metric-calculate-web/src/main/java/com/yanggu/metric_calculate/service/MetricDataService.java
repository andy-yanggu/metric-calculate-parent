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
import com.yanggu.metric_calculate.pojo.TableData;
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
    public DeriveMetricCalculateResult<Object> queryDeriveData(Long tableId,
                                                               Long deriveId,
                                                               LinkedHashMap<String, Object> dimensionMap) {
        DimensionSet dimension = getDimensionSet(tableId, deriveId, dimensionMap);
        MetricCube<Object, Object, Object> metricCube = deriveMetricMiddleStore.get(dimension);
        if (metricCube == null) {
            return null;
        }
        return metricCube.query();
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
            return;
        }

        //批量读取数据
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(new ArrayList<>(dimensionSets));
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        for (Tuple tuple : tupleList) {
            DeriveMetricCalculate deriveMetricCalculate = tuple.get(0);
            JSONObject input = tuple.get(1);
            DimensionSet dimensionSet = tuple.get(2);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            historyMetricCube = deriveMetricCalculate.addInput(input, historyMetricCube);
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
    public void correctDeriveData(TableData tableData) {
        DimensionSet dimensionSet = getDimensionSet(tableData.getTableId(), tableData.getDeriveId(), tableData.getDimensionMap());
        MetricCube metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setTable(tableData.getTable());
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
        DimensionSet dimension = new DimensionSet();
        dimension.setKey(tableId + "_" + derive.getId());
        dimension.setMetricName(derive.getName());
        dimension.setDimensionMap(dimensionMap);
        return dimension;
    }

}
