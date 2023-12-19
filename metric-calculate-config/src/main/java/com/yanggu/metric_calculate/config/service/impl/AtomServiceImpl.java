package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.exceptionhandler.BusinessException;
import com.yanggu.metric_calculate.config.mapper.AtomMapper;
import com.yanggu.metric_calculate.config.mapstruct.AtomMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.AtomDto;
import com.yanggu.metric_calculate.config.pojo.entity.AggregateFunctionParam;
import com.yanggu.metric_calculate.config.pojo.entity.Atom;
import com.yanggu.metric_calculate.config.pojo.entity.AtomAggregateFunctionParamRelation;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumn;
import com.yanggu.metric_calculate.config.pojo.req.AtomQueryReq;
import com.yanggu.metric_calculate.config.service.AggregateFunctionParamService;
import com.yanggu.metric_calculate.config.service.AtomAggregateFunctionParamRelationService;
import com.yanggu.metric_calculate.config.service.AtomService;
import com.yanggu.metric_calculate.config.service.ModelColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.enums.ResultCode.ATOM_EXIST;
import static com.yanggu.metric_calculate.config.pojo.entity.table.AtomTableDef.ATOM;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelColumnTableDef.MODEL_COLUMN;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTableDef.MODEL;
import static com.yanggu.metric_calculate.config.pojo.entity.table.ModelTimeColumnTableDef.MODEL_TIME_COLUMN;

/**
 * 原子指标 服务层实现。
 */
@Service
public class AtomServiceImpl extends ServiceImpl<AtomMapper, Atom> implements AtomService {

    @Autowired
    private AtomMapper atomMapper;

    @Autowired
    private AtomMapstruct  atomMapstruct;

    @Autowired
    private AggregateFunctionParamService aggregateFunctionParamService;

    @Autowired
    private ModelColumnService modelColumnService;

    @Autowired
    private AtomAggregateFunctionParamRelationService atomAggregateFunctionParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(AtomDto atomDto) throws Exception {
        Atom atom = atomMapstruct.toEntity(atomDto);
        checkExist(atom);
        this.save(atom);
        AggregateFunctionParam aggregateFunctionParam = atom.getAggregateFunctionParam();
        //根据宽表id查询对应的宽表字段
        List<ModelColumn> modelColumnList = modelColumnService.queryChain()
                .from(MODEL_COLUMN)
                .where(MODEL_COLUMN.MODEL_ID.eq(atom.getModelId()))
                .list();
        aggregateFunctionParamService.saveData(aggregateFunctionParam, modelColumnList);
        AtomAggregateFunctionParamRelation atomAggregateFunctionParamRelation = new AtomAggregateFunctionParamRelation();
        atomAggregateFunctionParamRelation.setAtomId(atom.getId());
        atomAggregateFunctionParamRelation.setAggregateFunctionParamId(aggregateFunctionParam.getId());
        atomAggregateFunctionParamRelationService.save(atomAggregateFunctionParamRelation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateData(AtomDto atomDto) throws Exception {
        Atom atom = atomMapstruct.toEntity(atomDto);
        checkExist(atom);
        this.updateById(atom);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Integer id) {

    }

    @Override
    public AtomDto queryById(Integer id) {
        Atom atom = atomMapper.selectOneWithRelationsById(id);
        return atomMapstruct.toDTO(atom);
    }

    @Override
    public List<AtomDto> listData(AtomQueryReq atomQueryReq) {
        QueryWrapper queryWrapper = buildAtomQueryWrapper(atomQueryReq);
        List<Atom> atomList = atomMapper.selectListWithRelationsByQuery(queryWrapper);
        return atomMapstruct.toDTO(atomList);
    }

    @Override
    public Page<AtomDto> pageQuery(Integer pageNumber, Integer pageSize, AtomQueryReq atomQueryReq) {
        QueryWrapper queryWrapper = buildAtomQueryWrapper(atomQueryReq);
        Page<Atom> page = atomMapper.paginate(pageNumber, pageSize, queryWrapper);
        List<AtomDto> dtoList = atomMapstruct.toDTO(page.getRecords());
        return new Page<>(dtoList, pageNumber, pageSize, page.getTotalRow());
    }

    private void checkExist(Atom atom) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                        .from(ATOM)
                       .where(ATOM.ID.ne(atom.getId()))
                       .and(ATOM.NAME.eq(atom.getName()).or(ATOM.DISPLAY_NAME.eq(atom.getDisplayName())));
        long count = atomMapper.selectCountByQuery(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ATOM_EXIST);
        }
    }

    private QueryWrapper buildAtomQueryWrapper(AtomQueryReq atomQueryReq) {
        return QueryWrapper.create()
                .select(ATOM.DEFAULT_COLUMNS)
                .from(ATOM)
                .innerJoin(MODEL).on(ATOM.MODEL_ID.eq(MODEL.ID))
                .innerJoin(MODEL_TIME_COLUMN).on(ATOM.MODEL_TIME_COLUMN_ID.eq(MODEL_TIME_COLUMN.ID))
                .innerJoin(MODEL_COLUMN).on(MODEL_TIME_COLUMN.MODEL_COLUMN_ID.eq(MODEL_COLUMN.ID))
                //模糊查询原子指标名
                .where(ATOM.NAME.like(atomQueryReq.getAtomName()))
                //模糊查询原子指标显示名
                .and(ATOM.DISPLAY_NAME.like(atomQueryReq.getAtomDisplayName()))
                //模糊查询所属宽表名称
                .and(MODEL.NAME.like(atomQueryReq.getModelName()))
                //模糊所属宽表中文名
                .and(MODEL.DISPLAY_NAME.like(atomQueryReq.getModelDisplayName()))
                //模糊查询对应时间字段格式
                .and(MODEL_TIME_COLUMN.TIME_FORMAT.like(atomQueryReq.getTimeFormat()))
                //模糊查询时间字段名
                .and(MODEL_COLUMN.NAME.like(atomQueryReq.getTimeColumnName()))
                //模糊查询时间字段中文名
                .and(MODEL_COLUMN.DISPLAY_NAME.like(atomQueryReq.getTimeColumnDisplayName()))
                ;

    }

}
