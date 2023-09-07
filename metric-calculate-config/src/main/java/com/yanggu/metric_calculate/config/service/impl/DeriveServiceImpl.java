package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.req.DeriveQueryReq;
import com.yanggu.metric_calculate.config.pojo.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.config.service.*;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionParamTableDef.AGGREGATE_FUNCTION_PARAM;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveAggregateFunctionParamRelationTableDef.DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveFilterExpressRelationTableDef.DERIVE_FILTER_EXPRESS_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveModelDimensionColumnRelationTableDef.DERIVE_MODEL_DIMENSION_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveModelTimeColumnRelationTableDef.DERIVE_MODEL_TIME_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveTableDef.DERIVE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveWindowParamRelationTableDef.DERIVE_WINDOW_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DimensionTableDef.DIMENSION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelDimensionColumnTableDef.MODEL_DIMENSION_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.WindowParamTableDef.WINDOW_PARAM;

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
    private ModelService modelService;

    @Autowired
    private DeriveModelDimensionColumnRelationService deriveModelDimensionColumnRelationService;

    @Autowired
    private DeriveModelTimeColumnRelationService deriveModelTimeColumnRelationService;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private DeriveFilterExpressRelationService deriveFilterExpressRelationService;

    @Autowired
    private AggregateFunctionParamService aggregateFunctionParamService;

    @Autowired
    private DeriveAggregateFunctionParamRelationService deriveAggregateFunctionParamRelationService;

    @Autowired
    private WindowParamService windowParamService;

    @Autowired
    private DeriveWindowParamRelationService deriveWindowParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(DeriveDto deriveDto) throws Exception {
        Derive derive = deriveMapstruct.toEntity(deriveDto);

        //检查name、displayName是否重复
        checkExist(derive);
        //保存派生指标
        deriveMapper.insertSelective(derive);

        //保存维度
        List<ModelDimensionColumn> modelDimensionColumnList = derive.getModelDimensionColumnList();
        AtomicInteger index = new AtomicInteger(0);
        //转换成派生指标和维度字段中间数据
        List<DeriveModelDimensionColumnRelation> collect = modelDimensionColumnList.stream()
                .map(dimensionColumn -> {
                    DeriveModelDimensionColumnRelation relation = new DeriveModelDimensionColumnRelation();
                    relation.setDeriveId(derive.getId());
                    relation.setModelDimensionColumnId(dimensionColumn.getId());
                    relation.setSort(index.incrementAndGet());
                    return relation;
                })
                .toList();
        deriveModelDimensionColumnRelationService.saveBatch(collect);

        //保存时间字段
        ModelTimeColumn modelTimeColumn = derive.getModelTimeColumn();
        DeriveModelTimeColumnRelation deriveModelTimeColumnRelation = new DeriveModelTimeColumnRelation();
        deriveModelTimeColumnRelation.setDeriveId(derive.getId());
        deriveModelTimeColumnRelation.setModelTimeColumnId(modelTimeColumn.getId());
        deriveModelTimeColumnRelationService.save(deriveModelTimeColumnRelation);

        //保存前置过滤条件
        AviatorExpressParam filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            aviatorExpressParamService.saveDataByModelColumn(filterExpressParam);
            //保存派生指标和前置过滤条件中间表数据
            DeriveFilterExpressRelation deriveFilterExpressRelation = new DeriveFilterExpressRelation();
            deriveFilterExpressRelation.setDeriveId(derive.getId());
            deriveFilterExpressRelation.setAviatorExpressParamId(filterExpressParam.getId());
            deriveFilterExpressRelationService.save(deriveFilterExpressRelation);
        }

        //保存聚合函数参数
        AggregateFunctionParam aggregateFunctionParam = derive.getAggregateFunctionParam();
        aggregateFunctionParamService.saveData(aggregateFunctionParam);
        //保存派生指标和聚合函数参数中间表数据
        DeriveAggregateFunctionParamRelation deriveAggregateFunctionParamRelation = new DeriveAggregateFunctionParamRelation();
        deriveAggregateFunctionParamRelation.setDeriveId(derive.getId());
        deriveAggregateFunctionParamRelation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
        deriveAggregateFunctionParamRelationService.save(deriveAggregateFunctionParamRelation);

        //保存窗口数据
        WindowParam windowParam = derive.getWindowParam();
        windowParamService.saveData(windowParam);
        //保存派生指标和窗口数据中间表
        DeriveWindowParamRelation deriveWindowParamRelation = new DeriveWindowParamRelation();
        deriveWindowParamRelation.setDeriveId(derive.getId());
        deriveWindowParamRelation.setWindowParamId(windowParam.getId());
        deriveWindowParamRelationService.save(deriveWindowParamRelation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(DeriveDto deriveDto) throws Exception {
        //查询一下数据库中的派生指标
        Derive dbDerive = getDeriveById(deriveDto.getId());

        //删除关联数据
        deleteRelation(dbDerive);

        //新增关联数据
        saveData(deriveDto);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        Derive derive = getDeriveById(id);
        //删除派生指标
        super.removeById(id);

        //删除关联数据
        deleteRelation(derive);
    }

    @Override
    public List<DeriveDto> listData(DeriveQueryReq deriveQuery) {
        QueryWrapper queryWrapper = buildDeriveQueryWrapper(deriveQuery);
        List<Derive> derives = deriveMapper.selectListWithRelationsByQuery(queryWrapper);
        return deriveMapstruct.toDTO(derives);
    }

    @Override
    public DeriveDto queryById(Integer id) {
        Derive derive = getDeriveById(id);
        return deriveMapstruct.toDTO(derive);
    }

    @Override
    public Page<DeriveDto> pageQuery(Integer pageNumber, Integer pageSize, DeriveQueryReq deriveQuery) {
        QueryWrapper queryWrapper = buildDeriveQueryWrapper(deriveQuery);
        Page<Derive> derivePage = deriveMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<DeriveDto> list = deriveMapstruct.toDTO(derivePage.getRecords());
        return new Page<>(list, pageNumber, pageSize, derivePage.getTotalRow());
    }

    @Override
    public DeriveMetrics toCoreDeriveMetrics(Integer deriveId) {
        Derive derive = deriveMapper.selectOneWithRelationsById(deriveId);
        return deriveMapstruct.toDeriveMetrics(derive);
    }

    @Override
    public List<DeriveMetricsConfigData> getAllCoreDeriveMetrics() {
        return TenantManager.withoutTenantCondition(() -> {
            List<Derive> deriveList = deriveMapper.selectAllWithRelations();
            if (CollUtil.isEmpty(deriveList)) {
                return Collections.emptyList();
            }
            RelationManager.addQueryRelations(Model::getModelColumnList);
            List<Integer> modelIdList = deriveList.stream()
                    .map(Derive::getModelId)
                    .distinct()
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create().where(MODEL.ID.in(modelIdList));
            Map<Integer, Model> modelMap = modelService.getMapper()
                    .selectListWithRelationsByQuery(queryWrapper).stream()
                    .collect(Collectors.toMap(Model::getId, Function.identity()));

            if (CollUtil.isEmpty(modelMap)) {
                return Collections.emptyList();
            }

            return deriveList.stream()
                    .map(derive -> {
                        Model model = modelMap.get(derive.getModelId());
                        if (model == null) {
                            throw new BusinessException(MODEL_ID_ERROR, derive.getModelId());
                        }
                        return deriveMapstruct.toDeriveMetricsConfigData(derive, model);
                    })
                    .toList();
        });
    }

    private Derive getDeriveById(Integer id) {
        Derive derive = deriveMapper.selectOneWithRelationsById(id);
        if (derive == null) {
            throw new BusinessException(DERIVE_ID_ERROR, id);
        }
        return derive;
    }

    /**
     * 构建派生指标查询sql
     *
     * @param deriveQuery 查询参数
     * @return
     */
    private QueryWrapper buildDeriveQueryWrapper(DeriveQueryReq deriveQuery) {
        return QueryWrapper.create()
                .select(DERIVE.DEFAULT_COLUMNS)
                .from(DERIVE)
                //数据明细宽表
                .innerJoin(MODEL).on(MODEL.ID.eq(DERIVE.MODEL_ID))
                //时间字段
                .innerJoin(DERIVE_MODEL_TIME_COLUMN_RELATION).on(DERIVE_MODEL_TIME_COLUMN_RELATION.DERIVE_ID.eq(DERIVE.ID))
                .innerJoin(MODEL_TIME_COLUMN).on(MODEL_TIME_COLUMN.ID.eq(DERIVE_MODEL_TIME_COLUMN_RELATION.MODEL_TIME_COLUMN_ID))
                .innerJoin(MODEL_COLUMN).as("time_column").on(MODEL_COLUMN.ID.eq(MODEL_TIME_COLUMN.MODEL_COLUMN_ID))
                //维度字段
                .innerJoin(DERIVE_MODEL_DIMENSION_COLUMN_RELATION).on(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.DERIVE_ID.eq(DERIVE.ID))
                .innerJoin(MODEL_DIMENSION_COLUMN).on(MODEL_DIMENSION_COLUMN.ID.eq(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.MODEL_DIMENSION_COLUMN_ID))
                .innerJoin(MODEL_COLUMN).as("dimension_column").on(MODEL_COLUMN.ID.eq(MODEL_DIMENSION_COLUMN.MODEL_COLUMN_ID))
                //维度数据
                .innerJoin(DIMENSION).on(DIMENSION.ID.eq(MODEL_DIMENSION_COLUMN.DIMENSION_ID))
                //聚合函数
                .innerJoin(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION).on(DERIVE.ID.eq(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.DERIVE_ID))
                .innerJoin(AGGREGATE_FUNCTION_PARAM).on(AGGREGATE_FUNCTION_PARAM.ID.eq(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID))
                .innerJoin(AGGREGATE_FUNCTION).on(AGGREGATE_FUNCTION.ID.eq(AGGREGATE_FUNCTION_PARAM.AGGREGATE_FUNCTION_ID))
                //窗口参数
                .innerJoin(DERIVE_WINDOW_PARAM_RELATION).on(DERIVE.ID.eq(DERIVE_WINDOW_PARAM_RELATION.DERIVE_ID))
                .innerJoin(WINDOW_PARAM).on(DERIVE_WINDOW_PARAM_RELATION.WINDOW_PARAM_ID.eq(WINDOW_PARAM.ID))
                //过滤派生指标名
                .where(DERIVE.NAME.like(deriveQuery.getDeriveName()))
                //过滤中文名
                .and(DERIVE.DISPLAY_NAME.like(deriveQuery.getDeriveDisplayName()))
                //过滤数据明细宽表名
                .and(MODEL.NAME.like(deriveQuery.getModelName()))
                //过滤数据明细宽表中文名
                .and(MODEL.DISPLAY_NAME.like(deriveQuery.getModelDisplayName()))
                //过滤聚合函数名字
                .and(AGGREGATE_FUNCTION.DISPLAY_NAME.like(deriveQuery.getAggregateFunctionName()))
                //过滤时间字段格式
                .and(MODEL_TIME_COLUMN.TIME_FORMAT.like(deriveQuery.getTimeFormat()))
                //过滤时间字段名
                .and(MODEL_COLUMN.NAME.as("time_column." + MODEL_COLUMN.NAME.getName()).like(deriveQuery.getTimeColumnName()))
                //过滤时间字段中文名
                .and(MODEL_COLUMN.DISPLAY_NAME.as("time_column." + MODEL_COLUMN.DISPLAY_NAME.getName()).like(deriveQuery.getTimeColumnDisplayName()))
                //过滤维度字段名
                //.and(MODEL_COLUMN.NAME.as("dimension_column." + MODEL_COLUMN.NAME.getName()).like(deriveQuery.getDimensionColumnName()))
                //.and(MODEL_COLUMN.DISPLAY_NAME.as("dimension_column." + MODEL_COLUMN.DISPLAY_NAME.getName()).like(deriveQuery.getDimensionColumnDisplayName()))
                //过滤维度名称
                .and(DIMENSION.NAME.like(deriveQuery.getDimensionName()))
                .and(DIMENSION.DISPLAY_NAME.like(deriveQuery.getDimensionDisplayName()))
                //过滤窗口类型
                .and(WINDOW_PARAM.WINDOW_TYPE.eq(deriveQuery.getWindowType()))
                .groupBy(DERIVE.ID);
    }

    private void deleteRelation(Derive derive) {
        Integer id = derive.getId();
        //删除前置过滤条件
        AviatorExpressParam filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            aviatorExpressParamService.deleteData(filterExpressParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(DERIVE_FILTER_EXPRESS_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_FILTER_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(filterExpressParam.getId()));
            deriveFilterExpressRelationService.remove(queryWrapper);
        }
        //删除时间字段
        ModelTimeColumn modelTimeColumn = derive.getModelTimeColumn();
        if (modelTimeColumn != null) {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(DERIVE_MODEL_TIME_COLUMN_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_MODEL_TIME_COLUMN_RELATION.MODEL_TIME_COLUMN_ID.eq(modelTimeColumn.getId()));
            deriveModelTimeColumnRelationService.remove(queryWrapper);
        }
        //删除维度字段
        List<ModelDimensionColumn> modelDimensionColumnList = derive.getModelDimensionColumnList();
        if (CollUtil.isNotEmpty(modelDimensionColumnList)) {
            List<Integer> modelDimensionIdList = modelDimensionColumnList.stream()
                    .map(ModelDimensionColumn::getId)
                    .toList();
            QueryWrapper dimensionQueryWrapper = QueryWrapper.create()
                    .where(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.MODEL_DIMENSION_COLUMN_ID.in(modelDimensionIdList));
            deriveModelDimensionColumnRelationService.remove(dimensionQueryWrapper);
        }
        //删除聚合函数参数
        AggregateFunctionParam aggregateFunctionParam = derive.getAggregateFunctionParam();
        if (aggregateFunctionParam != null) {
            aggregateFunctionParamService.deleteData(aggregateFunctionParam);
            QueryWrapper aggregateFunctionParamQueryWrapper = QueryWrapper.create()
                    .where(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION.AGGREGATE_FUNCTION_PARAM_ID.eq(aggregateFunctionParam.getId()));
            deriveAggregateFunctionParamRelationService.remove(aggregateFunctionParamQueryWrapper);
        }
        //删除窗口参数
        WindowParam windowParam = derive.getWindowParam();
        if (windowParam != null) {
            windowParamService.deleteData(derive.getWindowParam());
            QueryWrapper windowParamQueryWrapper = QueryWrapper.create()
                    .where(DERIVE_WINDOW_PARAM_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_WINDOW_PARAM_RELATION.WINDOW_PARAM_ID.eq(windowParam.getId()));
            deriveWindowParamRelationService.remove(windowParamQueryWrapper);
        }
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param derive
     */
    private void checkExist(Derive derive) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(DERIVE.ID.ne(derive.getId()).when(derive.getId() != null))
                .and(DERIVE.NAME.eq(derive.getName()).or(DERIVE.DISPLAY_NAME.eq(derive.getDisplayName())));
        long count = deriveMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(DERIVE_EXIST);
        }
    }

}