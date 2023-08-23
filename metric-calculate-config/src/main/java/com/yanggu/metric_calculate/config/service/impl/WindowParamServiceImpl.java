package com.yanggu.metric_calculate.config.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapper.WindowParamMapper;
import com.yanggu.metric_calculate.config.pojo.entity.WindowParam;
import com.yanggu.metric_calculate.config.service.WindowParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 窗口相关参数 服务层实现。
 *
 * @author MondayLi
 * @since 2023-07-10
 */
@Service
public class WindowParamServiceImpl extends ServiceImpl<WindowParamMapper, WindowParam> implements WindowParamService {

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveData(WindowParam windowParam) {
        //TODO 完成窗口表相关设计和保存逻辑
        super.save(windowParam);

    }

}