package com.yanggu.metric_calculate.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.client.metric_config.MetricConfigClient;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.cube.MetricCube;
import com.yanggu.metric_calculate.core2.field_process.dimension.DimensionSet;
import com.yanggu.metric_calculate.core2.middle_store.DeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.DeriveMetricCalculateResult;
import com.yanggu.metric_calculate.core2.table.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MetricDataService {

    @Autowired
    private MetricConfigClient metricConfigClient;

    @Autowired
    @Qualifier("redisDeriveMetricMiddleStore")
    private DeriveMetricMiddleStore deriveMetricMiddleStore;

    @Autowired
    private MetricConfigService metricConfigService;

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
        if (dimension == null) return null;
        MetricCube<Object, Object, Object> metricCube = deriveMetricMiddleStore.get(dimension);
        if (metricCube == null) {
            return null;
        }
        return metricCube.query();
    }

    /**
     * 全量铺底接口, 计算宽表下的所有指标
     */
    public void fullUpdate(List<JSONObject> dataList, Long tableId) {
        MetricCalculate metricCalculate = metricConfigService.getMetricCalculate(tableId);

        List<DeriveMetricCalculate> deriveMetricCalculateList = metricCalculate.getDeriveMetricCalculateList();
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
     *
     * @param tableId
     * @param deriveId
     * @param dimensionMap
     * @param table
     */
    public void updateDeriveData(Long tableId, Long deriveId, LinkedHashMap<String, Object> dimensionMap, Table table) {
        DimensionSet dimensionSet = getDimensionSet(tableId, deriveId, dimensionMap);
        MetricCube metricCube = new MetricCube<>();
        metricCube.setDimensionSet(dimensionSet);
        metricCube.setTable(table);
        deriveMetricMiddleStore.update(metricCube);
    }

    private DimensionSet getDimensionSet(Long tableId, Long deriveId, LinkedHashMap<String, Object> dimensionMap) {
        DataDetailsWideTable table = metricConfigClient.getTableAndMetricByTableId(tableId);
        if (table == null || table.getId() == null) {
            throw new RuntimeException("传入的tableId: " + tableId + "有误");
        }

        List<Derive> deriveList = table.getDerive();
        if (CollUtil.isEmpty(deriveList)) {
            return null;
        }
        Derive derive = deriveList.stream()
                .filter(tempDerive -> deriveId.equals(tempDerive.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("传入的派生指标id" + deriveId + "有误"));

        DimensionSet dimension = new DimensionSet();
        dimension.setKey(tableId + "_" + derive.getId());
        dimension.setMetricName(derive.getName());
        dimension.setDimensionMap(dimensionMap);
        return dimension;
    }

}
