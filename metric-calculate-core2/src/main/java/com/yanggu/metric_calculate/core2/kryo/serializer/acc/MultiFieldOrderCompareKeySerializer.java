package com.yanggu.metric_calculate.core2.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.MultiFieldOrderCompareKey;
import com.yanggu.metric_calculate.core2.kryo.serializer.util.KryoCollectionSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序序列化器
 */
public class MultiFieldOrderCompareKeySerializer extends Serializer<MultiFieldOrderCompareKey> implements Serializable {

    private static final long serialVersionUID = -5875701121165212922L;

    private KryoCollectionSerializer<List> listCollectionSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, MultiFieldOrderCompareKey multiFieldOrderCompareKey) {
        listCollectionSerializer.write(kryo, output, multiFieldOrderCompareKey.getFieldOrderList());
    }

    @Override
    public MultiFieldOrderCompareKey read(Kryo kryo, Input input, Class<? extends MultiFieldOrderCompareKey> type) {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setFieldOrderList(listCollectionSerializer.read(kryo, input, ArrayList.class));
        return multiFieldOrderCompareKey;
    }

}
