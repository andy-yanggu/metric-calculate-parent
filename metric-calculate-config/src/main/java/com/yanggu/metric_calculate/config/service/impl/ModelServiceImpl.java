package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
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
import com.yanggu.metric_calculate.config.pojo.req.ModelQueryReq;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveTableDef.DERIVE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelDimensionColumnTableDef.MODEL_DIMENSION_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;

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
    private ModelDimensionColumnService modelDimensionColumnService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DeriveService deriveService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(ModelDto modelDto) throws Exception {
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
            ModelColumn modelColumn = modelTimeColumn.getModelColumn();
            if (modelColumn == null) {
                throw new BusinessException(MODEL_TIME_COLUMN_NULL);
            }
            String name = modelColumn.getName();
            if (StrUtil.isEmpty(name)) {
                throw new BusinessException(MODEL_TIME_COLUMN_NULL);
            }
            Integer modelColumnId = modelColumnNameIdMap.get(name);
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
            ModelColumn modelColumn = modelDimensionColumn.getModelColumn();
            if (modelColumn == null) {
                throw new BusinessException(MODEL_DIMENSION_COLUMN_NULL);
            }
            String name = modelColumn.getName();
            if (StrUtil.isEmpty(name)) {
                throw new BusinessException(MODEL_DIMENSION_COLUMN_NULL);
            }
            Integer modelColumnId = modelColumnNameIdMap.get(name);
            if (modelColumnId == null) {
                throw new BusinessException(MODEL_DIMENSION_COLUMN_NULL);
            }
            modelDimensionColumn.setModelColumnId(modelColumnId);
        });
        modelDimensionColumnService.saveBatch(modelDimensionColumnList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(ModelDto modelDto) {
        Model model = modelMapstruct.toEntity(modelDto);
        //检查name、displayName是否重复
        checkExist(model);
        //更新宽表数据
        updateById(model, false);
        //处理宽表字段
        modelColumnService.updateModelColumnList(model);
        //处理时间字段
        List<ModelTimeColumn> modelTimeColumnList = model.getModelTimeColumnList();
        //处理维度字段
        List<ModelDimensionColumn> modelDimensionColumnList = model.getModelDimensionColumnList();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //如果宽表被派生指标使用则不能删除
        long count = deriveService.queryChain()
                .where(DERIVE.MODEL_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(MODEL_HAS_DERIVE_NOT_DELETE);
        }
        //删除宽表
        removeById(id);
        //删除宽表字段
        QueryWrapper columnQueryWrapper = QueryWrapper.create()
                .where(MODEL_COLUMN.MODEL_ID.eq(id));
        modelColumnService.remove(columnQueryWrapper);
        //删除时间字段
        QueryWrapper timeQueryWrapper = QueryWrapper.create()
                .where(MODEL_TIME_COLUMN.MODEL_ID.eq(id));
        modelTimeColumnService.remove(timeQueryWrapper);
        //删除维度字段
        QueryWrapper dimensionQueryWrapper = QueryWrapper.create()
                .where(MODEL_DIMENSION_COLUMN.MODEL_ID.eq(id));
        modelDimensionColumnService.remove(dimensionQueryWrapper);
    }

    @Override
    public List<ModelDto> listData(ModelQueryReq req) {
        QueryWrapper queryWrapper = buildModelQueryWrapper(req);
        List<Model> modelList = modelMapper.selectListWithRelationsByQuery(queryWrapper);
        return modelMapstruct.toDTO(modelList);
    }

    @Override
    public ModelDto queryById(Integer id) {
        //根据主键查询, 同时关联查询其他表数据
        Model model = modelMapper.selectOneWithRelationsById(id);
        return modelMapstruct.toDTO(model);
    }

    @Override
    public Page<ModelDto> pageData(Integer pageNumber, Integer pageSize, ModelQueryReq req) {
        QueryWrapper queryWrapper = buildModelQueryWrapper(req);
        Page<Model> page = modelMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<ModelDto> list = modelMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param model
     */
    private void checkExist(Model model) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(MODEL.ID.ne(model.getId()))
                .and(MODEL.NAME.eq(model.getName()).or(MODEL.DISPLAY_NAME.eq(model.getDisplayName())));
        long count = modelMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(MODEL_EXIST);
        }
    }

    private QueryWrapper buildModelQueryWrapper(ModelQueryReq req) {
        return QueryWrapper.create()
                .where(MODEL.NAME.like(req.getModelName()))
                .and(MODEL.DISPLAY_NAME.like(req.getModelDisplayName()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

}