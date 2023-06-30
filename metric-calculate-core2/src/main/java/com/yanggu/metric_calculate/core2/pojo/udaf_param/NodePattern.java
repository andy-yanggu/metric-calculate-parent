package com.yanggu.metric_calculate.core2.pojo.udaf_param;

import com.yanggu.metric_calculate.core2.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

@Data
public class NodePattern implements Comparable<NodePattern> {

    private Integer index;

    private String name;

    private String matchExpress;

    /**
     * Aviator表达式参数
     */
    private AviatorExpressParam matchExpressParam;

    private Long interval;

    @Override
    public int compareTo(NodePattern that) {
        return this.index.compareTo(that.index);
    }

}
