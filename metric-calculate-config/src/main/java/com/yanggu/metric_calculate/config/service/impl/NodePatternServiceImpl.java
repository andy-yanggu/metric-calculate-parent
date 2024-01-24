package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.NodePatternMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParamEntity;
import com.yanggu.metric_calculate.config.pojo.entity.ModelColumnEntity;
import com.yanggu.metric_calculate.config.pojo.entity.NodePatternAviatorExpressParamRelationEntity;
import com.yanggu.metric_calculate.config.pojo.entity.NodePatternEntity;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.NodePatternAviatorExpressParamRelationService;
import com.yanggu.metric_calculate.config.service.NodePatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.pojo.entity.table.NodePatternAviatorExpressParamRelationTableDef.NODE_PATTERN_AVIATOR_EXPRESS_PARAM_RELATION;

/**
 * CEP匹配配置数据 服务层实现。
 */
@Service
public class NodePatternServiceImpl extends ServiceImpl<NodePatternMapper, NodePatternEntity> implements NodePatternService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private NodePatternAviatorExpressParamRelationService nodePatternAviatorExpressParamRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(NodePatternEntity nodePattern, List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(nodePattern);

        AviatorExpressParamEntity matchExpressParam = nodePattern.getMatchExpressParam();
        matchExpressParam.setModelColumnList(modelColumnList);
        aviatorExpressParamService.saveDataByModelColumn(matchExpressParam);
        NodePatternAviatorExpressParamRelationEntity relation = new NodePatternAviatorExpressParamRelationEntity();
        relation.setNodePatternId(nodePattern.getId());
        relation.setAviatorExpressParamId(matchExpressParam.getId());
        nodePatternAviatorExpressParamRelationService.save(relation);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(NodePatternEntity nodePattern) {
        Integer nodePatternId = nodePattern.getId();
        super.removeById(nodePatternId);
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(NODE_PATTERN_AVIATOR_EXPRESS_PARAM_RELATION.NODE_PATTERN_ID.eq(nodePatternId))
                .and(NODE_PATTERN_AVIATOR_EXPRESS_PARAM_RELATION.AVIATOR_EXPRESS_PARAM_ID.eq(nodePattern.getMatchExpressParam().getId()));
        nodePatternAviatorExpressParamRelationService.remove(queryWrapper);
    }

}
