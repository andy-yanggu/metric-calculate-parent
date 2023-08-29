package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorFunctionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionField;
import com.yanggu.metric_calculate.config.pojo.entity.JarStore;
import com.yanggu.metric_calculate.config.service.AviatorFunctionFieldService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import com.yanggu.metric_calculate.config.service.JarStoreService;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionAnnotation;
import com.yanggu.metric_calculate.core.aviator_function.AviatorFunctionFieldAnnotation;
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

import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_FUNCTION_CLASS_NOT_HAVE_ANNOTATION;
import static com.yanggu.metric_calculate.core.function_factory.AviatorFunctionFactory.CLASS_FILTER;

/**
 * Aviator函数 服务层实现。
 */
@Service
public class AviatorFunctionServiceImpl extends ServiceImpl<AviatorFunctionMapper, AviatorFunction> implements AviatorFunctionService {

    @Autowired
    private JarStoreService jarStoreService;

    @Autowired
    private AviatorFunctionMapstruct aviatorFunctionMapstruct;

    @Autowired
    private AviatorFunctionFieldService aviatorFunctionFieldService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AviatorFunctionDto aviatorFunctionDto) {
        AviatorFunction aviatorFunction = aviatorFunctionMapstruct.toEntity(aviatorFunctionDto);
        super.save(aviatorFunction);
        List<AviatorFunctionField> aviatorFunctionFieldList = aviatorFunction.getAviatorFunctionFieldList();
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
        List<String> jarPathList = Collections.singletonList(dest.getAbsolutePath());
        List<AviatorFunction> aviatorFunctionList = new ArrayList<>();
        Consumer<Class<?>> consumer = clazz -> aviatorFunctionList.add(buildAviatorFunction(clazz));
        FunctionFactory.loadClassFromJar(jarPathList, CLASS_FILTER, consumer);
        if (CollUtil.isEmpty(aviatorFunctionList)) {
            return;
        }
        JarStore jarStore = new JarStore();
        //TODO 这里可以根据需要将jar文件保存到远程文件服务器中
        jarStore.setJarUrl(dest.toURI().toURL().getPath());
        jarStoreService.save(jarStore);
        for (AviatorFunction aviatorFunction : aviatorFunctionList) {
            aviatorFunction.setJarStoreId(jarStore.getId());
            AviatorFunctionDto dto = aviatorFunctionMapstruct.toDTO(aviatorFunction);
            this.saveData(dto);
        }
    }

    private static AviatorFunction buildAviatorFunction(Class<?> clazz) {
        AviatorFunctionAnnotation annotation = clazz.getAnnotation(AviatorFunctionAnnotation.class);
        if (annotation == null) {
            throw new BusinessException(AVIATOR_FUNCTION_CLASS_NOT_HAVE_ANNOTATION, clazz.getName());
        }
        String name = annotation.name();
        AviatorFunction aviatorFunction = new AviatorFunction();
        aviatorFunction.setName(name);
        aviatorFunction.setDisplayName(annotation.displayName());
        aviatorFunction.setDescription(annotation.description());
        aviatorFunction.setIsBuiltIn(false);
        //设置聚合函数字段
        List<UdafCustomParamData> udafCustomParamList = UdafCustomParamDataUtil.getUdafCustomParamList(clazz, AviatorFunctionFieldAnnotation.class);
        if (CollUtil.isNotEmpty(udafCustomParamList)) {
            AtomicInteger index = new AtomicInteger(0);
            List<AviatorFunctionField> list = udafCustomParamList.stream()
                    .map(temp -> {
                        AviatorFunctionField aviatorFunctionField = new AviatorFunctionField();
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

}