package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.service.*;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.yanggu.metric_calculate.config.enums.ResultCode.DERIVE_EXIST;
import static com.yanggu.metric_calculate.config.enums.ResultCode.DERIVE_ID_ERROR;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveAggregateFunctionParamRelationTableDef.DERIVE_AGGREGATE_FUNCTION_PARAM_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveFilterExpressRelationTableDef.DERIVE_FILTER_EXPRESS_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveModelDimensionColumnRelationTableDef.DERIVE_MODEL_DIMENSION_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveModelTimeColumnRelationTableDef.DERIVE_MODEL_TIME_COLUMN_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveTableDef.DERIVE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveWindowParamRelationTableDef.DERIVE_WINDOW_PARAM_RELATION;

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
    public void create(DeriveDto deriveDto) throws Exception {
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
    public void deleteById(Integer id) {
        Derive derive = deriveMapper.selectOneWithRelationsById(id);
        if (derive == null) {
            throw new BusinessException(DERIVE_ID_ERROR, id);
        }
        //删除派生指标
        super.removeById(id);
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

    @Override
    public DeriveDto queryById(Integer id) {
        Derive derive = deriveMapper.selectOneWithRelationsById(id);
        return deriveMapstruct.toDTO(derive);
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
                .and(DERIVE.NAME.eq(derive.getName()).or(DERIVE.DISPLAY_NAME.eq(derive.getDisplayName())))
                .and(DERIVE.USER_ID.eq(derive.getUserId()));
        long count = deriveMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(DERIVE_EXIST);
        }
    }

}