package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.service.DimensionColumnService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.service.TimeColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ResultCode.MODEL_EXIST;
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
    private DimensionColumnService dimensionColumnService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void create(ModelDto modelDto) {
        Model model = modelMapstruct.toEntity(modelDto);

        //检查name、displayName是否重复
        checkExist(model);

        //新增宽表
        modelMapper.insertSelective(model);

        List<ModelColumn> modelColumnList = model.getModelColumnList();

        //设置modelId
        modelColumnList.forEach(tempColumnDto -> tempColumnDto.setModelId(model.getId()));

        //保存宽表字段
        modelColumnService.saveModelColumnList(modelColumnList);

        //保存时间字段
        timeColumnService.saveTimeColumnList(modelColumnList);

        //保存维度字段
        dimensionColumnService.saveDimensionColumnList(modelColumnList);

    }

    @Override
    public ModelDto queryById(Integer id) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(MODEL.ID.eq(id));
        //设置递归查询深度
        RelationManager.setMaxDepth(10);
        //根据主键查询, 同时关联查询其他表数据
        Model model = modelMapper.selectOneWithRelationsByQuery(queryWrapper);
        return modelMapstruct.toDTO(model);
    }

    /**
     * 检查name、displayName是否重复
     * @param model
     */
    private void checkExist(Model model) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(MODEL.ID.ne(model.getId()).when(model.getId() != null))
                .and(MODEL.NAME.eq(model.getName()).or(MODEL.DISPLAY_NAME.eq(model.getDisplayName())))
                .and(MODEL.USER_ID.eq(model.getUserId()));
        long count = modelMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(MODEL_EXIST);
        }
    }

}