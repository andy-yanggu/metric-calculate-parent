package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.Model;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelDimensionColumn;
import com.yanggu.metric_calculate.config.pojo.entity.ModelTimeColumn;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import com.yanggu.metric_calculate.config.service.ModelDimensionColumnService;
import com.yanggu.metric_calculate.config.service.ModelService;
import com.yanggu.metric_calculate.config.service.ModelTimeColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.MODEL_COLUMN_NAME_ERROR;
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
    private ModelTimeColumnService modelTimeColumnService;

    @Autowired
    private ModelDimensionColumnService dimensionColumnService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void create(ModelDto modelDto) throws Exception {
        Model model = modelMapstruct.toEntity(modelDto);

        //检查name、displayName是否重复
        checkExist(model);

        //1. 保存宽表
        modelMapper.insertSelective(model);
        //获取回显的主键
        Integer modelId = model.getId();

        //2. 保存宽表字段
        List<ModelColumn> modelColumnList = model.getModelColumnList();
        //设置宽表字段中的modelId
        modelColumnList.forEach(tempColumnDto -> tempColumnDto.setModelId(modelId));
        modelColumnService.saveModelColumnList(modelColumnList);
        Map<String, Integer> modelColumnNameIdMap = modelColumnList.stream()
                .collect(Collectors.toMap(ModelColumn::getName, ModelColumn::getId));

        //3. 保存时间字段
        List<ModelTimeColumn> modelTimeColumnList = model.getModelTimeColumnList();
        modelTimeColumnList.forEach(modelTimeColumn -> {
            modelTimeColumn.setModelId(modelId);
            modelTimeColumn.setUserId(model.getUserId());
            Integer modelColumnId = modelColumnNameIdMap.get(modelTimeColumn.getModelColumnName());
            if (modelColumnId == null) {
                throw new BusinessException(MODEL_COLUMN_NAME_ERROR);
            }
            modelTimeColumn.setModelColumnId(modelColumnId);
        });
        modelTimeColumnService.saveBatch(modelTimeColumnList);

        //4. 保存维度字段
        List<ModelDimensionColumn> modelDimensionColumnList = model.getModelDimensionColumnList();
        modelDimensionColumnList.forEach(modelDimensionColumn -> {
            modelDimensionColumn.setModelId(modelId);
            modelDimensionColumn.setUserId(model.getUserId());
            Integer modelColumnId = modelColumnNameIdMap.get(modelDimensionColumn.getModelColumnName());
            if (modelColumnId == null) {
                throw new BusinessException(MODEL_COLUMN_NAME_ERROR);
            }
            modelDimensionColumn.setModelColumnId(modelColumnId);
        });
        dimensionColumnService.saveBatch(modelDimensionColumnList);
    }

    @Override
    public ModelDto queryById(Integer id) {
        //根据主键查询, 同时关联查询其他表数据
        Model model = modelMapper.selectOneWithRelationsById(id);
        return modelMapstruct.toDTO(model);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateById(ModelDto modelDto) {
        Model model = modelMapstruct.toEntity(modelDto);
        //检查name、displayName是否重复
        checkExist(model);
        //更新宽表数据
        updateById(model, false);
        //处理宽表字段
        modelColumnService.updateModelColumnList(model);

        //处理维度字段
        List<ModelDimensionColumn> modelDimensionColumnList = model.getModelDimensionColumnList();
        //处理时间字段
        List<ModelTimeColumn> modelTimeColumnList = model.getModelTimeColumnList();
    }

    /**
     * 检查name、displayName是否重复
     *
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