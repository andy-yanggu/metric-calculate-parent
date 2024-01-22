package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.base.vo.PageVO;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.ModelMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.mapstruct.ModelMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelDTO;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.query.ModelQuery;
import com.yanggu.metric_calculate.config.pojo.vo.ModelVO;
import com.yanggu.metric_calculate.config.service.*;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ModelColumnFieldType.VIRTUAL;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelDimensionColumnTableDef.MODEL_DIMENSION_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;

/**
 * 数据明细宽表 服务层实现
 */
@Service
public class ModelServiceImpl extends ServiceImpl<ModelMapper, ModelEntity> implements ModelService {

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
    public void saveData(ModelDTO modelDto) throws Exception {
        ModelEntity model = modelMapstruct.dtoToEntity(modelDto);

        //检查name、displayName是否重复
        checkExist(model);

        //1. 保存宽表
        modelMapper.insertSelective(model);
        //获取回显的主键
        Integer modelId = model.getId();

        //2. 保存宽表字段
        List<ModelColumnEntity> modelColumnList = model.getModelColumnList();
        //设置宽表字段中的modelId
        modelColumnList.forEach(tempColumnDto -> tempColumnDto.setModelId(modelId));
        modelColumnService.saveModelColumnList(modelColumnList);
        Map<String, Integer> modelColumnNameIdMap = modelColumnList.stream()
                .collect(Collectors.toMap(ModelColumnEntity::getName, ModelColumnEntity::getId));

        //3. 保存时间字段
        List<ModelTimeColumnEntity> modelTimeColumnList = model.getModelTimeColumnList();
        modelTimeColumnList.forEach(modelTimeColumn -> {
            //设置时间字段对应的宽表id和宽表字段id
            modelTimeColumn.setModelId(modelId);
            Integer modelColumnId = modelColumnNameIdMap.get(modelTimeColumn.getModelColumnName());
            if (modelColumnId == null) {
                throw new BusinessException(MODEL_COLUMN_NAME_ERROR);
            }
            modelTimeColumn.setModelColumnId(modelColumnId);
        });
        modelTimeColumnService.saveBatch(modelTimeColumnList);

        //4. 保存维度字段
        List<ModelDimensionColumnEntity> modelDimensionColumnList = model.getModelDimensionColumnList();
        modelDimensionColumnList.forEach(modelDimensionColumn -> {
            //设置维度字段对应的宽表id和宽表字段id
            modelDimensionColumn.setModelId(modelId);
            Integer modelColumnId = modelColumnNameIdMap.get(modelDimensionColumn.getModelColumnName());
            if (modelColumnId == null) {
                throw new BusinessException(MODEL_DIMENSION_COLUMN_NULL);
            }
            modelDimensionColumn.setModelColumnId(modelColumnId);
        });
        modelDimensionColumnService.saveBatch(modelDimensionColumnList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(ModelDTO modelDto) {
        ModelEntity updateModel = modelMapstruct.dtoToEntity(modelDto);

        //检查name、displayName是否重复
        checkExist(updateModel);

        //更新宽表数据
        updateById(updateModel, false);
    }

    //TODO 待完成修改宽表接口
    @Override
    public void updateOtherData(ModelDTO modelDto) {
        ModelEntity updateModel = modelMapstruct.dtoToEntity(modelDto);
        //查询之前的宽表和下面的派生指标
        ModelEntity dbModel = getModel(modelDto.getId());

        //获取所有的派生指标
        List<DeriveEntity> deriveList = dbModel.getDeriveList();
        //如果宽表下没有派生指标, 直接修改
        if (CollUtil.isEmpty(deriveList)) {
            return;
        }
        //派生指标使用的宽表字段
        Set<ModelColumnEntity> usedModelColumnSet = deriveList.stream()
                .map(DeriveMapstruct::getAviatorExpressParamFromDerive)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .map(AviatorExpressParamEntity::getModelColumnList)
                .filter(CollUtil::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        //派生指标使用的时间字段
        Set<ModelTimeColumnEntity> useModelTimeColumnSet = new HashSet<>();
        //派生指标使用的维度字段
        Set<ModelDimensionColumnEntity> useModelDimensionColumnSet = new HashSet<>();
        for (DeriveEntity derive : deriveList) {
            //ModelTimeColumn modelTimeColumn = derive.getModelTimeColumn();
            //useModelTimeColumnSet.add(modelTimeColumn);
            //添加时间字段对应的宽表字段
            //usedModelColumnSet.add(modelTimeColumn.getModelColumn());
            List<ModelDimensionColumnEntity> dimensionColumnList = derive.getModelDimensionColumnList();
            for (ModelDimensionColumnEntity modelDimensionColumn : dimensionColumnList) {
                useModelDimensionColumnSet.add(modelDimensionColumn);
                //添加维度字段对应的宽表字段
                //usedModelColumnSet.add(modelDimensionColumn.getModelColumn());
            }
        }

        //处理宽表字段
        List<ModelColumnEntity> newModelColumnList = updateModel.getModelColumnList();
        //处理时间字段
        List<ModelTimeColumnEntity> newModelTimeColumnList = updateModel.getModelTimeColumnList();
        //处理维度字段
        List<ModelDimensionColumnEntity> newModelDimensionColumnList = updateModel.getModelDimensionColumnList();

        Map<Integer, ModelColumnEntity> collect = newModelColumnList.stream()
                .filter(temp -> temp.getId() != null)
                .collect(Collectors.toUnmodifiableMap(ModelColumnEntity::getId, Function.identity()));
        List<ModelColumnEntity> updateModelColumnList = new ArrayList<>();
        for (ModelColumnEntity usedModelColumn : usedModelColumnSet) {
            //如果派生指标使用的宽表字段被删除了直接报错
            ModelColumnEntity updateModelColumn = collect.get(usedModelColumn.getId());
            if (updateModelColumn == null) {
                throw new BusinessException(MODEL_COLUMN_NOT_DELETE_WHEN_DERIVE_USED);
            }
            //如果宽表字段没有被修改过, 直接跳过
            if (updateModelColumn.equals(usedModelColumn)) {
                continue;
            }
            //宽表字段名不允许修改
            if (!StrUtil.equals(updateModelColumn.getName(), usedModelColumn.getName())) {
                throw new BusinessException(MODEL_COLUMN_NAME_NOT_UPDATE_WHEN_DERIVE_USED);
            }
            //宽表字段数据类型不允许修改
            if (!updateModelColumn.getDataType().equals(usedModelColumn.getDataType())) {
                throw new BusinessException(MODEL_COLUMN_DATA_TYPE_NOT_UPDATE_WHEN_DERIVE_USED);
            }
            //宽表字段类型不允许修改
            if (!updateModelColumn.getFieldType().equals(usedModelColumn.getFieldType())) {
                throw new BusinessException(MODEL_COLUMN_FIELD_TYPE_NOT_UPDATE_WHEN_DERIVE_USED);
            }
            //如果之前和现在宽表字段都是虚拟字段, 那么表达式不允许修改
            if (VIRTUAL.equals(updateModelColumn.getDataType())
                    && !usedModelColumn.getAviatorExpressParam().equals(updateModelColumn.getAviatorExpressParam())) {

            }
        }

        //遍历新的时间字段
        for (ModelTimeColumnEntity newModelTimeColumn : newModelTimeColumnList) {
            //如果派生指标使用的时间字段被修改了, 直接报错
            if (useModelTimeColumnSet.contains(newModelTimeColumn)) {
                throw new BusinessException(MODEL_TIME_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED);
            }
        }

        //遍历新的维度字段
        for (ModelDimensionColumnEntity newModelDimensionColumn : newModelDimensionColumnList) {
            //如果派生指标使用的维度字段被修改了, 直接报错
            if (!useModelDimensionColumnSet.contains(newModelDimensionColumn)) {
                throw new BusinessException(MODEL_DIMENSION_COLUMN_NOT_UPDATE_WHEN_DERIVE_USED);
            }
        }

        //修改宽表字段、时间字段、维度字段
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //如果宽表被派生指标使用则不能删除
        //long count = deriveService.queryChain()
        //        .where(DERIVE.MODEL_ID.eq(id))
        //        .count();
        //if (count > 0) {
        //    throw new BusinessException(MODEL_HAS_DERIVE_NOT_DELETE);
        //}
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
    public List<ModelVO> listData(ModelQuery req) {
        QueryWrapper queryWrapper = buildModelQueryWrapper(req);
        List<ModelEntity> modelList = modelMapper.selectListWithRelationsByQuery(queryWrapper);
        return modelMapstruct.entityToVO(modelList);
    }

    @Override
    public ModelVO queryById(Integer id) {
        ModelEntity model = getModel(id);
        return modelMapstruct.entityToVO(model);
    }

    @Override
    public PageVO<ModelVO> pageData(ModelQuery req) {
        QueryWrapper queryWrapper = buildModelQueryWrapper(req);
        modelMapper.paginateWithRelations(req, queryWrapper);
        return modelMapstruct.entityToPageVO(req);
    }

    @Override
    public Model toCoreModel(Integer modelId) {
        ModelEntity model = TenantManager.withoutTenantCondition(() -> modelMapper.selectOneWithRelationsById(modelId));
        return modelMapstruct.toCoreModel(model);
    }

    @Override
    public List<Model> getAllCoreModel() {
        List<ModelEntity> modelList = TenantManager.withoutTenantCondition(() -> modelMapper.selectAllWithRelations());
        return modelMapstruct.toCoreModel(modelList);
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param model
     */
    private void checkExist(ModelEntity model) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(MODEL.ID.ne(model.getId()))
                .and(MODEL.NAME.eq(model.getName()).or(MODEL.DISPLAY_NAME.eq(model.getDisplayName())));
        long count = modelMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(MODEL_EXIST);
        }
    }

    private ModelEntity getModel(Integer id) {
        //根据主键查询, 同时关联查询其他表数据
        ModelEntity model = modelMapper.selectOneWithRelationsById(id);
        if (model == null) {
            throw new BusinessException(MODEL_ID_ERROR);
        }
        return model;
    }

    private QueryWrapper buildModelQueryWrapper(ModelQuery req) {
        return QueryWrapper.create()
                .where(MODEL.NAME.like(req.getModelName()))
                .and(MODEL.DISPLAY_NAME.like(req.getModelDisplayName()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

}