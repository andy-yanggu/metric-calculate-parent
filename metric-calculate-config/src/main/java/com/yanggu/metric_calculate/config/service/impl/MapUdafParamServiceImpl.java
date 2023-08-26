package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MapUdafParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 映射类型udaf参数 服务层实现。
 */
@Service
public class MapUdafParamServiceImpl extends ServiceImpl<MapUdafParamMapper, MapUdafParam> implements MapUdafParamService {

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
    public void saveData(MapUdafParam mapUdafParam) throws Exception {
        super.save(mapUdafParam);

        List<AviatorExpressParam> distinctFieldParamList = mapUdafParam.getDistinctFieldParamList();
        for (AviatorExpressParam aviatorExpressParam : distinctFieldParamList) {
            aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
            MapUdafParamDistinctFieldListRelation relation = new MapUdafParamDistinctFieldListRelation();
            relation.setMapUdafParamId(mapUdafParam.getId());
            relation.setAviatorExpressParamId(aviatorExpressParam.getId());
            mapUdafParamDistinctFieldListRelationService.save(relation);
        }

        BaseUdafParam valueAggParam = mapUdafParam.getValueAggParam();
        baseUdafParamService.saveData(valueAggParam);
        MapUdafParamValueAggRelation relation = new MapUdafParamValueAggRelation();
        relation.setMapUdafParamId(mapUdafParam.getId());
        relation.setBaseUdafParamId(valueAggParam.getId());
        mapUdafParamValueAggRelationService.save(relation);
    }

}