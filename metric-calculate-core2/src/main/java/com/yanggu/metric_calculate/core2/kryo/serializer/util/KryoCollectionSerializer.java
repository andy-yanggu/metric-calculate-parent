package com.yanggu.metric_calculate.core2.kryo.serializer.util;


import com.esotericsoftware.kryo.serializers.CollectionSerializer;

import java.io.Serializable;
import java.util.Collection;

public class KryoCollectionSerializer<T extends Collection> extends CollectionSerializer<T> implements Serializable {

    private static final long serialVersionUID = 3827817020120217101L;

}
