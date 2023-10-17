package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamItemMapper;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MapUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.service.BaseUdafParamService;
import com.yanggu.metric_calculate.config.service.MapUdafParamService;
import com.yanggu.metric_calculate.config.service.MixUdafParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层实现。
 */
@Service
public class MixUdafParamItemServiceImpl extends ServiceImpl<MixUdafParamItemMapper, MixUdafParamItem> implements MixUdafParamItemService {

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Autowired
    private MapUdafParamService mapUdafParamService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParamItem mixUdafParamItem, List<ModelColumn> modelColumnList) throws Exception {
        super.save(mixUdafParamItem);
        Integer mixUdafParamItemId = mixUdafParamItem.getId();
        BaseUdafParam baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        if (baseUdafParam != null) {
            baseUdafParamService.saveData(baseUdafParam, modelColumnList);
            //保存中间表
        }
        MapUdafParam mapUdafParam = mixUdafParamItem.getMapUdafParam();
        if (mapUdafParam != null) {
            mapUdafParamService.saveData(mapUdafParam, modelColumnList);
            //保存中间表
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MixUdafParamItem mixUdafParamItem) {
        Integer id = mixUdafParamItem.getId();
        super.removeById(id);
        //删除中间表和数据
        BaseUdafParam baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        baseUdafParamService.deleteData(baseUdafParam);
    }

}