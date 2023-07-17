package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionColumnItemDto;
import com.yanggu.metric_calculate.config.pojo.entity.Derive;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionColumnItem;
import com.yanggu.metric_calculate.config.service.DeriveService;
import com.yanggu.metric_calculate.config.service.DimensionColumnItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 派生指标 服务层实现。
 */
@Service
public class DeriveServiceImpl extends ServiceImpl<DeriveMapper, Derive> implements DeriveService {

    @Autowired
    private DeriveMapstruct deriveMapstruct;

    @Autowired
    private DeriveMapper deriveMapper;

    @Autowired
    private DimensionColumnItemService dimensionColumnItemService;

    @Override
    public void create(DeriveDto deriveDto) {
        Derive derive = deriveMapstruct.toEntity(deriveDto);
        //保存派生指标
        deriveMapper.insertSelective(derive);

        //保存维度
        List<DimensionColumnItemDto> dimensionList = derive.getDimensionList();
        if (CollUtil.isNotEmpty(dimensionList)) {
            for (DimensionColumnItemDto dimensionColumnItem : dimensionList) {
                dimensionColumnItem.setDeriveId(derive.getId());
            }
            dimensionColumnItemService.save(dimensionList);
        }

        //保存时间字段

        //保存前置过滤条件

        //保存聚合函数

        //保存窗口数据
    }
}