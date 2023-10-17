package com.yanggu.metric_calculate.core.kryo.serializer.util;

import com.esotericsoftware.kryo.serializers.MapSerializer;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;


public class KryoMapSerializer<T extends Map> extends MapSerializer<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -8184202015221274324L;

}
