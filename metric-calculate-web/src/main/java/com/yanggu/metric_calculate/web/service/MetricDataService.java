package com.yanggu.metric_calculate.web.service;

import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.cube.MetricCube;
import com.yanggu.metric_calculate.core.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import com.yanggu.metric_calculate.web.pojo.dto.UpdateMetricData;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

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
                                                                           LinkedHashMap<String, Object> dimensionMap) throws Exception {
        DeriveMetricCalculate<IN, ACC, OUT> deriveMetricCalculate =
                metricConfigDataService.getDeriveMetricCalculateById(tableId, deriveId);
        DimensionSet dimensionSet = getDimensionSet(tableId, deriveId, dimensionMap);
        return deriveMetricCalculate.query(dimensionSet);
    }

    public List<DeriveMetricCalculateResult<Object>> queryDeriveCurrentData(Long tableId,
                                                                            List<Long> deriveIdList,
                                                                            Map<String, Object> input) throws Exception {

        //获宽表下的派生指标
        List<DeriveMetricCalculate> deriveMetricCalculateList = metricConfigDataService.getDeriveMetricCalculateList(tableId, deriveIdList);

        List<DeriveMetricCalculateResult<Object>> list = new ArrayList<>();
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return list;
        }

        Map<DimensionSet, DeriveMetricCalculate> map = new HashMap<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            DimensionSet dimensionSet = deriveMetricCalculate.getDimensionSetProcessor().process(input);
            map.put(dimensionSet, deriveMetricCalculate);
        }

        List<DimensionSet> dimensionSetList = new ArrayList<>(map.keySet());
        //根据维度进行批量查询
        Map<DimensionSet, MetricCube> dimensionSetMetricCubeMap = deriveMetricMiddleStore.batchGet(dimensionSetList);
        if (dimensionSetMetricCubeMap == null) {
            dimensionSetMetricCubeMap = Collections.emptyMap();
        }

        for (DimensionSet dimensionSet : dimensionSetList) {
            MetricCube metricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            DeriveMetricCalculate deriveMetricCalculate = map.get(dimensionSet);
            DeriveMetricCalculateResult query = deriveMetricCalculate.query(metricCube, input);
            if (query != null) {
                list.add(query);
            }
        }
        return list;
    }

    public void fillDeriveDataById(Long tableId, Long deriveId, List<Map<String, Object>> dataList) throws Exception {
        fullFillDeriveDataByDeriveIdList(dataList, tableId, Collections.singletonList(deriveId));
    }

    /**
     * 全量铺底接口, 计算宽表下的所有派生指标
     */
    public void fullFillDeriveData(List<Map<String, Object>> dataList, Long tableId) throws Exception {
        List<Long> allDeriveIdList = metricConfigDataService.getAllDeriveIdList(tableId);
        fullFillDeriveDataByDeriveIdList(dataList, tableId, allDeriveIdList);
    }

    /**
     * 计算宽表下的指定派生指标
     */
    public void fullFillDeriveDataByDeriveIdList(List<Map<String, Object>> dataList, Long tableId, List<Long> deriveIdList) throws Exception {
        MetricCalculate metricCalculate = metricConfigDataService.getMetricCalculate(tableId);

        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateListById(deriveIdList);

        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            return;
        }
        Set<DimensionSet> dimensionSets = new HashSet<>();
        List<Tuple> tupleList = new ArrayList<>();
        for (DeriveMetricCalculate deriveMetricCalculate : deriveMetricCalculateList) {
            for (Map<String, Object> input : dataList) {
                //进行字段计算
                Map<String, Object> detail = metricCalculate.getParam(input);
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
            Map<String, Object> detail = tuple.get(1);
            DimensionSet dimensionSet = tuple.get(2);
            MetricCube historyMetricCube = dimensionSetMetricCubeMap.get(dimensionSet);
            historyMetricCube = deriveMetricCalculate.addInput(detail, historyMetricCube, dimensionSet);
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
    public void deleteDeriveData(Long tableId, Long deriveId, LinkedHashMap<String, Object> dimensionMap) throws Exception {
        DimensionSet dimensionSet = getDimensionSet(tableId, deriveId, dimensionMap);
        deriveMetricMiddleStore.deleteData(dimensionSet);
    }

    /**
     * 更新派生指标数据
     */
    public <IN, ACC, OUT> void correctDeriveData(UpdateMetricData<IN, ACC, OUT> updateMetricData) throws Exception {
        if (updateMetricData.getWindow().isEmpty()) {
            throw new RuntimeException("传入的window为空");
        }
        DimensionSet dimensionSet = getDimensionSet(updateMetricData.getTableId(), updateMetricData.getDeriveId(), updateMetricData.getDimensionMap());
        MetricCube<IN, ACC, OUT> metricCube = new MetricCube<>();
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
        DeriveMetrics deriveMetrics = metricConfigDataService.getDerive(tableId, deriveId);
        if (deriveMetrics == null) {
            throw new RuntimeException("传入的tableId: " + tableId + "或者deriveId: " + deriveId + "有误");
        }
        DimensionSet dimension = new DimensionSet();
        dimension.setKey(tableId + "_" + deriveMetrics.getId());
        dimension.setMetricName(deriveMetrics.getName());
        dimension.setDimensionMap(dimensionMap);
        return dimension;
    }

}
