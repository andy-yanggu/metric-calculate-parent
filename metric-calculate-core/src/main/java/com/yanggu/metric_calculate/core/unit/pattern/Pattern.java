package com.yanggu.metric_calculate.core.unit.pattern;

import com.yanggu.metric_calculate.core.fieldprocess.FieldExtractProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.value.NoneValue;
import com.yanggu.metric_calculate.core.value.Value;
import com.yanggu.metric_calculate.core.value.Cloneable2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Pattern<T extends EventState> implements MergedUnit<Pattern<T>>, Value, Serializable {

    private boolean merged;

    private List<EventConnector> connectors;

    private PatternNode<T> rootNode;
    private PatternNode<T> startNode;
    private PatternNode<T> endNode;
    private PatternNode<T> currentNode;

    private boolean matchChoiceLast;
    private boolean matchAll;

    private boolean matchStart;
    private boolean matchEnd;
    private boolean matchFinish;

    private transient FieldExtractProcessor<T, Long> timeProcessor;

    private Object value;

    /**
     * Construct.
     */
    public Pattern() {
        matchChoiceLast = true;
        matchAll = false;
        matchStart = false;
        matchEnd = false;
        matchFinish = false;
        merged = false;
    }

    /**
     * Construct.
     */
    public Pattern(
            PatternNode rootNode,
            List<EventConnector> connectors,
            FieldExtractProcessor<T, Long> timeProcessor) {
        this();
        this.rootNode = rootNode;
        this.connectors = connectors;
        startNode = rootNode;
        while (rootNode.getNextNode() != null) {
            rootNode = rootNode.getNextNode();
        }
        endNode = rootNode;
        this.timeProcessor = timeProcessor;
    }

    @Override
    public Pattern<T> merge(Pattern<T> that) {
        if (that == null) {
            return this;
        }
        if (matchFinish && that.matchFinish) {
            value = value instanceof MergedUnit
                    ? ((MergedUnit) value).merge((MergedUnit) that.value) : value;
            setMerged(true);
            return this;
        }

        if (getReferenceTime() > that.getReferenceTime()) {
            return this;
        }

        if (mergeNode(that.currentNode) || currentNode.isEnd()) {
            return this;
        }

        EventConnector connector = currentNode.getConnector();
        if (that.currentNode.isTriggered() && connector.connect(
                that.currentNode.getReferenceTime() - currentNode.getReferenceTime())) {
            currentNode.setNextNode(that.subPattern(that.currentNode).startNode);
            currentNode = currentNode.getNextNode();
            if (currentNode.isEnd()) {
                matchEnd = true;
                endNode = currentNode;
            }
            matchEnd = matchEnd ? matchEnd : currentNode.isEnd();
            matchFinish = isMatchStart() && isMatchEnd();
            setMerged(true);
        }

        return this;
    }

    private boolean mergeNode(PatternNode<T> node) {
        if (!currentNode.canMerge(node)) {
            return false;
        }
        currentNode = currentNode.merge(node);
        setMerged(true);
        return true;
    }

    /**
     * Set current node.
     * @param currentNode currentNode
     */
    public void setCurrentNode(PatternNode<T> currentNode) {
        this.currentNode = currentNode;
        if (currentNode != null) {
            matchStart = isMatchStart() || currentNode.equals(startNode);
        }
    }

    /**
     * Return node stream, from root to end.
     */
    public Stream<PatternNode<T>> stream() {
        PatternNode<T> tNode = rootNode;
        Stream.Builder<PatternNode<T>> builder = Stream.builder();
        do {
            builder.add(tNode);
        } while ((tNode = tNode.getNextNode()) != null);
        return builder.build();
    }

    /**
     * object triggered this pattern.
     * @param object object
     * @return match pattern list
     */
    public List<Pattern<T>> match(T object) throws Exception {
        List<Pattern<T>> result = new ArrayList();
        PatternNode node = this.rootNode;
        do {
            if (node.getCond().cond(object)) {
                Pattern pattern = subPattern(node);
                PatternNode currentNode = pattern.getCurrentNode();
                currentNode.trigger(object);
                currentNode.setReferenceTime(timeProcessor.process(object));
                currentNode.setTriggered(true);
                pattern.currentNode = currentNode;
                pattern.matchStart = pattern.matchStart ? pattern.matchStart : currentNode.isStart();
                pattern.matchEnd = pattern.matchEnd ? pattern.matchEnd : currentNode.isEnd();
                result.add(pattern);
            }
        } while ((node = node.getNextNode()) != null);
        return result;
    }

    /**
     * sub pattern, start with startNode.
     */
    public Pattern subPattern(PatternNode startNode) {
        Pattern result;
        if (startNode.isStart()) {
            result = fastClone();
            result.currentNode = result.rootNode;
            return result;
        }
        PatternNode rootNode = startNode.fastClone();
        PatternNode node = rootNode;
        do {
            if (node.getNextNode() == null ) {
                break;
            }
            node.setNextNode(node.getNextNode().fastClone());
        } while ( (node = node.getNextNode()) != null);
        result = new Pattern();
        result.setRootNode(rootNode);
        result.setConnectors(connectors);
        result.setCurrentNode(rootNode);
        result.setTimeProcessor(timeProcessor);
        result.setMerged(isMerged());
        return result;
    }

    @Override
    public Pattern fastClone() {
        Pattern<T> pattern = new Pattern<>();
        PatternNode rootNode = this.rootNode.fastClone();
        PatternNode node = rootNode;
        do {
            if (node.getNextNode() == null) {
                break;
            }
            PatternNode nextNode = node.getNextNode().fastClone();
            if (node.getNextNode() == currentNode ) {
                pattern.setCurrentNode(nextNode);
            }
            node.setNextNode(nextNode);
        } while ((node = node.getNextNode()) != null);
        Object value = this.value instanceof Cloneable2 ? ((Cloneable2) this.value).fastClone() : this.value;

        pattern.setRootNode(rootNode);
        if (pattern.currentNode == null) {
            pattern.setCurrentNode(pattern.startNode);
        }
        pattern.connectors = connectors;
        pattern.timeProcessor = timeProcessor;
        pattern.value = value;
        pattern.matchChoiceLast = matchChoiceLast;
        pattern.matchAll = matchAll;
        pattern.matchEnd = matchEnd;
        pattern.matchStart = matchStart;
        pattern.matchFinish = matchFinish;
        pattern.setMerged(isMerged());
        return pattern;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    @Override
    public Object value() {
        if (value instanceof Value && isMatchFinish()) {
            return value;
        }
        if (value == null && isMatchEnd() ) {
            return endNode.value();
        }
        return NoneValue.INSTANCE.value();
    }

    public long getReferenceTime() {
        return currentNode.getReferenceTime();
    }

    public long getStartTime() {
        return startNode.getReferenceTime();
    }

    /**
     * set root node.
     */
    public void setRootNode(PatternNode rootNode) {
        this.rootNode = rootNode;
        startNode = rootNode;
        while (rootNode.getNextNode() != null) {
            rootNode = rootNode.getNextNode();
        }
        endNode = rootNode;
    }

    public void setStartNode(PatternNode startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(PatternNode<T> endNode) {
        this.endNode = endNode;
    }

    public void setConnectors(List<EventConnector> connectors) {
        this.connectors = connectors;
    }

    public void setMatchChoiceLast(boolean matchChoiceLast) {
        this.matchChoiceLast = matchChoiceLast;
    }

    public void setMatchAll(boolean matchAll) {
        this.matchAll = matchAll;
    }

    public void setMatchStart(boolean matchStart) {
        this.matchStart = matchStart;
    }

    public void setMatchEnd(boolean matchEnd) {
        this.matchEnd = matchEnd;
    }

    public void setMatchFinish(boolean matchFinish) {
        this.matchFinish = matchFinish;
    }

    public void setTimeProcessor(FieldExtractProcessor<T, Long> timeProcessor) {
        this.timeProcessor = timeProcessor;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<EventConnector> getConnectors() {
        return connectors;
    }

    public PatternNode getRootNode() {
        return rootNode;
    }

    public PatternNode getStartNode() {
        return startNode;
    }

    /**
     * get start connector.
     */
    public EventConnector getStartConnector() {
        for (EventConnector connector : getConnectors()) {
            if (connector.getPreNode().equals(getStartNode().getName())) {
                return connector;
            }
        }
        return null;
    }

    public PatternNode<T> getEndNode() {
        return endNode;
    }

    /**
     * get end connector.
     */
    public EventConnector getEndConnector() {
        for (EventConnector connector : getConnectors()) {
            if (connector.getNextNode().equals(getEndNode().getName())) {
                return connector;
            }
        }
        return null;
    }

    public PatternNode<T> getCurrentNode() {
        return currentNode;
    }

    /**
     * get node by name.
     */
    public PatternNode getNode(String name) {
        PatternNode node = getRootNode();
        while (node != null && node.getName() != name) {
            node = node.getNextNode();
        }
        return node;
    }

    public boolean isMatchChoiceLast() {
        return matchChoiceLast;
    }

    public boolean isMatchAll() {
        return matchAll;
    }

    public boolean isMatchStart() {
        return matchStart;
    }

    public boolean isMatchEnd() {
        return matchEnd;
    }

    public boolean isMatchFinish() {
        return matchFinish;
    }

    public FieldExtractProcessor<T, Long> getTimeProcessor() {
        return timeProcessor;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pattern)) {
            return false;
        }
        Pattern<?> pattern = (Pattern<?>) o;
        return merged == pattern.merged
               && matchChoiceLast == pattern.matchChoiceLast
               && matchAll == pattern.matchAll
               && matchStart == pattern.matchStart
               && matchEnd == pattern.matchEnd
               && matchFinish == pattern.matchFinish
               && Objects.equals(connectors, pattern.connectors)
               && Objects.equals(rootNode, pattern.rootNode)
               && Objects.equals(startNode, pattern.startNode)
               && Objects.equals(endNode, pattern.endNode)
               && Objects.equals(currentNode, pattern.currentNode)
               && Objects.equals(timeProcessor, pattern.timeProcessor)
               && Objects.equals(value, pattern.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{
            merged, connectors, rootNode, startNode, endNode, currentNode, matchChoiceLast, matchAll, matchStart,
            matchEnd, matchFinish, timeProcessor, value});
    }

    @Override
    public String toString() {
        return "Pattern{"
               + "value=" + value
               + ", currentNode=" + currentNode
               + ", rootNode=" + rootNode
               + ", startNode=" + startNode
               + ", endNode=" + endNode
               + ", connectors=" + connectors
               + ", merged=" + merged
               + ", matchChoiceLast=" + matchChoiceLast
               + ", matchAll=" + matchAll
               + ", matchStart=" + matchStart
               + ", matchEnd=" + matchEnd
               + ", matchFinish=" + matchFinish
               + ", timeProcessor=" + timeProcessor
               + "}";
    }
}
