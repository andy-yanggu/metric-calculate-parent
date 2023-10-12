package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldDistinctKey;

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
