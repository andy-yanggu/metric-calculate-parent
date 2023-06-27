package com.yanggu.metric_calculate.core2.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.pojo.udaf_param.NodePattern;

import java.io.Serializable;


public class NodePatternSerializer extends Serializer<NodePattern> implements Serializable {

    private static final long serialVersionUID = -911693259577461728L;

    @Override
    public void write(Kryo kryo, Output output, NodePattern nodePattern) {
        kryo.writeClassAndObject(output, nodePattern);
    }

    @Override
    public NodePattern read(Kryo kryo, Input input, Class<? extends NodePattern> aClass) {
        return (NodePattern) kryo.readClassAndObject(input);
    }

}
