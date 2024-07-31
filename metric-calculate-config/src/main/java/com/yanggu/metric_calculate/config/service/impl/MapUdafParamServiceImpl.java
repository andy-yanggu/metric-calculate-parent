package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MapUdafParamMapper;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 映射类型udaf参数 服务层实现。
 */
@Service
public class MapUdafParamServiceImpl extends ServiceImpl<MapUdafParamMapper, MapUdafParamEntity> implements MapUdafParamService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private MapUdafParamDistinctFieldListRelationService mapUdafParamDistinctFieldListRelationService;

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Autowired
    private MapUdafParamValueAggRelationService mapUdafParamValueAggRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MapUdafParamEntity mapUdafParam, List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(mapUdafParam);

        List<AviatorExpressParamEntity> distinctFieldParamList = mapUdafParam.getDistinctFieldParamList();
        for (AviatorExpressParamEntity aviatorExpressParam : distinctFieldParamList) {
            aviatorExpressParam.setModelColumnList(modelColumnList);
            aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
            MapUdafParamDistinctFieldListRelationEntity relation = new MapUdafParamDistinctFieldListRelationEntity();
            relation.setMapUdafParamId(mapUdafParam.getId());
            relation.setAviatorExpressParamId(aviatorExpressParam.getId());
            mapUdafParamDistinctFieldListRelationService.save(relation);
        }

        BaseUdafParamEntity valueAggParam = mapUdafParam.getValueAggParam();
        baseUdafParamService.saveData(valueAggParam, modelColumnList);
        MapUdafParamValueAggRelationEntity relation = new MapUdafParamValueAggRelationEntity();
        relation.setMapUdafParamId(mapUdafParam.getId());
        relation.setBaseUdafParamId(valueAggParam.getId());
        mapUdafParamValueAggRelationService.save(relation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MapUdafParamEntity mapUdafParam) {
        Integer mapUdafParamId = mapUdafParam.getId();
        super.removeById(mapUdafParamId);
        List<AviatorExpressParamEntity> distinctFieldParamList = mapUdafParam.getDistinctFieldParamList();
        if (CollUtil.isNotEmpty(distinctFieldParamList)) {
            for (AviatorExpressParamEntity aviatorExpressParam : distinctFieldParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
        }
        BaseUdafParamEntity valueAggParam = mapUdafParam.getValueAggParam();
        baseUdafParamService.deleteData(valueAggParam);
    }

}