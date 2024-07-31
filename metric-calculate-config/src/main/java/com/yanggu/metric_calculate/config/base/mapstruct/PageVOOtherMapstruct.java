package com.yanggu.metric_calculate.config.base.mapstruct;


import com.yanggu.metric_calculate.config.base.domain.query.PageQuery;
import com.yanggu.metric_calculate.config.base.domain.vo.PageVO;

import java.util.List;

/**
 * 其余情况下的分页数据转换
 */
public interface PageVOOtherMapstruct {

    /**
     * 将其余VO分页数据转换成PageVO分页数据
     *
     * @param <T> 其余VO对象
     */
    default <T> PageVO<T> toPageVO(PageQuery<T> voPageQuery) {
        return new PageVO<>(voPageQuery);
    }

    /**
     * 转成成分页数据通过全量参数
     *
     * @param records 已经分页后的list
     */
    default <T> PageVO<T> toPageVOWithFullParams(Long pageNum, Long pageSize, Long total, List<T> records) {
        return new PageVO<>(pageNum, pageSize, total, records);
    }

    /**
     * 转换成分页数据，手动分页
     *
     * @param records 分页前的数据
     */
    default <T> PageVO<T> toPageVOWithPagination(Long pageNum, Long pageSize, List<T> records) {
        return new PageVO<>(pageNum, pageSize, records);
    }

}