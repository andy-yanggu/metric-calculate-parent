package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.DimensionMapper;
import com.yanggu.metric_calculate.config.mapstruct.DimensionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDto;
import com.yanggu.metric_calculate.config.pojo.entity.Dimension;
import com.yanggu.metric_calculate.config.pojo.req.DimensionQueryReq;
import com.yanggu.metric_calculate.config.service.DimensionService;
import com.yanggu.metric_calculate.config.service.ModelDimensionColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ResultCode.DIMENSION_BINDING_MODEL_COLUMN_NOT_DELETE;
import static com.yanggu.metric_calculate.config.enums.ResultCode.DIMENSION_EXIST;
import static com.yanggu.metric_calculate.config.pojo.entity.table.DimensionTableDef.DIMENSION;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelDimensionColumnTableDef.MODEL_DIMENSION_COLUMN;

/**
 * 维度表 服务层实现。
 */
@Service
public class DimensionServiceImpl extends ServiceImpl<DimensionMapper, Dimension> implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;

    @Autowired
    private DimensionMapstruct dimensionMapstruct;

    @Autowired
    private ModelDimensionColumnService modelDimensionColumnService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(DimensionDto dimensionDto) {
        Dimension dimension = dimensionMapstruct.toEntity(dimensionDto);
        checkExist(dimension);
        super.save(dimension);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(DimensionDto dimensionDto) {
        Dimension dimension = dimensionMapstruct.toEntity(dimensionDto);
        checkExist(dimension);
        super.updateById(dimension);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {
        //如果维度和宽表字段绑定了不能删除
        long count = modelDimensionColumnService.queryChain()
                .where(MODEL_DIMENSION_COLUMN.DIMENSION_ID.eq(id))
                .count();
        if (count > 0) {
            throw new BusinessException(DIMENSION_BINDING_MODEL_COLUMN_NOT_DELETE);
        }
        super.removeById(id);
    }

    @Override
    public List<DimensionDto> listData(DimensionQueryReq req) {
        QueryWrapper queryWrapper = buildDimensionQueryWrapper(req);
        List<Dimension> dimensions = dimensionMapper.selectListByQuery(queryWrapper);
        return dimensionMapstruct.toDTO(dimensions);
    }

    @Override
    public DimensionDto queryById(Integer id) {
        Dimension dimension = dimensionMapper.selectOneById(id);
        return dimensionMapstruct.toDTO(dimension);
    }

    @Override
    public Page<DimensionDto> pageData(Integer pageNumber, Integer pageSize, DimensionQueryReq req) {
        QueryWrapper queryWrapper = buildDimensionQueryWrapper(req);
        Page<Dimension> page = dimensionMapper.paginate(pageNumber, pageSize, queryWrapper);
        List<DimensionDto> list = dimensionMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    private void checkExist(Dimension dimension) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(DIMENSION.ID.ne(dimension.getId()))
                .and(DIMENSION.NAME.eq(dimension.getName()).or(DIMENSION.DISPLAY_NAME.eq(dimension.getDisplayName())));
        long count = dimensionMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(DIMENSION_EXIST);
        }
    }

    private QueryWrapper buildDimensionQueryWrapper(DimensionQueryReq req) {
        return QueryWrapper.create()
                .where(DIMENSION.NAME.like(req.getDimensionName()))
                .and(DIMENSION.DISPLAY_NAME.like(req.getDimensionDisplayName()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

}