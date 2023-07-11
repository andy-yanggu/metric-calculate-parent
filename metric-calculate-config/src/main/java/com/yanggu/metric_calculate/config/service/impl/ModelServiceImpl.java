package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorExpressParamMapstruct;
import com.yanggu.metric_calculate.config.mapstruct.ModelColumnMapstruct;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorExpressParamDto;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnAviatorExpressRelation;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;

/**
 * 数据明细宽表 服务层实现
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Autowired
    private ModelMapstruct modelMapstruct;

    @Autowired
    private ModelColumnService modelColumnService;

    @Autowired
    private TimeColumnService timeColumnService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void create(ModelDto modelDto) {
        Model model = modelMapstruct.toEntity(modelDto);
        //新增宽表
        modelMapper.insertSelective(model);

        List<ModelColumnDto> modelColumnDtoList = modelDto.getModelColumnList();
        //设置modelId
        modelColumnDtoList.forEach(tempColumnDto -> tempColumnDto.setModelId(model.getId()));

        //保存宽表字段
        modelColumnService.insertModelColumnList(modelColumnDtoList);

        //保存时间字段
        timeColumnService.saveTimeColumn(modelColumnDtoList);

        //TODO 保存维度字段

    }

    @Override
    public ModelDto queryById(Integer id) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(MODEL.ID.eq(id));
        Model model = modelMapper.selectOneWithRelationsByQuery(queryWrapper);
        return modelMapstruct.toDTO(model);
    }

}