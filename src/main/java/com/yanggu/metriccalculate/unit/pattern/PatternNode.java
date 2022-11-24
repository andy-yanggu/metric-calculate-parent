/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit.pattern;

import com.yanggu.metriccalculate.fieldprocess.Cond;
import com.yanggu.metriccalculate.fieldprocess.FieldExtractProcessor;
import com.yanggu.metriccalculate.unit.MergedUnit;
import com.yanggu.metriccalculate.value.NoneValue;
import com.yanggu.metriccalculate.value.Value;

import java.io.Serializable;
import java.util.*;

public class PatternNode<T extends EventState> implements MergedUnit<PatternNode<T>>, Value, Serializable {

    private String name;
    private Value value;
    private transient Cond<T> cond;

    private boolean saveDetails;
    private Collection<T> details;
    private long count;

    private boolean isStart;
    private boolean isEnd;
    private boolean triggered;

    private boolean merged;
    private MergedUnit mergeLimit;
    private MergedUnit currentState;
    private transient FieldExtractProcessor<T, MergedUnit> stateProcessor;

    private PatternNode<T> nextNode;
    private EventConnector connector;

    private transient FieldExtractProcessor<T, MergedUnit> fieldProcessor;

    private long referenceTime;

    /**
     * Construct.
     */
    public PatternNode() {
        isStart = false;
        isEnd = false;
        triggered = false;
        merged = false;
        saveDetails = false;
        count = 0;
    }

    public PatternNode(String name) {
        this();
        this.name = name;
    }

    /**
     * check can merge.
     * @param node merge node
     * @return can merge?
     */
    public boolean canMerge(PatternNode<T> node) {
        if (mergeLimit == null) {
            return false;
        }
        if (currentState == null && node.currentState == null) {
            return false;
        }
        if (!(getName().equals(node.getName()))) {
            return false;
        }
        MergedUnit unit = currentState.fastClone().merge(node.currentState);
        return ((Comparable)mergeLimit).compareTo(unit) >= 0;
    }

    @Override
    public PatternNode<T> merge(PatternNode<T> that) {
        if (that == null) {
            return this;
        }
        value = value == null ? that.value : value instanceof MergedUnit
                                             ? (Value) ((MergedUnit) value).merge((MergedUnit) that.value) : that.value;
        mergeLimit = mergeLimit == null ? null : mergeLimit.merge(that.mergeLimit);
        currentState = currentState == null ? that.currentState : currentState.merge(that.currentState);

        if (saveDetails) {
            details.addAll(that.details == null ? Collections.emptySet() : that.details);
        } else {
            details = that.details;
        }
        count += that.count;
        referenceTime = referenceTime > that.referenceTime ? referenceTime : that.referenceTime;
        return this;
    }

    @Override
    public PatternNode fastClone() {
        PatternNode result = new PatternNode(name);
        result.setCond(cond);
        result.setSaveDetails(saveDetails);
        result.setStart(isStart);
        result.setEnd(isEnd);
        result.setMergeLimit(mergeLimit);
        result.setFieldProcessor(fieldProcessor);
        result.setConnector(connector);
        result.setNextNode(nextNode);
        result.setCurrentState(currentState);
        result.setReferenceTime(referenceTime);
        result.setStateProcessor(stateProcessor);
        result.setTriggered(triggered);
        result.setValue(value);
        result.setMerged(isMerged());
        result.details = details;
        return result;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    @Override
    public Object value() {
        return value == null ? NoneValue.INSTANCE : value;
    }

    /**
     * trigger.
     * @param object object
     */
    public void trigger(T object) throws Exception {
        if (saveDetails) {
            details = new ArrayList(Arrays.asList(object));
        }
        Object value = fieldProcessor == null ? NoneValue.INSTANCE : fieldProcessor.process(object);
        if (value instanceof Value) {
            this.value = (Value) value;
        }
        count++;
        if (mergeLimit != null ) {
            MergedUnit state = stateProcessor == null ? (MergedUnit) value : stateProcessor.process(object);
            currentState = currentState == null ? state : currentState.merge(state);
        }
        triggered = true;
    }

    /**
     * Set connector.
     * @param connector connector
     */
    public void setConnector(EventConnector connector) {
        this.connector = connector;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public void setCond(Cond<T> cond) {
        this.cond = cond;
    }

    public void setSaveDetails(boolean saveDetails) {
        this.saveDetails = saveDetails;
    }

    public void setDetails(Collection<T> details) {
        this.details = details;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public void setMergeLimit(MergedUnit mergeLimit) {
        this.mergeLimit = mergeLimit;
    }

    public void setCurrentState(MergedUnit currentState) {
        this.currentState = currentState;
    }

    public void setStateProcessor(FieldExtractProcessor<T, MergedUnit> stateProcessor) {
        this.stateProcessor = stateProcessor;
    }

    public void setNextNode(PatternNode<T> nextNode) {
        this.nextNode = nextNode;
    }

    public void setFieldProcessor(FieldExtractProcessor<T, MergedUnit> fieldProcessor) {
        this.fieldProcessor = fieldProcessor;
    }

    public void setReferenceTime(long referenceTime) {
        this.referenceTime = referenceTime;
    }

    public String getName() {
        return name;
    }

    public Value getValue() {
        return value;
    }

    public Cond getCond() {
        return cond;
    }

    public boolean isSaveDetails() {
        return saveDetails;
    }

    public Collection<T> getDetails() {
        return details;
    }

    public long getCount() {
        return count;
    }

    public boolean isStart() {
        return isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public MergedUnit getMergeLimit() {
        return mergeLimit;
    }

    public MergedUnit getCurrentState() {
        return currentState;
    }

    public FieldExtractProcessor<T, MergedUnit> getStateProcessor() {
        return stateProcessor;
    }

    public PatternNode<T> getNextNode() {
        return nextNode;
    }

    public EventConnector getConnector() {
        return connector;
    }

    public FieldExtractProcessor<T, MergedUnit> getFieldProcessor() {
        return fieldProcessor;
    }

    public long getReferenceTime() {
        return referenceTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatternNode)) {
            return false;
        }
        PatternNode<?> node = (PatternNode<?>) o;
        return saveDetails == node.saveDetails && count == node.count && isStart == node.isStart && isEnd == node.isEnd
               && triggered == node.triggered && merged == node.merged && referenceTime == node.referenceTime
               && name.equals(node.name) && Objects.equals(value, node.value) && Objects.equals(cond, node.cond)
               && Objects.equals(details, node.details) && Objects.equals(mergeLimit, node.mergeLimit)
               && Objects.equals(currentState, node.currentState) && Objects.equals(stateProcessor, node.stateProcessor)
               && Objects.equals(nextNode, node.nextNode) && Objects.equals(connector, node.connector)
               && Objects.equals(fieldProcessor, node.fieldProcessor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                name, value, cond, saveDetails, details, count, isStart, isEnd, triggered, merged, mergeLimit,
                currentState, stateProcessor, nextNode, connector, fieldProcessor, referenceTime);
    }

    @Override
    public String toString() {
        return "PatternNode{"
               + "name='" + name + '\''
               + ", value=" + value
               + ", saveDetails=" + saveDetails
               + ", details=" + details
               + ", referenceTime=" + referenceTime
               + ", count=" + count
               + ", isStart=" + isStart
               + ", isEnd=" + isEnd
               + ", triggered=" + triggered
               + ", merged=" + merged
               + ", mergeLimit=" + mergeLimit
               + ", currentState=" + currentState
               + ", nextNode=" + nextNode
               + ", cond=" + cond
               + ", connector=" + connector
               + ", fieldProcessor=" + fieldProcessor
               + ", stateProcessor=" + stateProcessor
               + '}';
    }
}
