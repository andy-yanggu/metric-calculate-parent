package com.yanggu.metric_calculate.core.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.pattern.EventConnector;
import com.yanggu.metric_calculate.core.unit.pattern.PatternNode;
import com.yanggu.metric_calculate.core.value.Value;

import java.util.Collection;

public class PatternNodeSerializer extends Serializer<PatternNode> {
    @Override
    public void write(Kryo kryo, Output output, PatternNode node) {
        kryo.writeObject(output, node.getName());
        kryo.writeClassAndObject(output, node.getValue());

        kryo.writeObject(output, node.isSaveDetails());
        kryo.writeObjectOrNull(output, node.getDetails(), Collection.class);
        kryo.writeObject(output, node.getCount());

        kryo.writeObject(output, node.isStart());
        kryo.writeObject(output, node.isEnd());
        kryo.writeObject(output, node.isTriggered());

        kryo.writeObject(output, node.isMerged());
        kryo.writeClassAndObject(output, node.getMergeLimit());
        kryo.writeClassAndObject(output, node.getCurrentState());
        kryo.writeClassAndObject(output, node.getStateProcessor());

        kryo.writeObjectOrNull(output, node.getNextNode(), PatternNode.class);

        kryo.writeObjectOrNull(output, node.getConnector(), EventConnector.class);

        kryo.writeObject(output, node.getReferenceTime());
    }

    @Override
    public PatternNode read(Kryo kryo, Input input, Class<PatternNode> type) {
        PatternNode node = new PatternNode();
        node.setName(kryo.readObject(input, String.class));
        node.setValue((Value) kryo.readClassAndObject(input));

        node.setSaveDetails(kryo.readObject(input, Boolean.class));
        node.setDetails(kryo.readObjectOrNull(input, Collection.class));
        node.setCount(kryo.readObject(input, Long.class));

        node.setStart(kryo.readObject(input, Boolean.class));
        node.setEnd(kryo.readObject(input, Boolean.class));
        node.setTriggered(kryo.readObject(input, Boolean.class));

        node.setMerged(kryo.readObject(input, Boolean.class));
        node.setMergeLimit((MergedUnit) kryo.readClassAndObject(input));
        node.setCurrentState((MergedUnit) kryo.readClassAndObject(input));
        node.setStateProcessor((FieldProcessor) kryo.readClassAndObject(input));

        PatternNode nextNode = kryo.readObjectOrNull(input, PatternNode.class);
        node.setNextNode(nextNode);
        EventConnector connector = kryo.readObjectOrNull(input, EventConnector.class);
        if (connector != null) {
            connector.setPreNode(node.getName());
        }
        node.setConnector(connector);

        node.setReferenceTime(kryo.readObject(input, Long.class));
        return node;
    }

}
