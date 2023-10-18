package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldData;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 多字段去重序列化器
 */
public class MultiFieldDataSerializer extends Serializer<MultiFieldData> implements Serializable {

    @Serial
    private static final long serialVersionUID = -6465589424180908501L;

    private final KryoCollectionSerializer<List> listCollectionSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, MultiFieldData multiFieldData) {
        listCollectionSerializer.write(kryo, output, multiFieldData.getFieldList());
    }

    @Override
    public MultiFieldData read(Kryo kryo, Input input, Class<? extends MultiFieldData> type) {
        MultiFieldData multiFieldData = new MultiFieldData();
        multiFieldData.setFieldList(listCollectionSerializer.read(kryo, input, ArrayList.class));
        return multiFieldData;
    }

}
