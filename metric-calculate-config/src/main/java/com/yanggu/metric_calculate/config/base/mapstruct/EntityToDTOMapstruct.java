package com.yanggu.metric_calculate.config.base.mapstruct;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.List;

/**
 * 将Entity转换成DTO的通用接口
 *
 * @param <E> entity实体类泛型（数据库实体类）
 * @param <D> dto实体类泛型（controller新增、修改请求参数）
 */
public interface EntityToDTOMapstruct<E, D> {

    /**
     * 将Entity转换成DTO
     */
    D entityToDTO(E entity);

    /**
     * 将DTO列表转换为Entity列表
     */
    default List<D> entityToDTO(List<E> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(this::entityToDTO).toList();
    }

}