package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.mapstruct.AggregateFunctionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionField;
import com.yanggu.metric_calculate.config.service.AggregateFunctionFieldService;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import com.yanggu.metric_calculate.config.util.ThreadLocalUtil;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ResultCode.AGGREGATE_FUNCTION_EXIST;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;

/**
 * 聚合函数 服务层实现。
 */
@Service
public class AggregateFunctionServiceImpl extends ServiceImpl<AggregateFunctionMapper, AggregateFunction> implements AggregateFunctionService {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    @Autowired
    private AggregateFunctionMapstruct aggregateFunctionMapstruct;

    @Autowired
    private AggregateFunctionFieldService aggregateFunctionFieldService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AggregateFunctionDto aggregateFunctionDto) throws Exception {
        AggregateFunction aggregateFunction = aggregateFunctionMapstruct.toEntity(aggregateFunctionDto);
        //检查name、displayName字段是否重复
        checkExist(aggregateFunction);
        super.save(aggregateFunction);
        List<AggregateFunctionField> aggregateFunctionFieldList = aggregateFunction.getAggregateFunctionFieldList();
        if (CollUtil.isNotEmpty(aggregateFunctionFieldList)) {
            aggregateFunctionFieldList.forEach(aggregateFunctionField ->
                    aggregateFunctionField.setAggregateFunctionId(aggregateFunction.getId()));
            aggregateFunctionFieldService.saveBatch(aggregateFunctionFieldList);
        }
    }

    @Override
    public AggregateFunctionDto queryById(Integer id) {
        AggregateFunction aggregateFunction = aggregateFunctionMapper.selectOneWithRelationsById(id);
        return aggregateFunctionMapstruct.toDTO(aggregateFunction);
    }

    @Override
    public List<AggregateFunctionDto> listData() {
        QueryWrapper where = QueryWrapper.create()
                .where(AGGREGATE_FUNCTION.USER_ID.eq(ThreadLocalUtil.getUserId()));
        List<AggregateFunction> aggregateFunctionList = aggregateFunctionMapper.selectListWithRelationsByQuery(where);
        return aggregateFunctionMapstruct.toDTO(aggregateFunctionList);
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param aggregateFunction
     */
    private void checkExist(AggregateFunction aggregateFunction) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(AGGREGATE_FUNCTION.ID.ne(aggregateFunction.getId()).when(aggregateFunction.getId() != null))
                .and(AGGREGATE_FUNCTION.NAME.eq(aggregateFunction.getName()).or(AGGREGATE_FUNCTION.DISPLAY_NAME.eq(aggregateFunction.getDisplayName())))
                .and(AGGREGATE_FUNCTION.USER_ID.eq(aggregateFunction.getUserId()));
        long count = aggregateFunctionMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(AGGREGATE_FUNCTION_EXIST);
        }
    }

}