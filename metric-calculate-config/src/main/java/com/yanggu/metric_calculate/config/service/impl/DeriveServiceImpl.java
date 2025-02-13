package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.query.QueryTable;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.relation.RelationManager;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.DeriveDTO;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.domain.query.DeriveQuery;
import com.yanggu.metric_calculate.config.domain.vo.DeriveMetricsConfigData;
import com.yanggu.metric_calculate.config.domain.vo.DeriveVO;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.service.*;
import com.yanggu.metric_calculate.core.pojo.metric.DeriveMetrics;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.domain.entity.table.AggregateFunctionParamTableDef.AGGREGATE_FUNCTION_PARAM;
import static com.yanggu.metric_calculate.config.domain.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;
import static com.yanggu.metric_calculate.config.domain.entity.table.AtomTableDef.ATOM;
import static com.yanggu.metric_calculate.config.domain.entity.table.DeriveFilterExpressRelationTableDef.DERIVE_FILTER_EXPRESS_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.DeriveModelDimensionColumnRelationTableDef.DERIVE_MODEL_DIMENSION_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.DeriveTableDef.DERIVE;
import static com.yanggu.metric_calculate.config.domain.entity.table.DeriveWindowParamRelationTableDef.DERIVE_WINDOW_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.domain.entity.table.DimensionTableDef.DIMENSION;
import static com.yanggu.metric_calculate.config.domain.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.domain.entity.table.ModelDimensionColumnTableDef.MODEL_DIMENSION_COLUMN;
import static com.yanggu.metric_calculate.config.domain.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.domain.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;
import static com.yanggu.metric_calculate.config.domain.entity.table.WindowParamTableDef.WINDOW_PARAM;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;

/**
 * 派生指标 服务层实现。
 */
@Service
public class DeriveServiceImpl extends ServiceImpl<DeriveMapper, DeriveEntity> implements DeriveService {

    @Autowired
    private DeriveMapstruct deriveMapstruct;

    @Autowired
    private DeriveMapper deriveMapper;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelColumnService modelColumnService;

    @Autowired
    private DeriveModelDimensionColumnRelationService deriveModelDimensionColumnRelationService;

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private DeriveFilterExpressRelationService deriveFilterExpressRelationService;

    @Autowired
    private WindowParamService windowParamService;

    @Autowired
    private DeriveWindowParamRelationService deriveWindowParamRelationService;

    @Autowired
    private AtomService atomService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(DeriveDTO deriveDto) throws Exception {
        DeriveEntity derive = deriveMapstruct.dtoToEntity(deriveDto);

        //检查name、displayName是否重复
        checkExist(derive);

        Integer atomId = derive.getAtomId();
        AtomEntity atom = atomService.queryChain().eq(ATOM.ID.getName(), atomId).withRelations().one();
        derive.setAtom(atom);

        //保存派生指标
        deriveMapper.insertSelective(derive);

        //保存关联数据
        saveRelation(derive);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(DeriveDTO deriveDto) throws Exception {
        DeriveEntity updateDerive = deriveMapstruct.dtoToEntity(deriveDto);

        //检查name和displayName是否重复
        checkExist(updateDerive);

        //更新派生指标基本数据
        deriveMapper.update(updateDerive, false);

        //查询一下数据库中的派生指标
        DeriveEntity dbDerive = getDeriveById(deriveDto.getId());
        //删除关联数据
        deleteRelation(dbDerive);

        //新增关联数据
        saveRelation(updateDerive);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        DeriveEntity derive = getDeriveById(id);
        //删除派生指标
        super.removeById(id);

        //删除关联数据
        deleteRelation(derive);
    }

    @Override
    public List<DeriveVO> listData(DeriveQuery deriveQuery) {
        QueryWrapper queryWrapper = buildDeriveQueryWrapper(deriveQuery);
        List<DeriveEntity> derives = deriveMapper.selectListWithRelationsByQuery(queryWrapper);
        return deriveMapstruct.entityToVO(derives);
    }

    @Override
    public DeriveVO queryById(Integer id) {
        DeriveEntity derive = getDeriveById(id);
        return deriveMapstruct.entityToVO(derive);
    }

    @Override
    public PageVO<DeriveVO> pageQuery(DeriveQuery deriveQuery) {
        QueryWrapper queryWrapper = buildDeriveQueryWrapper(deriveQuery);
        deriveMapper.paginateWithRelations(deriveQuery, queryWrapper);
        return deriveMapstruct.entityToPageVO(deriveQuery);
    }

    @Override
    public DeriveMetrics toCoreDeriveMetrics(Integer deriveId) {
        DeriveEntity derive = deriveMapper.selectOneWithRelationsById(deriveId);
        return deriveMapstruct.toDeriveMetrics(derive);
    }

    @Override
    public List<DeriveMetricsConfigData> getAllCoreDeriveMetrics() {
        return TenantManager.withoutTenantCondition(() -> {
            List<DeriveEntity> deriveList = deriveMapper.selectAllWithRelations();
            if (CollUtil.isEmpty(deriveList)) {
                return Collections.emptyList();
            }
            RelationManager.addQueryRelations(ModelEntity::getModelColumnList);
            List<Integer> modelIdList = deriveList.stream()
                    .map(tempDerive -> tempDerive.getAtom().getModelId())
                    .distinct()
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .from(MODEL)
                    .where(MODEL.ID.in(modelIdList));
            Map<Integer, ModelEntity> modelMap = modelService.getMapper()
                    .selectListWithRelationsByQuery(queryWrapper).stream()
                    .collect(Collectors.toMap(ModelEntity::getId, Function.identity()));

            if (MapUtil.isEmpty(modelMap)) {
                return Collections.emptyList();
            }

            return deriveList.stream()
                    .map(derive -> {
                        ModelEntity model = modelMap.get(derive.getAtom().getModelId());
                        if (model == null) {
                            throw new BusinessException(MODEL_ID_ERROR, derive.getAtom().getModelId());
                        }
                        return deriveMapstruct.toDeriveMetricsConfigData(derive, model);
                    })
                    .toList();
        });
    }

    private void saveRelation(DeriveEntity derive) throws Exception {
        //保存维度
        List<ModelDimensionColumnEntity> modelDimensionColumnList = derive.getModelDimensionColumnList();
        AtomicInteger index = new AtomicInteger(0);
        //转换成派生指标和维度字段中间数据
        List<DeriveModelDimensionColumnRelationEntity> collect = modelDimensionColumnList.stream()
                .map(dimensionColumn -> {
                    DeriveModelDimensionColumnRelationEntity relation = new DeriveModelDimensionColumnRelationEntity();
                    relation.setDeriveId(derive.getId());
                    relation.setModelDimensionColumnId(dimensionColumn.getId());
                    relation.setSort(index.incrementAndGet());
                    return relation;
                })
                .toList();
        deriveModelDimensionColumnRelationService.saveBatch(collect);

        //根据宽表id查询对应的宽表字段
        List<ModelColumnEntity> modelColumnList = modelColumnService.queryChain()
                .from(MODEL_COLUMN)
                .where(MODEL_COLUMN.MODEL_ID.eq(derive.getAtom().getModelId()))
                .list();

        //保存前置过滤条件
        AviatorExpressParamEntity filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            filterExpressParam.setModelColumnList(modelColumnList);
            aviatorExpressParamService.saveDataByModelColumn(filterExpressParam);
            //保存派生指标和前置过滤条件中间表数据
            DeriveFilterExpressRelationEntity deriveFilterExpressRelation = new DeriveFilterExpressRelationEntity();
            deriveFilterExpressRelation.setDeriveId(derive.getId());
            deriveFilterExpressRelation.setAviatorExpressParamId(filterExpressParam.getId());
            deriveFilterExpressRelationService.save(deriveFilterExpressRelation);
        }

        //保存窗口数据
        WindowParamEntity windowParam = derive.getWindowParam();
        windowParamService.saveData(windowParam, modelColumnList);
        //保存派生指标和窗口数据中间表
        DeriveWindowParamRelationEntity deriveWindowParamRelation = new DeriveWindowParamRelationEntity();
        deriveWindowParamRelation.setDeriveId(derive.getId());
        deriveWindowParamRelation.setWindowParamId(windowParam.getId());
        deriveWindowParamRelationService.save(deriveWindowParamRelation);
    }

    private DeriveEntity getDeriveById(Integer id) {
        DeriveEntity derive = deriveMapper.selectOneWithRelationsById(id);
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
    private QueryWrapper buildDeriveQueryWrapper(DeriveQuery deriveQuery) {
        String timeColumn = "time_column";
        QueryTable timeColumnAlias = MODEL_COLUMN.as(timeColumn);

        String dimensionColumn = "dimension_column";
        QueryTable dimensionColumnAlias = MODEL_COLUMN.as(dimensionColumn);
        return QueryWrapper.create()
                .select(DERIVE.DEFAULT_COLUMNS)
                .from(DERIVE)
                .innerJoin(MODEL_COLUMN).as(timeColumn).on(MODEL_COLUMN.ID.eq(MODEL_TIME_COLUMN.MODEL_COLUMN_ID))
                //维度字段
                .innerJoin(DERIVE_MODEL_DIMENSION_COLUMN_RELATION).on(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.DERIVE_ID.eq(DERIVE.ID))
                .innerJoin(MODEL_DIMENSION_COLUMN).on(MODEL_DIMENSION_COLUMN.ID.eq(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.MODEL_DIMENSION_COLUMN_ID))
                .innerJoin(MODEL_COLUMN).as(dimensionColumn).on(MODEL_COLUMN.ID.eq(MODEL_DIMENSION_COLUMN.MODEL_COLUMN_ID))
                //维度数据
                .innerJoin(DIMENSION).on(DIMENSION.ID.eq(MODEL_DIMENSION_COLUMN.DIMENSION_ID))
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
                .and(new QueryColumn(timeColumnAlias, MODEL_COLUMN.NAME.getName()).like(deriveQuery.getTimeColumnName()))
                //过滤时间字段中文名
                .and(new QueryColumn(timeColumnAlias, MODEL_COLUMN.DISPLAY_NAME.getName()).like(deriveQuery.getTimeColumnDisplayName()))
                //过滤维度字段名
                .and(new QueryColumn(dimensionColumnAlias, MODEL_COLUMN.NAME.getName()).like(deriveQuery.getDimensionColumnName()))
                //过滤维度字段中文名
                .and(new QueryColumn(dimensionColumnAlias, MODEL_COLUMN.DISPLAY_NAME.getName()).like(deriveQuery.getDimensionColumnDisplayName()))
                //过滤维度名称
                .and(DIMENSION.NAME.like(deriveQuery.getDimensionName()))
                .and(DIMENSION.DISPLAY_NAME.like(deriveQuery.getDimensionDisplayName()))
                //过滤窗口类型
                .and(WINDOW_PARAM.WINDOW_TYPE.eq(deriveQuery.getWindowType()))
                .groupBy(DERIVE.ID);
    }

    private void deleteRelation(DeriveEntity derive) {
        Integer id = derive.getId();
        //删除前置过滤条件
        AviatorExpressParamEntity filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            aviatorExpressParamService.deleteData(filterExpressParam);
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .from(DERIVE_FILTER_EXPRESS_RELATION)
                    .where(DERIVE_FILTER_EXPRESS_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_FILTER_EXPRESS_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(filterExpressParam.getId()));
            deriveFilterExpressRelationService.remove(queryWrapper);
        }
        //删除维度字段
        List<ModelDimensionColumnEntity> modelDimensionColumnList = derive.getModelDimensionColumnList();
        if (CollUtil.isNotEmpty(modelDimensionColumnList)) {
            List<Integer> modelDimensionIdList = modelDimensionColumnList.stream()
                    .map(ModelDimensionColumnEntity::getId)
                    .toList();
            QueryWrapper dimensionQueryWrapper = QueryWrapper.create()
                    .from(DERIVE_MODEL_DIMENSION_COLUMN_RELATION)
                    .where(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.DERIVE_ID.eq(id))
                    .and(DERIVE_MODEL_DIMENSION_COLUMN_RELATION.MODEL_DIMENSION_COLUMN_ID.in(modelDimensionIdList));
            deriveModelDimensionColumnRelationService.remove(dimensionQueryWrapper);
        }
        //删除窗口参数
        WindowParamEntity windowParam = derive.getWindowParam();
        if (windowParam != null) {
            windowParamService.deleteData(derive.getWindowParam());
            QueryWrapper windowParamQueryWrapper = QueryWrapper.create()
                    .from(DERIVE_WINDOW_PARAM_RELATION)
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
    private void checkExist(DeriveEntity derive) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .from(DERIVE)
                //当id存在时为更新
                .where(DERIVE.ID.ne(derive.getId()))
                .and(DERIVE.NAME.eq(derive.getName()).or(DERIVE.DISPLAY_NAME.eq(derive.getDisplayName())));
        long count = deriveMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(DERIVE_EXIST);
        }
    }

}