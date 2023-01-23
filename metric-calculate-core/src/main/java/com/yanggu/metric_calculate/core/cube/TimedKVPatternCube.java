//package com.yanggu.metric_calculate.core.cube;
//
//import com.yanggu.metric_calculate.core.fieldprocess.FieldExtractProcessor;
//import com.yanggu.metric_calculate.core.number.CubeLong;
//import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
//import com.yanggu.metric_calculate.core.unit.MergedUnit;
//import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
//import com.yanggu.metric_calculate.core.unit.pattern.EventState;
//import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
//import com.yanggu.metric_calculate.core.unit.pattern.Pattern;
//import com.yanggu.metric_calculate.core.unit.pattern.PatternNode;
//import com.yanggu.metric_calculate.core.value.NoneValue;
//import com.yanggu.metric_calculate.core.value.Value;
//import lombok.Data;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Data
//public class TimedKVPatternCube<T, E extends EventState<T, E>> extends TimedKVMetricCube<E, TimedKVPatternCube<T, E>> {
//
//    /**
//     * Magic Cube . Timed . Key Value . Pattern . Cube.
//     */
//    public static final String PREFIX = "MC.T.KV.P.C";
//
//    protected Pattern<E> pattern;
//
//    protected Map<String, PatternNode> nodes;
//    protected SortedMap<Integer, String> sortedNodes;
//    protected Map<String, TimeSeriesKVTable<E>> nodeTables;
//
//    protected Map<String, TimeSeriesKVTable<E>> tables;
//
//    public TimedKVPatternCube() {
//    }
//
//    @Override
//    public TimedKVPatternCube<T, E> init() {
//        super.init();
//        if (pattern.isMatchAll()) {
//            throw new UnsupportedOperationException("Current version not support match all.");
//        }
//        nodeTables = new HashMap<>();
//        nodes = new HashMap<>();
//        sortedNodes = new TreeMap<>();
//        pattern = pattern.fastClone();
//        AtomicInteger nodeOrder = new AtomicInteger(0);
//        pattern.stream().forEach(node -> {
//            TimeSeriesKVTable nodeTable = new TimeSeriesKVTable<>();
//            nodes.put(node.getName(), node);
//            tables.put(node.getName(), nodeTable);
//            nodeTables.put(node.getName(), nodeTable);
//            sortedNodes.put(nodeOrder.getAndIncrement(), node.getName());
//        });
//        return this;
//    }
//
//    public Map<String, PatternNode> nodes() {
//        return nodes;
//    }
//
//    public Map<String, TimeSeriesKVTable<E>> nodeTables() {
//        return nodeTables;
//    }
//
//    public Pattern pattern() {
//        return pattern;
//    }
//
//    @Override
//    public void put(Long key, E value) {
//        Object obj = value.value();
//        nodeTables.entrySet().stream()
//            .filter(e -> nodes.get(e.getKey()).getCond().cond(value))
//            .forEach(e -> e.getValue().put(key, processorEnd(nodes.get(e.getKey()), value)));
//        setReferenceTime(key);
//    }
//
//    private E processorEnd(PatternNode node, E event) {
//        if (node.isEnd()) {
//            if (node.getFieldProcessor() == null) {
//                return (E) new MatchState(new CountUnit(CubeLong.of(1)));
//            } else {
//                return (E) new MatchState(node.getFieldProcessor().process(event));
//            }
//        }
//        return event;
//    }
//
//    @Override
//    public Value query(long start, boolean fromInclusive, long end, boolean toInclusive) {
//        if (end < start) {
//            return NoneValue.INSTANCE;
//        }
//        TimeSeriesKVTable<E> endTable = nodeTables.get(pattern.getEndNode().getName()).query(start, fromInclusive, end, toInclusive);
//        if (endTable.isEmpty()) {
//            return NoneValue.INSTANCE;
//        }
//
//        MergedUnit result;
//        if (sortedNodes.size() == 1) {
//            result = reduceTable(endTable.subTable(start, end));
//            return result instanceof Value ? (Value) result : NoneValue.INSTANCE;
//        }
//
//        TimeSeriesKVTable<E> nodeTable = nodeTables.get(pattern.getRootNode().getName()).subTable(start, fromInclusive, end, toInclusive);
//        TimeSeriesKVTable<E> nextTable = null;
//
//        PatternNode node = pattern.getRootNode();
//        PatternNode nextNode;
//
//        while ((nextNode = node.getNextNode()) != null) {
//            Long size = node.getConnector().getCond().timeBaseline().realLength();
//            nextTable = nodeTables.get(nextNode.getName()).cloneEmpty();
//            for (Map.Entry<Long, E> entry : nodeTable.entrySet()) {
//                Long from = entry.getKey();
//                nextTable.putAll(nodeTables.get(nextNode.getName()).subTable(from, Math.min(from + size, end)));
//                if (from + size > nodeTables.get(nextNode.getName()).referenceTime()) {
//                    break;
//                }
//            }
//            nodeTable = nextTable;
//            node = nextNode;
//        }
//
//        result = reduceTable(nextTable);
//        return result instanceof Value ? (Value) result : NoneValue.INSTANCE;
//    }
//
//    protected MergedUnit reduceTable(TimeSeriesKVTable<E> table, FieldExtractProcessor<E, MergedUnit> fieldProcessor) {
//        return table.values().stream()
//            .map(fieldProcessor::process)
//            .filter(Objects::nonNull)
//            .reduce(MergedUnit::merge)
//            .orElse(null);
//    }
//
//    protected MergedUnit reduceTable(TimeSeriesKVTable<E> table) {
//        if (table == null || table.isEmpty()) {
//            return null;
//        }
//        return table.values().stream()
//            .filter(Objects::nonNull)
//            .map(s -> (MergedUnit) s.value())
//            .filter(Objects::nonNull)
//            .reduce((a, b) -> a.getClass().equals(b.getClass()) ? a.merge(b) : a)
//            .orElse(null);
//    }
//
//    @Override
//    public TimedKVPatternCube<T, E> merge(TimedKVPatternCube<T, E> that) {
//        if (that == null) {
//            return this;
//        }
//        this.table.putAll(that.table);
//        that.nodeTables.forEach((k, v) -> nodeTables.get(k).merge(v));
//        referenceTime(that.referenceTime());
//        return this;
//    }
//
//    @Override
//    public TimedKVPatternCube<T, E> cloneEmpty() {
//        return new TimedKVPatternCube<T, E>(name, key, expire, baselineDimension, dimensions, pattern).init();
//    }
//
//    @Override
//    public TimedKVPatternCube<T, E> fastClone() {
//        TimedKVPatternCube<T, E> result = cloneEmpty();
//        result.table = table.fastClone();
//        result.nodeTables.keySet().forEach(key -> result.nodeTables.put(key, nodeTables.get(key).fastClone()));
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) {
//            return true;
//        }
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        if (!super.equals(o)) {
//            return false;
//        }
//        TimedKVPatternCube<T, E> that = (TimedKVPatternCube<T, E>) o;
//        return super.equals(that) && pattern.equals(that.pattern) && nodes.equals(that.nodes);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), pattern, nodes);
//    }
//
//    @Override
//    public String toString() {
//        return new StringJoiner(", ", TimedKVPatternCube.class.getSimpleName() + "[", "]")
//            .add("pattern=" + pattern)
//            .add("nodeMap=" + nodes)
//            .add("name='" + name + "'")
//            .add("merged=" + merged)
//            .add("expire=" + expire)
//            .add("referenceTime=" + referenceTime)
//            .add("baselineDimension=" + baselineDimension)
//            .add("dimensions=" + dimensions)
//            .add("labels=" + labels)
//            .add("table=" + table)
//            .add("tables=" + tables)
//            .toString();
//    }
//}