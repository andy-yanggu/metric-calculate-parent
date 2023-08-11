package com.yanggu.metric_calculate.config.mapstruct;

import java.util.List;

/**
 * 转换类
 *
 * @param <D> DTO类
 * @param <E> Entity类
 */
public interface BaseMapstruct<D, E> {

    /**
     * 将源对象转换为DTO对象
     *
     * @param e .
     * @return D
     */
    D toDTO(E e);

    /**
     * 将源对象集合转换为DTO对象集合
     *
     * @param es .
     * @return List<D>
     */
    List<D> toDTO(List<E> es);


    /**
     * 将目标对象转换为源对象
     *
     * @param d .
     * @return E
     */
    E toEntity(D d);

    /**
     * 将目标对象集合转换为源对象集合
     *
     * @param ds .
     * @return List<E>
     */
    List<E> toEntity(List<D> ds);

}