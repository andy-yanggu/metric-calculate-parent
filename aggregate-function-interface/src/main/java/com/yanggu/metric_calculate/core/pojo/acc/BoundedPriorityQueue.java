package com.yanggu.metric_calculate.core.pojo.acc;

import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * 有界优先队列<br>
 * 按照给定的排序规则，排序元素，当队列满时，按照给定的排序规则淘汰末尾元素（去除末尾元素）
 *
 * @param <E> 成员类型
 */
@Data
public class BoundedPriorityQueue<E> extends PriorityQueue<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3794348988671694820L;

    /**
     * 容量
     */
    private Integer capacity;

    public BoundedPriorityQueue(final int capacity) {
        this(capacity, null);
    }

    /**
     * 构造
     *
     * @param capacity   容量
     * @param comparator 比较器
     */
    public BoundedPriorityQueue(final int capacity, final Comparator<? super E> comparator) {
        super(capacity, comparator == null ? (Comparator<? super E>) Comparator.naturalOrder().reversed() : comparator.reversed());
        this.capacity = capacity;
    }

    /**
     * 加入元素，当队列满时，淘汰末尾元素
     *
     * @param e 元素
     * @return 加入成功与否
     */
    @Override
    public boolean offer(final E e) {
        if (size() >= capacity) {
            final E head = peek();
            if (this.comparator().compare(e, head) <= 0) {
                return true;
            }
            //当队列满时，就要淘汰顶端队列
            poll();
        }
        return super.offer(e);
    }

    /**
     * @return 返回排序后的列表
     */
    public List<E> toList() {
        final ArrayList<E> list = new ArrayList<>(this);
        list.sort(comparator());
        return list;
    }

    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }

}