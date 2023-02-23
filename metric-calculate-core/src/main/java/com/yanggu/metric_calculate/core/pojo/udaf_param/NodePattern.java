package com.yanggu.metric_calculate.core.pojo.udaf_param;

import lombok.Data;

@Data
public class NodePattern implements Comparable<NodePattern> {

    private Integer index;

    private String name;

    private String matchExpress;

    private Long interval;

    @Override
    public int compareTo(NodePattern that) {
        return this.index.compareTo(that.index);
    }

}
