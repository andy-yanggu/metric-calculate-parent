package com.yanggu.metric_calculate.core.kryo.serializer.util;

import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.io.Serial;
import java.io.Serializable;


public class KryoLongSerializer extends DefaultSerializers.LongSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = -8432538155523377433L;

}
