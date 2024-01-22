package com.yanggu.metric_calculate.config.base.mapstruct;

/**
 * Mapstruct转换基类，提供Entity和VO之间基本的转换
 * <p>适用于DTO不存在的情况，但是有分页的情况</p>
 * <p>例如系统日志表，它的数据是程序内部自己生成的，并不是外界传入</p>
 *
 * @param <E> entity实体类泛型（数据库实体类）
 * @param <V> vo实体类泛型（controller返回参数）
 */
public interface BaseNoDTOMapstruct<E, V> extends PageEntityMapstruct<E, V>, PageVOMapstruct<V> {
}