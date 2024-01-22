package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.DimensionMapper;
import com.yanggu.metric_calculate.config.mapstruct.DimensionMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.DimensionDTO;
import com.yanggu.metric_calculate.config.pojo.entity.DimensionEntity;
import com.yanggu.metric_calculate.config.pojo.query.DimensionQuery;
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
public class DimensionServiceImpl extends ServiceImpl<DimensionMapper, DimensionEntity> implements DimensionService {

    @Autowired
    private DimensionMapper dimensionMapper;

    @Autowired
    private DimensionMapstruct dimensionMapstruct;

    @Autowired
    private ModelDimensionColumnService modelDimensionColumnService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(DimensionDTO dimensionDto) {
        DimensionEntity dimension = dimensionMapstruct.toEntity(dimensionDto);
        checkExist(dimension);
        super.save(dimension);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(DimensionDTO dimensionDto) {
        DimensionEntity dimension = dimensionMapstruct.toEntity(dimensionDto);
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
    public List<DimensionDTO> listData(DimensionQuery req) {
        QueryWrapper queryWrapper = buildDimensionQueryWrapper(req);
        List<DimensionEntity> dimensions = dimensionMapper.selectListByQuery(queryWrapper);
        return dimensionMapstruct.toDTO(dimensions);
    }

    @Override
    public DimensionDTO queryById(Integer id) {
        DimensionEntity dimension = dimensionMapper.selectOneById(id);
        return dimensionMapstruct.toDTO(dimension);
    }

    @Override
    public Page<DimensionDTO> pageData(Integer pageNumber, Integer pageSize, DimensionQuery req) {
        QueryWrapper queryWrapper = buildDimensionQueryWrapper(req);
        Page<DimensionEntity> page = dimensionMapper.paginate(pageNumber, pageSize, queryWrapper);
        List<DimensionDTO> list = dimensionMapstruct.toDTO(page.getRecords());
        return new Page<>(list, pageNumber, pageSize, page.getTotalRow());
    }

    private void checkExist(DimensionEntity dimension) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(DIMENSION.ID.ne(dimension.getId()))
                .and(DIMENSION.NAME.eq(dimension.getName()).or(DIMENSION.DISPLAY_NAME.eq(dimension.getDisplayName())));
        long count = dimensionMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(DIMENSION_EXIST);
        }
    }

    private QueryWrapper buildDimensionQueryWrapper(DimensionQuery req) {
        return QueryWrapper.create()
                .where(DIMENSION.NAME.like(req.getDimensionName()))
                .and(DIMENSION.DISPLAY_NAME.like(req.getDimensionDisplayName()))
                .and(DIMENSION.ID.in(req.getIdList()))
                .orderBy(req.getOrderByColumnName(), req.getAsc());
    }

}