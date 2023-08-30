package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.WindowParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.AviatorExpressParam;
import com.yanggu.metric_calculate.config.pojo.entity.NodePattern;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParamStatusExpressParamListRelation;
import com.yanggu.metric_calculate.config.service.AviatorExpressParamService;
import com.yanggu.metric_calculate.config.service.NodePatternService;
import com.yanggu.metric_calculate.config.service.WindowParamService;
import com.yanggu.metric_calculate.config.service.WindowParamStatusExpressParamListRelationService;
import org.dromara.hutool.core.collection.CollUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.yanggu.metric_calculate.config.pojo.entity.table.WindowParamStatusExpressParamListRelationTableDef.WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION;

/**
 * 窗口相关参数 服务层实现。
 */
@Service
public class WindowParamServiceImpl extends ServiceImpl<WindowParamMapper, WindowParam> implements WindowParamService {

    @Autowired
    private AviatorExpressParamService aviatorExpressParamService;

    @Autowired
    private WindowParamStatusExpressParamListRelationService windowParamStatusExpressParamListRelationService;

    @Autowired
    private NodePatternService nodePatternService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(WindowParam windowParam) throws Exception {
        super.save(windowParam);

        //保存状态窗口表达式列表
        List<AviatorExpressParam> statusExpressParamList = windowParam.getStatusExpressParamList();
        if (CollUtil.isNotEmpty(statusExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : statusExpressParamList) {
                aviatorExpressParamService.saveDataByModelColumn(aviatorExpressParam);
                WindowParamStatusExpressParamListRelation relation = new WindowParamStatusExpressParamListRelation();
                relation.setWindowParamId(windowParam.getId());
                relation.setAviatorExpressParamId(aviatorExpressParam.getId());
                windowParamStatusExpressParamListRelationService.save(relation);
            }
        }

        List<NodePattern> nodePatternList = windowParam.getNodePatternList();
        if (CollUtil.isNotEmpty(nodePatternList)) {
            for (NodePattern nodePattern : nodePatternList) {
                nodePattern.setWindowParamId(windowParam.getId());
                nodePatternService.saveData(nodePattern);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteData(WindowParam windowParam) {
        Integer windowParamId = windowParam.getId();
        super.removeById(windowParamId);
        List<AviatorExpressParam> statusExpressParamList = windowParam.getStatusExpressParamList();
        if (CollUtil.isNotEmpty(statusExpressParamList)) {
            for (AviatorExpressParam aviatorExpressParam : statusExpressParamList) {
                aviatorExpressParamService.deleteData(aviatorExpressParam);
            }
            List<Integer> list = statusExpressParamList.stream()
                    .map(AviatorExpressParam::getId)
                    .toList();
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION.WINDOW_PARAM_ID.eq(windowParamId))
                    .and(WINDOW_PARAM_STATUS_EXPRESS_PARAM_LIST_RELATION.AVIATOR_EXPRESS_PARAM_ID.in(list));
            windowParamStatusExpressParamListRelationService.remove(queryWrapper);
        }
        List<NodePattern> nodePatternList = windowParam.getNodePatternList();
        if (CollUtil.isNotEmpty(nodePatternList)) {
            for (NodePattern nodePattern : nodePatternList) {
                nodePatternService.deleteData(nodePattern);
            }
        }
    }

}