package com.yanggu.metric_calculate.config.service.impl;

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

}