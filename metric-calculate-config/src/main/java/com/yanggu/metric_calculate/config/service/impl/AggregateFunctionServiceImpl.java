package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AggregateFunctionMapper;
import com.yanggu.metric_calculate.config.mapstruct.AggregateFunctionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AggregateFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionField;
import com.yanggu.metric_calculate.config.pojo.entity.JarStore;
import com.yanggu.metric_calculate.config.service.AggregateFunctionFieldService;
import com.yanggu.metric_calculate.config.service.AggregateFunctionService;
import com.yanggu.metric_calculate.config.service.JarStoreService;
import com.yanggu.metric_calculate.core.aggregate_function.annotation.*;
import com.yanggu.metric_calculate.core.function_factory.FunctionFactory;
import com.yanggu.metric_calculate.core.util.UdafCustomParamData;
import com.yanggu.metric_calculate.core.util.UdafCustomParamDataUtil;
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
import static com.yanggu.metric_calculate.config.pojo.entity.table.AggregateFunctionTableDef.AGGREGATE_FUNCTION;
import static com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory.CLASS_FILTER;

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

    @Autowired
    private JarStoreService jarStoreService;

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
        QueryWrapper where = QueryWrapper.create();
        List<AggregateFunction> aggregateFunctionList = aggregateFunctionMapper.selectListWithRelationsByQuery(where);
        return aggregateFunctionMapstruct.toDTO(aggregateFunctionList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void jarSave(MultipartFile file) throws Exception {
        File dest = new File(SystemUtil.getTmpDirPath() + File.separatorChar + IdUtil.fastSimpleUUID());
        file.transferTo(dest);
        List<String> udafJarPathList = Collections.singletonList(dest.getAbsolutePath());
        List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
        Consumer<Class<?>> consumer = clazz -> aggregateFunctionList.add(buildAggregateFunction(clazz));
        FunctionFactory.loadClassFromJar(udafJarPathList, CLASS_FILTER, consumer);
        if (CollUtil.isEmpty(aggregateFunctionList)) {
            return;
        }
        JarStore jarStore = new JarStore();
        //TODO 这里可以根据需要将jar文件保存到远程文件服务器中
        jarStore.setJarUrl(dest.toURI().toURL().getPath());
        jarStoreService.save(jarStore);
        for (AggregateFunction aggregateFunction : aggregateFunctionList) {
            aggregateFunction.setJarStoreId(jarStore.getId());
            AggregateFunctionDto dto = aggregateFunctionMapstruct.toDTO(aggregateFunction);
            saveData(dto);
        }
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
        aggregateFunction.setIsBuiltIn(false);
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
                .where(AGGREGATE_FUNCTION.ID.ne(aggregateFunction.getId()).when(aggregateFunction.getId() != null))
                .and(AGGREGATE_FUNCTION.NAME.eq(aggregateFunction.getName()).or(AGGREGATE_FUNCTION.DISPLAY_NAME.eq(aggregateFunction.getDisplayName())));
        long count = aggregateFunctionMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(AGGREGATE_FUNCTION_EXIST);
        }
    }

}