package com.yanggu.metric_calculate.core.kryo.serializer.util;


import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.io.Serial;
import java.io.Serializable;

public class KryoEnumSerializer extends DefaultSerializers.EnumSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = 7168628872195758510L;

    public KryoEnumSerializer(Class<? extends Enum> type) {
        super(type);
    }

}
