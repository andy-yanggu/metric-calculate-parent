package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.WindowParamMapper;
import com.yanggu.metric_calculate.config.domain.entity.*;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.NodePatternService;
import com.yanggu.metric_calculate.config.service.WindowParamService;
import com.yanggu.metric_calculate.config.service.WindowParamStatusExpressParamListRelationService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.domain.entity.table.WindowParamStatusExpressParamListRelationTableDef.WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION;

/**
 * 窗口相关参数 服务层实现。
 */
@Service
public class WindowParamServiceImpl extends ServiceImpl<WindowParamMapper, WindowParamEntity> implements WindowParamService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private WindowParamStatusExpressParamListRelationService windowParamStatusExpressParamListRelationService;

    @Autowired
    private NodePatternService nodePatternService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(WindowParamEntity windowParam, List<ModelColumnEntity> modelColumnList) throws Exception {
        super.save(windowParam);

        //保存状态窗口表达式列表
        List<AviatorExpressParamEntity> statusExpressParamList = windowParam.getStatusExpressParamList();
        if (CollUtil.isNotEmpty(statusExpressParamList)) {
            for (AviatorExpressParamEntity aviatorExpressParam : statusExpressParamList) {
                aviatorExpressParam.setModelColumnList(modelColumnList);
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                WindowParamStatusExpressParamListRelationEntity relation = new WindowParamStatusExpressParamListRelationEntity();
                relation.setWindowParamId(windowParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                windowParamStatusExpressParamListRelationService.save(relation);
            }
        }

        List<NodePatternEntity> nodePatternList = windowParam.getNodePatternList();
        if (CollUtil.isNotEmpty(nodePatternList)) {
            for (NodePatternEntity nodePattern : nodePatternList) {
                nodePattern.setWindowParamId(windowParam.getId());
                nodePatternService.saveData(nodePattern, modelColumnList);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(WindowParamEntity windowParam) {
        Integer windowParamId = windowParam.getId();
        super.removeById(windowParamId);
        List<AviatorExpressParamEntity> statusExpressParamList = windowParam.getStatusExpressParamList();
        if (CollUtil.isNotEmpty(statusExpressParamList)) {
            for (AviatorExpressParamEntity aviatorExpressParam : statusExpressParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
            List<Integer> list = statusExpressParamList.stream()
                    .map(AviatorExpressParamEntity::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION.WINDOW_PARAM_ID.eq(windowParamId))
                    .and(WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION.AVIATOR_EXPRESS_PARAM_ID.in(list));
            windowParamStatusExpressParamListRelationService.remove(queryWrapper);
        }
        List<NodePatternEntity> nodePatternList = windowParam.getNodePatternList();
        if (CollUtil.isNotEmpty(nodePatternList)) {
            for (NodePatternEntity nodePattern : nodePatternList) {
                nodePatternService.deleteData(nodePattern);
            }
        }
    }

}