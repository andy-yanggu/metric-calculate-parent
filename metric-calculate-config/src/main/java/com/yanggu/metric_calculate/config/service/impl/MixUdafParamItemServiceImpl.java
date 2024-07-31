package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamItemMapper;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.domain.entity.table.MixUdafParamItemBaseUdafParamRelationTableDef.MIX_UDAF_PARAM_ITEM_BASE_UDAF_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.MixUdafParamItemMapUdafParamRelationTableDef.MIX_UDAF_PARAM_ITEM_MAP_UDAF_PARAM_RELATION;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层实现。
 */
@Service
public class MixUdafParamItemServiceImpl extends ServiceImpl<MixUdafParamItemMapper, MixUdafParamItemEntity> implements MixUdafParamItemService {

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Autowired
    private MapUdafParamService mapUdafParamService;

    @Autowired
    private MixUdafParamItemBaseUdafParamRelationService mixUdafParamItemBaseUdafParamRelationService;

    @Autowired
    private MixUdafParamItemMapUdafParamRelationService mixUdafParamItemMapUdafParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParamItemEntity mixUdafParamItem, List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(mixUdafParamItem);
        Integer mixUdafParamItemId = mixUdafParamItem.getId();
        BaseUdafParamEntity baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.saveData(baseUdafParam, modelColumnList);
            //保存中间表
            MixUdafParamItemBaseUdafParamRelationEntity relation = new MixUdafParamItemBaseUdafParamRelationEntity();
            relation.setMixUdafParamItemId(mixUdafParamItemId);
            relation.setBaseUdafParamId(baseUdafParam.getId());
            mixUdafParamItemBaseUdafParamRelationService.save(relation);
        }
        MapUdafParamEntity mapUdafParam = mixUdafParamItem.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.saveData(mapUdafParam, modelColumnList);
            //保存中间表
            MixUdafParamItemMapUdafParamRelationEntity relation = new MixUdafParamItemMapUdafParamRelationEntity();
            relation.setMixUdafParamItemId(mixUdafParamItemId);
            relation.setMapUdafParamId(mapUdafParam.getId());
            mixUdafParamItemMapUdafParamRelationService.save(relation);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MixUdafParamItemEntity mixUdafParamItem) {
        Integer mixUdafParamItemId = mixUdafParamItem.getId();
        super.removeById(mixUdafParamItemId);
        //删除中间表和数据
        BaseUdafParamEntity baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.deleteData(baseUdafParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .from(MIX_UDAF_PARAM_ITEM_BASE_UDAF_PARAM_RELATION)
                    .where(MIX_UDAF_PARAM_ITEM_BASE_UDAF_PARAM_RELATION.MIX_UDAF_PARAM_ITEM_ID.eq(mixUdafParamItemId))
                    .and(MIX_UDAF_PARAM_ITEM_BASE_UDAF_PARAM_RELATION.BASE_UDAF_PARAM_ID.eq(baseUdafParam.getId()));
            mixUdafParamItemBaseUdafParamRelationService.remove(queryWrapper);
        }
        MapUdafParamEntity mapUdafParam = mixUdafParamItem.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.deleteData(mapUdafParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .from(MIX_UDAF_PARAM_ITEM_MAP_UDAF_PARAM_RELATION)
                    .where(MIX_UDAF_PARAM_ITEM_MAP_UDAF_PARAM_RELATION.MIX_UDAF_PARAM_ITEM_ID.eq(mixUdafParamItemId))
                    .and(MIX_UDAF_PARAM_ITEM_MAP_UDAF_PARAM_RELATION.MAP_UDAF_PARAM_ID.eq(mapUdafParam.getId()));
            mixUdafParamItemMapUdafParamRelationService.remove(queryWrapper);
        }
    }

}