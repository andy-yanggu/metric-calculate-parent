package com.yanggu.metric_calculate.config.base.mapstruct;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.List;

/**
 * 将Entity转换成VO的通用接口
 *
 * @param <E> entity实体类泛型（数据库实体类）
 * @param <V> vo实体类泛型（controller返回参数）
 */
public interface EntityToVOMapstruct<E, V> {

    /**
     * 将Entity转换成VO
     */
    V entityToVO(E entity);

    /**
     * 将Entity列表转换成VO列表
     */
    default List<V> entityToVO(List<E> entityList) {
        if (CollUtil.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(this::entityToVO).toList();
    }

}