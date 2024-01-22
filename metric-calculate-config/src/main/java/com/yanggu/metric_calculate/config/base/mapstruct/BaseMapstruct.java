package com.yanggu.metric_calculate.config.base.mapstruct;

/**
 * Mapstruct转换基类，提供Entity、VO、DTO之间基本的转换
 * <p>适用于Entity、VO、DTO都存在的情况</p>
 *
 * @param <E> entity实体类泛型（数据库实体类）
 * @param <V> vo实体类泛型（controller返回参数）
 * @param <D> dto实体类泛型（controller新增、修改请求参数）
 */
public interface BaseMapstruct<E, V, D> extends DTOToEntityMapstruct<D, E>, PageEntityMapstruct<E, V>, PageVOMapstruct<V> {
}