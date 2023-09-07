package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AviatorFunctionInstanceMapper;
import com.yanggu.metric_calculate.config.mapstruct.AviatorFunctionInstanceMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AviatorFunctionInstanceDto;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorFunctionInstance;
import com.yanggu.metric_calculate.config.pojo.req.AviatorFunctionInstanceQueryReq;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamAviatorFunctionInstanceRelationService;
import com.yanggu.metric_calculate.config.service.AviatorFunctionInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ResultCode.AVIATOR_EXPRESS_PARAM_USE_AVIATOR_FUNCTION_INSTANCE_NOT_DELETE;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorExpressParamAviatorFunctionInstanceRelationTableDef.AVIATOR_EXPRESS_PARAM_AVIATOR_FUNCTION_INSTANCE_RELATION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AviatorFunctionInstanceTableDef.AVIATOR_FUNCTION_INSTANCE;

/**
 * Aviator函数实例 服务层实现。
 */
@Service
public class AviatorFunctionInstanceServiceImpl extends ServiceImpl<AviatorFunctionInstanceMapper, AviatorFunctionInstance> implements AviatorFunctionInstanceService {

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
        super.save(aviatorFunctionInstance);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(AviatorFunctionInstanceDto aviatorFunctionInstanceDto) {
        AviatorFunctionInstance aviatorFunctionInstance = aviatorFunctionInstanceMapstruct.toEntity(aviatorFunctionInstanceDto);
        super.updateById(aviatorFunctionInstance);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //Aviator表达式使用了就不能删除
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
    public Page<AviatorFunctionInstanceDto> pageData(Integer pageNumber, Integer pageSize, AviatorFunctionInstanceQueryReq req) {
        QueryWrapper queryWrapper = buildQueryWrapper(req);
        Page<AviatorFunctionInstance> page = aviatorFunctionInstanceMapper.paginateWithRelations(pageNumber, pageSize, queryWrapper);
        List<AviatorFunctionInstanceDto> list = aviatorFunctionInstanceMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    private QueryWrapper buildQueryWrapper(AviatorFunctionInstanceQueryReq req) {
        return QueryWrapper.create()
                .where(AVIATOR_FUNCTION_INSTANCE.DISPLAY_NAME.like(req.getAviatorFunctionInstanceDisplayName()));
    }

}