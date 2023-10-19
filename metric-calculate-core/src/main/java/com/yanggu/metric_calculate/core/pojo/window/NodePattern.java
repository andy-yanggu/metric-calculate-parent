package com.yanggu.metric_calculate.core.pojo.window;

import com.yanggu.metric_calculate.core.pojo.aviator_express.AviatorExpressParam;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class NodePattern implements Comparable<NodePattern>, Serializable {

    @Serial
    private static final long serialVersionUID = 2449075043333799390L;

    /**
     * 名称
     */
    private String name;

    /**
     * Aviator表达式参数
     */
    private AviatorExpressParam matchExpressParam;

    /**
     * 时间间隔:单位毫秒
     */
    private Long interval;

    /**
     * 顺序
     */
    private Integer sort;

    @Override
    public int compareTo(NodePattern that) {
        return this.sort.compareTo(that.sort);
    }

}
