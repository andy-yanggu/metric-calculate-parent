package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.MixUdafParamItemMapper;
import com.yanggu.metric_calculate.config.pojo.entity.BaseUdafParam;
import com.yanggu.metric_calculate.config.pojo.entity.MixUdafParamItem;
import com.yanggu.metric_calculate.config.service.BaseUdafParamService;
import com.yanggu.metric_calculate.config.service.MixUdafParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 混合聚合参数，混合聚合类型定义。value只能是数值型、集合型、对象型中间表 服务层实现。
 */
@Service
public class MixUdafParamItemServiceImpl extends ServiceImpl<MixUdafParamItemMapper, MixUdafParamItem> implements MixUdafParamItemService {

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(MixUdafParamItem mixUdafParamItem) throws Exception {
        BaseUdafParam baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        baseUdafParamService.saveData(baseUdafParam);
        mixUdafParamItem.setBaseUdafParamId(baseUdafParam.getId());
        super.save(mixUdafParamItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(MixUdafParamItem mixUdafParamItem) {
        Integer id = mixUdafParamItem.getId();
        super.removeById(id);
        BaseUdafParam baseUdafParam = mixUdafParamItem.getBaseUdafParam();
        baseUdafParamService.deleteData(baseUdafParam);
    }

}