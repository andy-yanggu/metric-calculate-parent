package com.yanggu.metric_calculate.config.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.yanggu.metric_calculate.config.mapstruct.TimeColumnMapstruct;
import com.yanggu.metric_calculate.config.pojo.dto.ModelColumnDto;
import com.yanggu.metric_calculate.config.pojo.entity.TimeColumn;
import com.yanggu.metric_calculate.config.mapper.TimeColumnMapper;
import com.yanggu.metric_calculate.config.service.TimeColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 时间字段 服务层实现。
 */
@Service
public class TimeColumnServiceImpl extends ServiceImpl<TimeColumnMapper, TimeColumn> implements TimeColumnService {

    @Autowired
    private TimeColumnMapstruct timeColumnMapstruct;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveTimeColumn(List<ModelColumnDto> modelColumnDtoList) {
        if (CollUtil.isEmpty(modelColumnDtoList)) {
            return;
        }
        List<TimeColumn> timeColumnList = modelColumnDtoList.stream()
                .filter(tempModelColumn -> tempModelColumn.getTimeColumn() != null)
                .map(tempModelColumn -> {
                    TimeColumn timeColumn = timeColumnMapstruct.toEntity(tempModelColumn.getTimeColumn());
                    timeColumn.setUserId(tempModelColumn.getUserId());
                    timeColumn.setModelColumnId(tempModelColumn.getModelId());
                    return timeColumn;
                })
                .collect(Collectors.toList());

        if (CollUtil.isNotEmpty(timeColumnList)) {
            saveBatch(timeColumnList);
        }
    }

}