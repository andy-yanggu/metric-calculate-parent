package com.yanggu.metric_calculate.config.base.mapstruct;


import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.base.vo.PageVO;

/**
 * 将Entity分页数据转换为VO分页数据的通用接口
 *
 * @param <E> entity实体类泛型（数据库实体类）
 * @param <V> vo实体类泛型（controller返回参数）
 */
public interface PageEntityMapstruct<E, V> extends EntityToVOMapstruct<E, V> {

    /**
     * 将Entity分页数据转换为VO分页数据
     */
    PageVO<V> entityToPageVO(PageQuery<E> entityPageQuery);

}