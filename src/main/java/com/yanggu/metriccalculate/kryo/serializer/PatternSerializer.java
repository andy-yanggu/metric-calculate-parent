package com.yanggu.metriccalculate.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metriccalculate.unit.pattern.Pattern;
import com.yanggu.metriccalculate.unit.pattern.PatternNode;

import java.util.ArrayList;

public class PatternSerializer extends Serializer<Pattern> {
    @Override
    public void write(Kryo kryo, Output output, Pattern pattern) {
        kryo.writeObject(output, pattern.isMerged());

        kryo.writeObject(output, pattern.getRootNode());
        kryo.writeObject(output, pattern.getStartNode());
        kryo.writeObject(output, pattern.getEndNode());
        kryo.writeObjectOrNull(output, pattern.getCurrentNode(), PatternNode.class);
        kryo.writeObject(output, pattern.getConnectors());

        kryo.writeObject(output, pattern.isMatchChoiceLast());
        kryo.writeObject(output, pattern.isMatchAll());

        kryo.writeObject(output, pattern.isMatchStart());
        kryo.writeObject(output, pattern.isMatchEnd());
        kryo.writeObject(output, pattern.isMatchFinish());

        kryo.writeClassAndObject(output, pattern.getValue());
    }

    @Override
    public Pattern read(Kryo kryo, Input input, Class<Pattern> type) {
        Pattern pattern = new Pattern();
        pattern.setMerged(kryo.readObject(input, Boolean.class));

        PatternNode rootNode = kryo.readObject(input, PatternNode.class);
        pattern.setRootNode(rootNode);
        pattern.setStartNode(kryo.readObject(input, PatternNode.class));
        pattern.setEndNode(kryo.readObject(input, PatternNode.class));
        pattern.setCurrentNode(kryo.readObjectOrNull(input, PatternNode.class));
        pattern.setConnectors(kryo.readObject(input, ArrayList.class));

        pattern.setMatchChoiceLast(kryo.readObject(input, Boolean.class));
        pattern.setMatchAll(kryo.readObject(input, Boolean.class));

        pattern.setMatchStart(kryo.readObject(input, Boolean.class));
        pattern.setMatchEnd(kryo.readObject(input, Boolean.class));
        pattern.setMatchFinish(kryo.readObject(input, Boolean.class));

        pattern.setValue(kryo.readClassAndObject(input));
        return pattern;
    }
}
