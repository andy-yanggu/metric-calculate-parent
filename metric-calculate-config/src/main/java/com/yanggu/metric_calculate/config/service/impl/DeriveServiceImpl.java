package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.DeriveMapper;
import com.yanggu.metric_calculate.config.mapstruct.DeriveMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DeriveDto;
import com.yanggu.metric_calculate.config.pojo.entity.*;
import com.yanggu.metric_calculate.config.pojo.exception.BusinessException;
import com.yanggu.metric_calculate.config.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.yanggu.metric_calculate.config.enums.ResultCode.DERIVE_EXIST;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DeriveTableDef.DERIVE;

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
    private DeriveDimensionColumnRelationService deriveDimensionColumnRelationService;

    @Autowired
    private DeriveTimeColumnRelationService deriveTimeColumnRelationService;

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
    public void create(DeriveDto deriveDto) {
        Derive derive = deriveMapstruct.toEntity(deriveDto);

        //检查name、displayName是否重复
        checkExist(derive);
        //保存派生指标
        deriveMapper.insertSelective(derive);

        //保存维度
        List<DimensionColumn> dimensionColumnList = derive.getDimensionColumnList();
        AtomicInteger index = new AtomicInteger(0);
        //转换成派生指标和维度字段中间数据
        List<DeriveDimensionColumnRelation> collect = dimensionColumnList.stream()
                .map(dimensionColumn -> {
                    DeriveDimensionColumnRelation relation = new DeriveDimensionColumnRelation();
                    relation.setDeriveId(derive.getId());
                    relation.setDimensionColumnId(dimensionColumn.getId());
                    relation.setUserId(derive.getUserId());
                    relation.setSort(index.incrementAndGet());
                    return relation;
                })
                .collect(Collectors.toList());
        deriveDimensionColumnRelationService.saveBatch(collect);

        //保存时间字段
        TimeColumn timeColumn = derive.getTimeColumn();
        DeriveTimeColumnRelation deriveTimeColumnRelation = new DeriveTimeColumnRelation();
        deriveTimeColumnRelation.setDeriveId(derive.getId());
        deriveTimeColumnRelation.setTimeColumnId(timeColumn.getId());
        deriveTimeColumnRelation.setUserId(derive.getUserId());
        deriveTimeColumnRelationService.save(deriveTimeColumnRelation);

        //保存前置过滤条件
        AviatorExpressParam filterExpressParam = derive.getFilterExpressParam();
        if (filterExpressParam != null) {
            filterExpressParam.setUserId(derive.getUserId());
            aviatorExpressParamService.save(filterExpressParam);
            //保存派生指标和前置过滤条件中间表数据
            DeriveFilterExpressRelation deriveFilterExpressRelation = new DeriveFilterExpressRelation();
            deriveFilterExpressRelation.setDeriveId(derive.getId());
            deriveFilterExpressRelation.setAviatorExpressParamId(filterExpressParam.getId());
            deriveFilterExpressRelation.setUserId(derive.getUserId());
            deriveFilterExpressRelationService.save(deriveFilterExpressRelation);
        }

        //保存聚合函数参数
        AggregateFunctionParam aggregateFunctionParam = derive.getAggregateFunctionParam();
        aggregateFunctionParam.setUserId(derive.getUserId());
        aggregateFunctionParamService.save(aggregateFunctionParam);
        //保存派生指标和聚合函数参数中间表数据
        DeriveAggregateFunctionParamRelation deriveAggregateFunctionParamRelation = new DeriveAggregateFunctionParamRelation();
        deriveAggregateFunctionParamRelation.setDeriveId(derive.getId());
        deriveAggregateFunctionParamRelation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
        deriveAggregateFunctionParamRelation.setUserId(derive.getUserId());
        deriveAggregateFunctionParamRelationService.save(deriveAggregateFunctionParamRelation);

        //保存窗口数据
        WindowParam windowParam = derive.getWindowParam();
        windowParam.setUserId(derive.getUserId());
        windowParamService.save(windowParam);
        //保存派生指标和窗口数据中间表
        DeriveWindowParamRelation deriveWindowParamRelation = new DeriveWindowParamRelation();
        deriveWindowParamRelation.setDeriveId(derive.getId());
        deriveWindowParamRelation.setWindowParamId(windowParam.getId());
        deriveWindowParamRelation.setUserId(derive.getUserId());
        deriveWindowParamRelationService.save(deriveWindowParamRelation);
    }

    @Override
    public DeriveDto queryById(Integer id) {
        QueryWrapper queryWrapper = QueryWrapper.create().where(DERIVE.ID.eq(id));
        Derive derive = deriveMapper.selectOneWithRelationsByQuery(queryWrapper);
        return deriveMapstruct.toDTO(derive);
    }

    /**
     * 检查name、displayName是否重复
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