package com.yanggu.metric_calculate.config.base.mapstruct;

import org.dromara.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.List;

/**
 * 将DTO转换成Entity的通用接口
 *
 * @param <D> dto实体类泛型（controller新增、修改请求参数）
 * @param <E> entity实体类泛型（数据库实体类）
 */
public interface DTOToEntityMapstruct<D, E> {

    /**
     * 将DTO转换成Entity
     */
    E dtoToEntity(D dto);

    /**
     * 将DTO列表转换为Entity列表
     */
    default List<E> dtoToEntity(List<D> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            return Collections.emptyList();
        }
        return dtoList.stream().map(this::dtoToEntity).toList();
    }

}