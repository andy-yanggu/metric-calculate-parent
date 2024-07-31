package com.yanggu.metric_calculate.config.base.mapstruct;


import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;

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
    default PageVO<V> entityToPageVO(PageQuery<E> entityPageQuery) {
        PageVO<V> pageVO = new PageVO<>();
        pageVO.setPageNum(entityPageQuery.getPageNum());
        pageVO.setPageSize(entityPageQuery.getPageSize());
        pageVO.setTotal(entityPageQuery.getTotalPage());
        pageVO.setRecords(entityToVO(entityPageQuery.getRecords()));
        return pageVO;
    }

}