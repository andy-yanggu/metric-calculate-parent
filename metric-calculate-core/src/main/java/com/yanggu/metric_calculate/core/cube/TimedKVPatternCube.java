package com.yanggu.metric_calculate.core.cube;

import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
import com.yanggu.metric_calculate.core.unit.pattern.EventState;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.unit.pattern.Pattern;
import com.yanggu.metric_calculate.core.unit.pattern.PatternNode;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class TimedKVPatternCube<T, E extends EventState<T, E>> extends TimedKVMetricCube<E, TimedKVPatternCube<T, E>> {

    /**
     * Magic Cube . Timed . Key Value . Pattern . Cube.
     */
    public static final String PREFIX = "MC.T.KV.P.C";

    private Pattern<E> pattern;

    private Map<String, PatternNode> nodes;

    /**
     * 放入对应满足过滤条件的数据
     * <p>key是节点名称, value的map, 内层map的key是数据的时间戳, value是原始数据</p>
     */
    private Map<String, TimeSeriesKVTable<E>> nodeTables;

    private SortedMap<Integer, String> sortedNodes;


    public TimedKVPatternCube() {
    }

    public TimedKVPatternCube(String key, String name, TimeBaselineDimension timeBaselineDimension, DimensionSet dimensionSet, Pattern<E> pattern) {
        setKey(key);
        setName(name);
        setTimeBaselineDimension(timeBaselineDimension);
        setDimensionSet(dimensionSet);
        this.pattern = pattern;
    }

    @Override
    public TimedKVPatternCube<T, E> init() {
        super.init();
        if (pattern.isMatchAll()) {
            throw new UnsupportedOperationException("Current version not support match all.");
        }
        nodeTables = new HashMap<>();
        nodes = new HashMap<>();
        sortedNodes = new TreeMap<>();
        pattern = pattern.fastClone();
        AtomicInteger nodeOrder = new AtomicInteger(0);
        pattern.stream().forEach(node -> {
            TimeSeriesKVTable<E> nodeTable = new TimeSeriesKVTable<>();
            nodeTable.setTimeBaselineDimension(getTimeBaselineDimension());

            nodes.put(node.getName(), node);
            nodeTables.put(node.getName(), nodeTable);
            sortedNodes.put(nodeOrder.getAndIncrement(), node.getName());
        });
        return this;
    }

    /**
     * 将满足过滤条件的数据分别放在对应节点中
     * <p>key是数据时间戳, value是原始数据</p>
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Long key, E value) {
        nodeTables.entrySet().stream()
                .filter(e -> nodes.get(e.getKey()).getCond().cond(value))
                .forEach(e -> {
                    try {
                        e.getValue().put(key, processorEnd(nodes.get(e.getKey()), value));
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                });
        setReferenceTime(key);
    }

    @Override
    public Value query(Long start, boolean fromInclusive, Long end, boolean toInclusive) {
        if (end < start) {
            return NoneValue.INSTANCE;
        }
        TimeSeriesKVTable<E> endTable = nodeTables.get(pattern.getEndNode().getName()).subTable(start, fromInclusive, end, toInclusive);
        if (endTable.isEmpty()) {
            return NoneValue.INSTANCE;
        }

        MergedUnit result;
        if (sortedNodes.size() == 1) {
            result = reduceTable(endTable.subTable(start, fromInclusive, end, toInclusive));
            return result instanceof Value ? (Value) result : NoneValue.INSTANCE;
        }

        TimeSeriesKVTable<E> nodeTable = nodeTables.get(pattern.getRootNode().getName()).subTable(start, fromInclusive, end, toInclusive);
        TimeSeriesKVTable<E> nextTable = null;

        PatternNode node = pattern.getRootNode();
        PatternNode nextNode;

        while ((nextNode = node.getNextNode()) != null) {
            Long size = node.getConnector().getCond().timeBaseline().realLength();
            TimeSeriesKVTable<E> nextNodeTable = nodeTables.get(nextNode.getName());
            nextTable = nextNodeTable.cloneEmpty();
            for (Map.Entry<Long, E> entry : nodeTable.entrySet()) {
                Long timestamp = entry.getKey();
                nextTable.putAll(nextNodeTable.subTable(timestamp, true,Math.min(timestamp + size, end), false));
                //判断和是否超过当前节点的最大时间戳, 如果超过没有必要继续遍历了
                if (timestamp + size > nextNodeTable.getReferenceTime()) {
                    break;
                }
            }
            nodeTable = nextTable;
            node = nextNode;
        }

        result = reduceTable(nextTable);
        return result instanceof Value ? (Value) result : NoneValue.INSTANCE;
    }

    private E processorEnd(PatternNode node, E event) throws Exception {
        if (node.isEnd()) {
            if (node.getFieldProcessor() == null) {
                return (E) new MatchState<>(new CountUnit(CubeLong.of(1)));
            } else {
                return (E) new MatchState<>(node.getFieldProcessor().process(event));
            }
        }
        return event;
    }

    protected MergedUnit reduceTable(TimeSeriesKVTable<E> table) {
        if (CollUtil.isEmpty(table)) {
            return null;
        }
        return table.values().stream()
            .filter(Objects::nonNull)
            .map(s -> (MergedUnit) s.value())
            .filter(Objects::nonNull)
            .reduce((a, b) -> a.getClass().equals(b.getClass()) ? a.merge(b) : a)
            .orElse(null);
    }

    @Override
    public TimedKVPatternCube<T, E> merge(TimedKVPatternCube<T, E> that) {
        if (that == null) {
            return this;
        }
        this.getTable().merge(that.getTable());
        that.nodeTables.forEach((k, v) -> nodeTables.get(k).merge(v));
        setReferenceTime(that.getReferenceTime());
        return this;
    }

    @Override
    public TimedKVPatternCube<T, E> cloneEmpty() {
        return new TimedKVPatternCube<T, E>(getKey(), getName(), getTimeBaselineDimension(), getDimensionSet(), pattern).init();
    }

    @Override
    public TimedKVPatternCube<T, E> fastClone() {
        TimedKVPatternCube<T, E> result = cloneEmpty();
        result.setTable(getTable().fastClone());
        result.nodeTables.keySet().forEach(key -> result.nodeTables.put(key, nodeTables.get(key).fastClone()));
        return result;
    }

    @Override
    public boolean isEmpty() {
        return nodeTables.values().stream().allMatch(AbstractMap::isEmpty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TimedKVPatternCube<T, E> that = (TimedKVPatternCube<T, E>) o;
        return super.equals(that) && pattern.equals(that.pattern) && nodes.equals(that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pattern, nodes);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TimedKVPatternCube.class.getSimpleName() + "[", "]")
            .add("pattern=" + pattern)
            .add("nodeMap=" + nodes)
            .add("name='" + getName() + "'")
            .add("referenceTime=" + getReferenceTime())
            .add("dimensions=" + getDimensionSet())
            .toString();
    }

}