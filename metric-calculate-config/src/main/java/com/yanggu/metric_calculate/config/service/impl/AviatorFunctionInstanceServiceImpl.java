package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionInstanceMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorFunctionInstanceMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunction;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionField;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.pojo.req.AviatorFunctionInstanceQueryReq;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamAviatorFunctionInstanceRelationService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionService;
import org.dromara.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.config.enums.ResultCode.*;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamAviatorFunctionInstanceRelationTableDef.AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorFunctionInstanceTableDef.AVIATOR_FUNCTION_INSTANCE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorFunctionTableDef.AVIATOR_FUNCTION;

/**
 * Aviator函数实例 服务层实现。
 */
@Service
public class AviatorFunctionInstanceServiceImpl extends ServiceImpl<AviatorFunctionInstanceMapper, AviatorFunctionInstance> implements AviatorFunctionInstanceService {

    @Autowired
    private AviatorFunctionService aviatorFunctionService;

    @Autowired
    private AviatorFunctionInstanceMapstruct aviatorFunctionInstanceMapstruct;

    @Autowired
    private AviatorFunctionInstanceMapper aviatorFunctionInstanceMapper;

    @Autowired
    private AviatorExpressParamAviatorFunctionInstanceRelationService relationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        AviatorFunctionInstance aviatorFunctionInstance = aviatorFunctionInstanceMapstruct.toEntity(aviatorFunctionInstanceDto);
        check(aviatorFunctionInstance);
        super.save(aviatorFunctionInstance);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        AviatorFunctionInstance aviatorFunctionInstance = aviatorFunctionInstanceMapstruct.toEntity(aviatorFunctionInstanceDto);
        check(aviatorFunctionInstance);
        //如果该实例被使用了则不能修改
        long count = relationService.queryChain()
                .where(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_FUNCTION_INSTANCE_ID.eq(aviatorFunctionInstance.getId()))
                .count();
        if (count > 0) {
            throw new BusinessException(AVIATOR_EXPRESS_PARAM_USE_AVIATOR_FUNCTION_INSTANCE_NOT_DELETE);
        }
        super.updateById(aviatorFunctionInstance);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //Aviator函数使用了就不能删除
        long count = relationService.queryChain()
                .where(AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION.AVIATOR_FUNCTION_INSTANCE_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(AVIATOR_EXPRESS_PARAM_USE_AVIATOR_FUNCTION_INSTANCE_NOT_DELETE);
        }
        super.removeById(id);
    }

    @Override
    public List<AviatorFunctionInstanceDto> listData(AviatorFunctionInstanceQueryReq req) {
        QueryWrapper queryWrapper = buildQueryWrapper(req);
        List<AviatorFunctionInstance> list = aviatorFunctionInstanceMapper.selectListWithRelationsByQuery(queryWrapper);
        return aviatorFunctionInstanceMapstruct.toDTO(list);
    }

    @Override
    public AviatorFunctionInstanceDto queryById(Integer id) {
        AviatorFunctionInstance aviatorFunctionInstance = aviatorFunctionInstanceMapper.selectOneWithRelationsById(id);
        return aviatorFunctionInstanceMapstruct.toDTO(aviatorFunctionInstance);
    }

    @Override
    public Page<AviatorFunctionInstanceDto> pageData(Integer pageNumber,
                                                     Integer pageSize,
                                                     AviatorFunctionInstanceQueryReq req) {
        QueryWrapper queryWrapper = buildQueryWrapper(req);
        Page<AviatorFunctionInstance> page = aviatorFunctionInstanceMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<AviatorFunctionInstanceDto> list = aviatorFunctionInstanceMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    private QueryWrapper buildQueryWrapper(AviatorFunctionInstanceQueryReq req) {
        return QueryWrapper.create()
                .where(AVIATOR_FUNCTION_INSTANCE.DISPLAY_NAME.like(req.getAviatorFunctionInstanceDisplayName()));
    }

    private void check(AviatorFunctionInstance aviatorFunctionInstance) {
        //检查aviatorFunctionId是否存在
        Integer aviatorFunctionId = aviatorFunctionInstance.getAviatorFunctionId();
        AviatorFunction aviatorFunction = aviatorFunctionService.queryChain()
                .where(AVIATOR_FUNCTION.ID.eq(aviatorFunctionId))
                .withRelations()
                .one();

        if (aviatorFunction == null) {
            throw new BusinessException(AVIATOR_FUNCTION_ID_ERROR, aviatorFunctionId);
        }
        //检查param参数是否和字段定义匹配
        List<AviatorFunctionField> aviatorFunctionFieldList = aviatorFunction.getAviatorFunctionFieldList();
        if (aviatorFunctionFieldList == null) {
            aviatorFunctionFieldList = Collections.emptyList();
        }
        List<String> list = aviatorFunctionFieldList.stream()
                .map(AviatorFunctionField::getName)
                .toList();
        Map<String, Object> param = aviatorFunctionInstance.getParam();
        if (MapUtil.isNotEmpty(param)) {
            param.forEach((key, value) -> {
                if (!list.contains(key)) {
                    throw new BusinessException(AVIATOR_FUNCTION_PARAM_ERROR, key);
                }
            });
        }
    }

}