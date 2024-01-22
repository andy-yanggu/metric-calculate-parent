package com.yanggu.metric_calculate.config.base.mapstruct;


import com.yanggu.metric_calculate.config.base.query.PageQuery;
import com.yanggu.metric_calculate.config.base.vo.PageVO;

/**
 * 将VO分页数据转换为VO分页数据的通用接口
 *
 * @param <V> vo实体类泛型（controller返回参数）
 */
public interface PageVOMapstruct<V> {

    /**
     * 将VO分页数据转换成PageVO分页数据
     */
    PageVO<V> voToPageVO(PageQuery<V> voPageQuery);

}