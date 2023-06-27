package com.yanggu.metric_calculate.core2.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.yanggu.metric_calculate.core2.field_process.multi_field_distinct.MultiFieldDistinctKey;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoCollectionSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 多字段去重序列化器
 */
public class MultiFieldDistinctKeySerializer extends Serializer<MultiFieldDistinctKey> implements Serializable {

    private static final long serialVersionUID = -6465589424180908501L;

    private KryoCollectionSerializer<List> listCollectionSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, MultiFieldDistinctKey multiFieldDistinctKey) {
        listCollectionSerializer.write(kryo, output, multiFieldDistinctKey.getFieldList());
    }

    @Override
    public MultiFieldDistinctKey read(Kryo kryo, Input input, Class<? extends MultiFieldDistinctKey> type) {
        MultiFieldDistinctKey multiFieldDistinctKey = new MultiFieldDistinctKey();
        multiFieldDistinctKey.setFieldList(listCollectionSerializer.read(kryo, input, ArrayList.class));
        return multiFieldDistinctKey;
    }

}
