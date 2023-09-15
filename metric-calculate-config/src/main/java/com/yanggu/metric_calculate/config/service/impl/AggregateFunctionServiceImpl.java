package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.mapstruct.AggregateFunctionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionField;
import com.yanggu.metric_calculate.config.pojo.entity.JarStore;
import com.yanggu.metric_calculate.config.pojo.req.AggregateFunctionQueryReq;
import com.yanggu.metric_calculate.config.service.*;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.*;
import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.function_factory.FunctionFactory;
import com.yanggu.metric_calculate.core.util.UdafCustomParamData;
import com.yanggu.metric_calculate.core.util.UdafCustomParamDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.data.id.IdUtil;
import org.dromara.hutool.core.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.yanggu.metric_calculate.config.enums.AggregateFunctionTypeEnums.*;
import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionFieldTableDef.AGGREGATE_FUNCTION_FIELD;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.BaseUdafParamTableDef.BASE_UDAF_PARAM;
import static com.yanggu.metric_calculate.config.pojo.entity.table.MapUdafParamTableDef.MAP_UDAF_PARAM;
import static com.yanggu.metric_calculate.config.pojo.entity.table.MixUdafParamTableDef.MIX_UDAF_PARAM;
import static com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory.CLASS_FILTER;

/**
 * 聚合函数 服务层实现。
 */
@Slf4j
@Service
public class AggregateFunctionServiceImpl extends ServiceImpl<AggregateFunctionMapper, AggregateFunction> implements AggregateFunctionService {

    @Autowired
    private AggregateFunctionMapper aggregateFunctionMapper;

    @Autowired
    private AggregateFunctionMapstruct aggregateFunctionMapstruct;

    @Autowired
    private AggregateFunctionFieldService aggregateFunctionFieldService;

    @Autowired
    private JarStoreService jarStoreService;

    @Autowired
    private BaseUdafParamService baseUdafParamService;

    @Autowired
    private MapUdafParamService mapUdafParamService;

    @Autowired
    private MixUdafParamService mixUdafParamService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AggregateFunctionDto aggregateFunctionDto) throws Exception {
        Boolean isBuiltIn = aggregateFunctionDto.getIsBuiltIn();
        AggregateFunction aggregateFunction;
        //如果是内置的, 检查是否存在
        if (Boolean.TRUE.equals(isBuiltIn)) {
            AggregateFunctionFactory aggregateFunctionFactory = new AggregateFunctionFactory();
            aggregateFunctionFactory.init();
            Class<? extends com.yanggu.metric_calculate.core.aggregate_function.AggregateFunction> aggregateFunctionClass = aggregateFunctionFactory.getAggregateFunctionClass(aggregateFunctionDto.getName());
            aggregateFunction = buildAggregateFunction(aggregateFunctionClass);
            aggregateFunction.setIsBuiltIn(true);
        } else {
            //如果不是内置的, 检查jarStoreId是否为空
            if (aggregateFunctionDto.getJarStoreId() == null) {
                throw new BusinessException(JAR_STORE_ID_NULL);
            }
            aggregateFunction = aggregateFunctionMapstruct.toEntity(aggregateFunctionDto);
        }
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
    @Transactional(rollbackFor = RuntimeException.class)
    public void jarSave(MultipartFile file) throws Exception {
        //文件保存到本地
        File dest = new File(SystemUtil.getTmpDirPath() + File.separatorChar + IdUtil.fastSimpleUUID());
        file.transferTo(dest);
        List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
        Consumer<Class<?>> consumer = clazz -> aggregateFunctionList.add(buildAggregateFunction(clazz));
        //加载class到list中
        FunctionFactory.loadClassFromJar(Collections.singletonList(dest.getAbsolutePath()), CLASS_FILTER, consumer);
        if (CollUtil.isEmpty(aggregateFunctionList)) {
            throw new BusinessException(JAR_NOT_HAVE_CLASS);
        }
        JarStore jarStore = new JarStore();
        //TODO 这里可以根据需要将jar文件保存到远程文件服务器中
        jarStore.setJarUrl(dest.toURI().toURL().getPath());
        jarStoreService.save(jarStore);
        for (AggregateFunction aggregateFunction : aggregateFunctionList) {
            aggregateFunction.setIsBuiltIn(false);
            aggregateFunction.setJarStoreId(jarStore.getId());
            AggregateFunctionDto dto = aggregateFunctionMapstruct.toDTO(aggregateFunction);
            saveData(dto);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(AggregateFunctionDto aggregateFunctionDto) {
        AggregateFunction aggregateFunction = UpdateEntity.of(AggregateFunction.class, aggregateFunctionDto.getId());
        //只允许修改description
        aggregateFunction.setDescription(aggregateFunctionDto.getDescription());
        aggregateFunctionMapper.update(aggregateFunction);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //检查基本聚合类型是否引用
        //这里包含了映射类型的value和混合类型的item
        long count = baseUdafParamService.queryChain()
                .where(BASE_UDAF_PARAM.AGGREGATE_FUNCTION_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(BASE_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION);
        }

        //检查映射聚合类型是否引用
        count = mapUdafParamService.queryChain()
                .where(MAP_UDAF_PARAM.AGGREGATE_FUNCTION_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(MAP_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION);
        }

        //检查混合聚合类型是否引用
        count = mixUdafParamService.queryChain()
                .where(MIX_UDAF_PARAM.AGGREGATE_FUNCTION_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(MIX_UDAF_PARAM_REFERENCE_AGGREGATE_FUNCTION);
        }

        //根据id删除
        super.removeById(id);
        //删除聚合函数字段
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(AGGREGATE_FUNCTION_FIELD.AGGREGATE_FUNCTION_ID.eq(id));
        aggregateFunctionFieldService.remove(queryWrapper);
    }

    @Override
    public List<AggregateFunctionDto> listData(AggregateFunctionQueryReq queryReq) {
        QueryWrapper where = buildAggregateFunctionQueryWrapper(queryReq);
        List<AggregateFunction> aggregateFunctionList = aggregateFunctionMapper.selectListWithRelationsByQuery(where);
        return aggregateFunctionMapstruct.toDTO(aggregateFunctionList);
    }

    @Override
    public AggregateFunctionDto queryById(Integer id) {
        AggregateFunction aggregateFunction = aggregateFunctionMapper.selectOneWithRelationsById(id);
        return aggregateFunctionMapstruct.toDTO(aggregateFunction);
    }

    @Override
    public Page<AggregateFunctionDto> pageQuery(Integer pageNumber,
                                                Integer pageSize,
                                                AggregateFunctionQueryReq queryReq) {
        QueryWrapper queryWrapper = buildAggregateFunctionQueryWrapper(queryReq);
        Page<AggregateFunction> aggregateFunctionPage = aggregateFunctionMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<AggregateFunctionDto> list = aggregateFunctionMapstruct.toDTO(aggregateFunctionPage.getRecords());
        return new Page<>(list, pageNumber, pageSize, aggregateFunctionPage.getTotalRow());
    }

    private QueryWrapper buildAggregateFunctionQueryWrapper(AggregateFunctionQueryReq queryReq) {
        return QueryWrapper.create()
                .where(AGGREGATE_FUNCTION.NAME.like(queryReq.getAggregateFunctionName()))
                .and(AGGREGATE_FUNCTION.DISPLAY_NAME.like(queryReq.getAggregateFunctionDisplayName()))
                .orderBy(queryReq.getOrderByColumnName(), queryReq.getAsc());
    }

    private static AggregateFunction buildAggregateFunction(Class<?> clazz) {
        AggregateFunctionAnnotation aggregateFunctionAnnotation = clazz.getAnnotation(AggregateFunctionAnnotation.class);
        if (aggregateFunctionAnnotation == null) {
            throw new BusinessException(AGGREGATE_FUNCTION_CLASS_NOT_HAVE_ANNOTATION, clazz.getName());
        }
        AggregateFunction aggregateFunction = new AggregateFunction();
        //设置名字
        aggregateFunction.setName(aggregateFunctionAnnotation.name());
        //设置中文名
        aggregateFunction.setDisplayName(aggregateFunctionAnnotation.displayName());
        //设置描述信息
        aggregateFunction.setDescription(aggregateFunctionAnnotation.description());
        //数值型
        if (clazz.isAnnotationPresent(Numerical.class)) {
            Numerical numerical = clazz.getAnnotation(Numerical.class);
            aggregateFunction.setMultiNumber(numerical.multiNumber());
            aggregateFunction.setType(NUMERICAL);
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            //集合型
            Collective collective = clazz.getAnnotation(Collective.class);
            aggregateFunction.setKeyStrategy(collective.keyStrategy());
            aggregateFunction.setRetainStrategy(collective.retainStrategy());
            aggregateFunction.setType(COLLECTIVE);
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            //对象型
            Objective objective = clazz.getAnnotation(Objective.class);
            aggregateFunction.setKeyStrategy(objective.keyStrategy());
            aggregateFunction.setRetainStrategy(objective.retainStrategy());
            aggregateFunction.setType(OBJECTIVE);
        } else if (clazz.isAnnotationPresent(Mix.class)) {
            //混合型
            aggregateFunction.setType(MIX);
        } else if (clazz.isAnnotationPresent(MapType.class)) {
            //映射型
            aggregateFunction.setType(MAP_TYPE);
        } else {
            throw new BusinessException(AGGREGATE_FUNCTION_CLASS_TYPE_ERROR, clazz.getName());
        }
        //设置聚合函数字段
        List<UdafCustomParamData> udafCustomParamList = UdafCustomParamDataUtil.getUdafCustomParamList(clazz, AggregateFunctionFieldAnnotation.class);
        if (CollUtil.isNotEmpty(udafCustomParamList)) {
            AtomicInteger index = new AtomicInteger(0);
            List<AggregateFunctionField> list = udafCustomParamList.stream()
                    .map(temp -> {
                        AggregateFunctionField aggregateFunctionField = new AggregateFunctionField();
                        aggregateFunctionField.setName(temp.getName());
                        aggregateFunctionField.setDisplayName(temp.getDisplayName());
                        aggregateFunctionField.setDescription(temp.getDescription());
                        aggregateFunctionField.setSort(index.incrementAndGet());
                        return aggregateFunctionField;
                    })
                    .toList();
            aggregateFunction.setAggregateFunctionFieldList(list);
        }
        return aggregateFunction;
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param aggregateFunction
     */
    private void checkExist(AggregateFunction aggregateFunction) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(AGGREGATE_FUNCTION.ID.ne(aggregateFunction.getId()))
                .and(AGGREGATE_FUNCTION.NAME.eq(aggregateFunction.getName()).or(AGGREGATE_FUNCTION.DISPLAY_NAME.eq(aggregateFunction.getDisplayName())));
        long count;
        //如果是内置的不需要用户id
        if (Boolean.TRUE.equals(aggregateFunction.getIsBuiltIn())) {
            count = TenantManager.withoutTenantCondition(() -> aggregateFunctionMapper.selectCountByQuery(queryWrapper));
        } else {
            count = aggregateFunctionMapper.selectCountByQuery(queryWrapper);
        }
        if (count > 0) {
            throw new BusinessException(AGGREGATE_FUNCTION_EXIST);
        }
    }

}