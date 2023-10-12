package com.yanggu.metric_calculate.core.kryo.serializer.acc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.MultiFieldOrderCompareKey;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 多字段排序序列化器
 */
public class MultiFieldOrderCompareKeySerializer extends Serializer<MultiFieldOrderCompareKey> implements Serializable {

    @Serial
    private static final long serialVersionUID = -5875701121165212922L;

    private KryoCollectionSerializer<List> listCollectionSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, MultiFieldOrderCompareKey multiFieldOrderCompareKey) {
        listCollectionSerializer.write(kryo, output, multiFieldOrderCompareKey.getDataList());
        listCollectionSerializer.write(kryo, output, multiFieldOrderCompareKey.getBooleanList());
    }

    @Override
    public MultiFieldOrderCompareKey read(Kryo kryo, Input input, Class<? extends MultiFieldOrderCompareKey> type) {
        MultiFieldOrderCompareKey multiFieldOrderCompareKey = new MultiFieldOrderCompareKey();
        multiFieldOrderCompareKey.setDataList(listCollectionSerializer.read(kryo, input, ArrayList.class));
        multiFieldOrderCompareKey.setBooleanList(listCollectionSerializer.read(kryo, input, ArrayList.class));
        return multiFieldOrderCompareKey;
    }

}
