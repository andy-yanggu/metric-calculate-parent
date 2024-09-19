package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.tenant.TenantManager;
import com.mybatisflex.core.util.UpdateEntity;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;
import com.yanggu.metric_calculate.config.domain.dto.AviatorFunctionDTO;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionEntity;
import com.yanggu.metric_calculate.config.domain.entity.AviatorFunctionFieldEntity;
import com.yanggu.metric_calculate.config.domain.entity.JarStoreEntity;
import com.yanggu.metric_calculate.config.domain.query.AviatorFunctionQuery;
import com.yanggu.metric_calculate.config.domain.vo.AviatorFunctionVO;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorFunctionMapstruct;
import com.yanggu.metric_calculate.config.service.AviatorFunctionFieldService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import com.yanggu.metric_calculate.config.service.JarStoreService;
import com.yanggu.metric_calculate.core.aviator_function.AbstractUdfAviatorFunction;
import com.yanggu.metric_calculate.core.aviator_function.annotation.AviatorFunctionAnnotation;
import com.yanggu.metric_calculate.core.aviator_function.annotation.AviatorFunctionFieldAnnotation;
import com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory;
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

import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorFunctionFieldTableDef.AVIATOR_FUNCTION_FIELD;
import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorFunctionInstanceTableDef.AVIATOR_FUNCTION_INSTANCE;
import static com.yanggu.metric_calculate.config.domain.entity.table.AviatorFunctionTableDef.AVIATOR_FUNCTION;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_FUNCTION_CLASS_NOT_HAVE_ANNOTATION;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_FUNCTION_EXIST;
import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_FUNCTION_HAS_INSTANCE;
import static com.yanggu.metric_calculate.config.enums.ResultCode.JAR_STORE_ID_NULL;
import static com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory.CLASS_FILTER;

/**
 * Aviator函数 服务层实现。
 */
@Service
public class AviatorFunctionServiceImpl extends ServiceImpl<AviatorFunctionMapper, AviatorFunctionEntity> implements AviatorFunctionService {

    @Autowired
    private AviatorFunctionMapper aviatorFunctionMapper;

    @Autowired
    private JarStoreService jarStoreService;

    @Autowired
    private AviatorFunctionMapstruct aviatorFunctionMapstruct;

    @Autowired
    private AviatorFunctionFieldService aviatorFunctionFieldService;

    @Autowired
    private AviatorFunctionInstanceService aviatorFunctionInstanceService;

    private static AviatorFunctionEntity buildAviatorFunction(Class<?> clazz) {
        AviatorFunctionAnnotation annotation = clazz.getAnnotation(AviatorFunctionAnnotation.class);
        if (annotation == null) {
            throw new BusinessException(AVIATOR_FUNCTION_CLASS_NOT_HAVE_ANNOTATION, clazz.getName());
        }
        AviatorFunctionEntity aviatorFunction = new AviatorFunctionEntity();
        aviatorFunction.setName(annotation.name());
        aviatorFunction.setDisplayName(annotation.displayName());
        aviatorFunction.setDescription(annotation.description());
        //设置聚合函数字段
        List<UdafCustomParamData> udafCustomParamList = UdafCustomParamDataUtil.getUdafCustomParamList(clazz, AviatorFunctionFieldAnnotation.class);
        if (CollUtil.isNotEmpty(udafCustomParamList)) {
            AtomicInteger index = new AtomicInteger(0);
            List<AviatorFunctionFieldEntity> list = udafCustomParamList.stream()
                    .map(temp -> {
                        AviatorFunctionFieldEntity aviatorFunctionField = new AviatorFunctionFieldEntity();
                        aviatorFunctionField.setName(temp.getName());
                        aviatorFunctionField.setDisplayName(temp.getDisplayName());
                        aviatorFunctionField.setDescription(temp.getDescription());
                        aviatorFunctionField.setSort(index.incrementAndGet());
                        return aviatorFunctionField;
                    })
                    .toList();
            aviatorFunction.setAviatorFunctionFieldList(list);
        }
        return aviatorFunction;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AviatorFunctionDTO aviatorFunctionDto) throws Exception {
        AviatorFunctionEntity aviatorFunction;
        if (Boolean.TRUE.equals(aviatorFunctionDto.getIsBuiltIn())) {
            AviatorFunctionFactory aviatorFunctionFactory = new AviatorFunctionFactory();
            aviatorFunctionFactory.init();
            Class<? extends AbstractUdfAviatorFunction> clazz = aviatorFunctionFactory.getClazz(aviatorFunctionDto.getName());
            aviatorFunction = buildAviatorFunction(clazz);
            aviatorFunction.setIsBuiltIn(true);
        } else {
            if (aviatorFunctionDto.getJarStoreId() == null) {
                throw new BusinessException(JAR_STORE_ID_NULL);
            }
            aviatorFunction = aviatorFunctionMapstruct.dtoToEntity(aviatorFunctionDto);
        }
        checkExist(aviatorFunction);
        super.save(aviatorFunction);
        List<AviatorFunctionFieldEntity> aviatorFunctionFieldList = aviatorFunction.getAviatorFunctionFieldList();
        if (CollUtil.isNotEmpty(aviatorFunctionFieldList)) {
            aviatorFunctionFieldList.forEach(temp -> temp.setAviatorFunctionId(aviatorFunction.getId()));
            aviatorFunctionFieldService.saveBatch(aviatorFunctionFieldList);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void jarSave(MultipartFile file) throws Exception {
        File dest = new File(SystemUtil.getTmpDirPath() + File.separatorChar + IdUtil.fastSimpleUUID());
        file.transferTo(dest);
        List<AviatorFunctionEntity> aviatorFunctionList = new ArrayList<>();
        Consumer<Class<?>> consumer = clazz -> aviatorFunctionList.add(buildAviatorFunction(clazz));
        FunctionFactory.loadClassFromJar(Collections.singletonList(dest.getAbsolutePath()), CLASS_FILTER, consumer);
        if (CollUtil.isEmpty(aviatorFunctionList)) {
            return;
        }
        JarStoreEntity jarStore = new JarStoreEntity();
        //TODO 这里可以根据需要将jar文件保存到远程文件服务器中
        jarStore.setJarUrl(dest.toURI().toURL().getPath());
        jarStoreService.save(jarStore);
        for (AviatorFunctionEntity aviatorFunction : aviatorFunctionList) {
            aviatorFunction.setJarStoreId(jarStore.getId());
            aviatorFunction.setIsBuiltIn(false);
            //TODO 这里需要将Aviator函数保存到数据库中
            //AviatorFunctionDTO dto = aviatorFunctionMapstruct.entityToVO(aviatorFunction);
            //this.saveData(dto);
        }
    }

    @Override
    public void updateData(AviatorFunctionDTO aviatorFunctionDto) {
        //只允许修改描述信息
        AviatorFunctionEntity aviatorFunction = UpdateEntity.of(AviatorFunctionEntity.class, aviatorFunctionDto.getId());
        aviatorFunction.setDescription(aviatorFunctionDto.getDescription());
        aviatorFunctionMapper.update(aviatorFunction);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //检查是否该函数下是否有实例
        long count = aviatorFunctionInstanceService.queryChain()
                .where(AVIATOR_FUNCTION_INSTANCE.AVIATOR_FUNCTION_ID.eq(id)).count();
        if (count > 0) {
            throw new BusinessException(AVIATOR_FUNCTION_HAS_INSTANCE);
        }
        //删除Aviator函数和Aviator函数字段
        removeById(id);
        QueryWrapper idQueryWrapper = QueryWrapper.create()
                .where(AVIATOR_FUNCTION_FIELD.AVIATOR_FUNCTION_ID.eq(id));
        aviatorFunctionFieldService.remove(idQueryWrapper);
    }

    @Override
    public List<AviatorFunctionVO> listData(AviatorFunctionQuery req) {
        QueryWrapper queryWrapper = buildAviatorFunctionQueryWrapper(req);
        List<AviatorFunctionEntity> aviatorFunctions = aviatorFunctionMapper.selectListWithRelationsByQuery(queryWrapper);
        return aviatorFunctionMapstruct.entityToVO(aviatorFunctions);
    }

    @Override
    public AviatorFunctionVO queryById(Integer id) {
        AviatorFunctionEntity aviatorFunction = aviatorFunctionMapper.selectOneWithRelationsById(id);
        return aviatorFunctionMapstruct.entityToVO(aviatorFunction);
    }

    @Override
    public PageVO<AviatorFunctionVO> pageData(AviatorFunctionQuery req) {
        QueryWrapper queryWrapper = buildAviatorFunctionQueryWrapper(req);
        aviatorFunctionMapper.paginateWithRelations(req, queryWrapper);
        return aviatorFunctionMapstruct.entityToPageVO(req);
    }

    private QueryWrapper buildAviatorFunctionQueryWrapper(AviatorFunctionQuery req) {
        return QueryWrapper.create()
                .where(AVIATOR_FUNCTION.NAME.like(req.getAviatorFunctionName()))
                .and(AVIATOR_FUNCTION.DISPLAY_NAME.like(req.getAviatorFunctionDisplayName()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

    /**
     * 检查name、displayName是否重复
     *
     * @param aviatorFunction
     */
    private void checkExist(AviatorFunctionEntity aviatorFunction) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                //当id存在时为更新
                .where(AVIATOR_FUNCTION.ID.ne(aviatorFunction.getId()))
                .and(AVIATOR_FUNCTION.NAME.eq(aviatorFunction.getName()).or(AVIATOR_FUNCTION.DISPLAY_NAME.eq(aviatorFunction.getDisplayName())));
        long count;
        //内置的Aviator函数不需要where userId
        if (Boolean.TRUE.equals(aviatorFunction.getIsBuiltIn())) {
            count = TenantManager.withoutTenantCondition(() -> aviatorFunctionMapper.selectCountByQuery(queryWrapper));
        } else {
            count = aviatorFunctionMapper.selectCountByQuery(queryWrapper);
        }
        if (count > 0) {
            throw new BusinessException(AVIATOR_FUNCTION_EXIST);
        }
    }

}
