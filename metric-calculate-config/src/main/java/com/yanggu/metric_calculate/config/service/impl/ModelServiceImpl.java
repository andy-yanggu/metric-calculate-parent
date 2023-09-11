package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.req.ModelQueryReq;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

        Model updateModel = modelMapstruct.toEntity(modelDto);
        //检查name、displayName是否重复
        checkExist(updateModel);

        //查询之前的宽表和下面的派生指标
        Model dbModel = getModel(modelDto.getId());

        //获取所有的派生指标
        List<Derive> deriveList = dbModel.getDeriveList();
        //如果宽表下没有派生指标, 直接修改即可
        if (CollUtil.isEmpty(deriveList)) {
            //更新宽表数据
            updateById(updateModel, false);
            //更新宽表字段
            //modelColumnService
            return;
        }
        //派生指标使用的宽表字段
        Set<ModelColumn> usedModelColumnSet = deriveList.stream()
                .map(DeriveMapstruct::getAviatorExpressParamFromDerive)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .map(AviatorExpressParam::getModelColumnList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        //派生指标使用的时间字段
        Set<ModelTimeColumn> useModelTimeColumnSet = new HashSet<>();
        for (Derive derive : deriveList) {
            ModelTimeColumn modelTimeColumn = derive.getModelTimeColumn();
            useModelTimeColumnSet.add(modelTimeColumn);
            //添加时间字段对应的宽表字段
            usedModelColumnSet.add(modelTimeColumn.getModelColumn());
        }

        //派生指标使用的维度字段
        Set<ModelDimensionColumn> useModelDimensionColumnSet = new HashSet<>();
        for (Derive derive : deriveList) {
            List<ModelDimensionColumn> dimensionColumnList = derive.getModelDimensionColumnList();
            for (ModelDimensionColumn modelDimensionColumn : dimensionColumnList) {
                useModelDimensionColumnSet.add(modelDimensionColumn);
                //添加维度字段对应的宽表字段
                usedModelColumnSet.add(modelDimensionColumn.getModelColumn());
            }
        }

        //处理宽表字段
        List<ModelColumn> newModelColumnList = updateModel.getModelColumnList();
        //处理时间字段
        List<ModelTimeColumn> newModelTimeColumnList = updateModel.getModelTimeColumnList();
        //处理维度字段
        List<ModelDimensionColumn> newModelDimensionColumnList = updateModel.getModelDimensionColumnList();

        //遍历新的宽表字段
        for (ModelColumn newModelColumn : newModelColumnList) {
            //如果派生指标使用的宽表字段被修改了, 直接报错
            if (!usedModelColumnSet.contains(newModelColumn)) {
                throw new BusinessException(MODEL_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED);
            }
        }

        //遍历新的时间字段
        for (ModelTimeColumn newModelTimeColumn : newModelTimeColumnList) {
            //如果派生指标使用的时间字段被修改了, 直接报错
            if (useModelTimeColumnSet.contains(newModelTimeColumn)) {

            }
        }

        //遍历新的维度字段
        for (ModelDimensionColumn newModelDimensionColumn : newModelDimensionColumnList) {
            //如果派生指标使用的维度字段被修改了, 直接报错
            if (!useModelDimensionColumnSet.contains(newModelDimensionColumn)) {

            }
        }

        //修改宽表字段、时间字段、维度字段

        //更新宽表数据
        updateById(updateModel, false);
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
        Model model = getModel(id);
        return modelMapstruct.toDTO(model);
    }

    @Override
    public Page<ModelDto> pageData(Integer pageNumber, Integer pageSize, ModelQueryReq req) {
        QueryWrapper queryWrapper = buildModelQueryWrapper(req);
        Page<Model> page = modelMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<ModelDto> list = modelMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    @Override
    public com.yanggu.metric_calculate.core.pojo.data_detail_table.Model toCoreModel(Integer modelId) {
        Model model = TenantManager.withoutTenantCondition(() -> modelMapper.selectOneWithRelationsById(modelId));
        return modelMapstruct.toCoreModel(model);
    }

    @Override
    public List<com.yanggu.metric_calculate.core.pojo.data_detail_table.Model> getAllCoreModel() {
        List<Model> modelList = TenantManager.withoutTenantCondition(() -> modelMapper.selectAllWithRelations());
        return modelMapstruct.toCoreModel(modelList);
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

    private Model getModel(Integer id) {
        //根据主键查询, 同时关联查询其他表数据
        Model model = modelMapper.selectOneWithRelationsById(id);
        if (model == null) {
            throw new BusinessException(MODEL_ID_ERROR);
        }
        return model;
    }

    private QueryWrapper buildModelQueryWrapper(ModelQueryReq req) {
        return QueryWrapper.create()
                .where(MODEL.NAME.like(req.getModelName()))
                .and(MODEL.DISPLAY_NAME.like(req.getModelDisplayName()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

}