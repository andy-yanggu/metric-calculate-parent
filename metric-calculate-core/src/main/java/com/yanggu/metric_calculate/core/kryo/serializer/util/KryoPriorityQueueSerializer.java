package com.yanggu.metric_calculate.core.kryo.serializer.util;


import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.io.Serial;
import java.io.Serializable;

public class KryoPriorityQueueSerializer extends DefaultSerializers.PriorityQueueSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = 7609674975734240041L;

}
