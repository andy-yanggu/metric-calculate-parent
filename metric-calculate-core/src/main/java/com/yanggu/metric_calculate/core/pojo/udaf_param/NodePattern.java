package com.yanggu.metric_calculate.core.pojo.udaf_param;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serializable;

@Data
public class NodePattern implements Comparable<NodePattern>, Serializable {

    private static final long serialVersionUID = 2449075043333799390L;

    private Integer index;

    private String name;

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
