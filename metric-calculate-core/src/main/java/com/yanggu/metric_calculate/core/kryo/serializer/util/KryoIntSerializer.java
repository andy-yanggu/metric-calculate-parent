package com.yanggu.metric_calculate.core.kryo.serializer.util;


import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.io.Serial;
import java.io.Serializable;

public class KryoIntSerializer extends DefaultSerializers.IntSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = -6908287790121924586L;

}
