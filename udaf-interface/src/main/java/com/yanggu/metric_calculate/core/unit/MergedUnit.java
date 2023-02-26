package com.yanggu.metric_calculate.core.unit;

import com.yanggu.metric_calculate.core.Mergeable;
import com.yanggu.metric_calculate.core.value.Clone;

/**
 * 聚合中间数据接口
 * @param <U> MergedUnit具体的实现类
 */
public interface MergedUnit<U extends MergedUnit<U> & Clone<U>> extends Clone<U>, Mergeable<U> {

    /**
     * 进行合并
     *
     * @param that
     * @return
     */
    @Override
    U merge(U that);

    /**
     * 克隆出一个和原对象一样的数据
     *
     * @return
     */
    @Override
    U fastClone();

}
