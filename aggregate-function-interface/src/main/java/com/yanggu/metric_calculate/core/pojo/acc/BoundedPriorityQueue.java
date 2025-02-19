package com.yanggu.metric_calculate.core.pojo.acc;

import com.yanggu.metric_calculate.core.enums.SortType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import static com.yanggu.metric_calculate.core.enums.SortType.*;

/**
 * 有界优先队列<br>
 * 按照给定的排序规则，排序元素，当队列满时，按照给定的排序规则淘汰末尾元素（去除末尾元素）
 *
 * @param <E> 成员类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BoundedPriorityQueue<E> extends PriorityQueue<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3794348988671694820L;

    /**
     * 容量
     */
    private Integer capacity;

    /**
     * 比较器
     */
    private Comparator<? super E> comparator;

    /**
     * 排序方式
     */
    private SortType sortType;

    public BoundedPriorityQueue(final int capacity) {
        this(capacity, null, ROW_NUMBER);
    }

    public BoundedPriorityQueue(final int capacity, final Comparator<? super E> comparator) {
        this(capacity, comparator, ROW_NUMBER);
    }

    /**
     * 构造
     *
     * @param capacity   容量
     * @param comparator 比较器
     * @param sortType   排序方式
     */
    public BoundedPriorityQueue(final int capacity, final Comparator<? super E> comparator, final SortType sortType) {
        super(capacity, comparator == null ? (Comparator<? super E>) Comparator.naturalOrder().reversed() : comparator.reversed());
        this.capacity = capacity;
        this.comparator = comparator;
        this.sortType = sortType;
    }

    /**
     * 返回排序后的列表
     *
     * @return 排序后的列表
     */
    public List<E> toList() {
        final ArrayList<E> list = new ArrayList<>(this);
        list.sort(comparator);

        List<E> result = new ArrayList<>();
        if (sortType == ROW_NUMBER) {
            int rowNumber = 1;
            for (E element : list) {
                if (rowNumber <= capacity) {
                    result.add(element);
                }
                rowNumber++;
            }
        } else if (sortType == RANK) {
            int rank = 1;
            int prevRank = 1;
            E prevElement = null;
            for (E element : list) {
                if (prevElement != null && comparator.compare(prevElement, element) != 0) {
                    rank = prevRank + (prevRank - rank + 1);
                }
                if (rank <= capacity) {
                    result.add(element);
                }
                prevElement = element;
                prevRank++;
            }
        } else if (sortType == DENSE_RANK) {
            int denseRank = 1;
            E prevDenseElement = null;
            for (E element : list) {
                if (prevDenseElement != null && comparator.compare(prevDenseElement, element) != 0) {
                    denseRank++;
                }
                if (denseRank <= capacity) {
                    result.add(element);
                }
                prevDenseElement = element;
            }
        } else {
            result = list;
        }
        return result;
    }

    @Override
    public Iterator<E> iterator() {
        return toList().iterator();
    }

    @Override
    public String toString() {
        return "BoundedPriorityQueue{" +
                "capacity=" + capacity +
                ", comparator=" + comparator +
                ", sortType=" + sortType +
                '}';
    }

}