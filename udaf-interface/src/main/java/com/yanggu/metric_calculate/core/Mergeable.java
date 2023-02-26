package com.yanggu.metric_calculate.core;

/**
 * 最顶级的抽象接口, 表示进行合并
 *
 * @param <M> Mergeable的具体实现类
 */
public interface Mergeable<M extends Mergeable<M>> {

    M merge(M that);

}