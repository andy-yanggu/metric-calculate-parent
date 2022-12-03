package com.yanggu.metric_calculate.core.unit.pattern;

import java.io.Serializable;

public class EventConnector implements Serializable {

    private String preNode;
    private String nextNode;
    private TimeBaselineCond cond;

    public EventConnector() {
    }

    /**
     * Construct.
     * @param preNode preNode
     * @param nextNode nextNode
     * @param cond cond
     */
    public EventConnector(String preNode, String nextNode, TimeBaselineCond cond) {
        this.preNode = preNode;
        this.nextNode = nextNode;
        this.cond = cond;
    }

    /**
     * connect.
     */
    public boolean connect(Long time) {
        return cond.cond(time);
    }

    public String getPreNode() {
        return preNode;
    }

    public String getNextNode() {
        return nextNode;
    }

    public TimeBaselineCond getCond() {
        return cond;
    }

    public void setPreNode(String preNode) {
        this.preNode = preNode;
    }

    public void setNextNode(String nextNode) {
        this.nextNode = nextNode;
    }

    public void setCond(TimeBaselineCond cond) {
        this.cond = cond;
    }
    
}
