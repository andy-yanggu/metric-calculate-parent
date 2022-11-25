/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metriccalculate.unit.pattern.EventConnector;
import com.yanggu.metriccalculate.unit.pattern.TimeBaselineCond;

public class EventConnectorSerializer extends Serializer<EventConnector> {

    @Override
    public void write(Kryo kryo, Output output, EventConnector connector) {
        output.writeString(connector.getPreNode());
        output.writeString(connector.getNextNode());
        kryo.writeClassAndObject(output, connector.getCond());
    }

    @Override
    public EventConnector read(Kryo kryo, Input input, Class<EventConnector> type) {
        EventConnector connector = new EventConnector();
        connector.setPreNode(input.readString());
        connector.setNextNode(input.readString());
        connector.setCond((TimeBaselineCond) kryo.readClassAndObject(input));
        return connector;
    }
}
