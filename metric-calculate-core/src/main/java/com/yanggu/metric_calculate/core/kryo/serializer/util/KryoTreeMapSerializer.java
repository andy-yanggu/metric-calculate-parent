package com.yanggu.metric_calculate.core.kryo.serializer.util;


import com.esotericsoftware.kryo.serializers.DefaultSerializers;

import java.io.Serial;
import java.io.Serializable;

public class KryoTreeMapSerializer extends DefaultSerializers.TreeMapSerializer implements Serializable {

    @Serial
    private static final long serialVersionUID = -1284449195290237106L;

}
