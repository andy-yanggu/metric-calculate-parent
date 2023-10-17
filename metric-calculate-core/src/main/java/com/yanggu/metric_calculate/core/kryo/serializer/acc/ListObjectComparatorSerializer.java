package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.serializer.util.KryoCollectionSerializer;
import com.yanggu.metric_calculate.core.pojo.acc.ListObjectComparator;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListObjectComparatorSerializer<IN> extends Serializer<ListObjectComparator<IN>> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3803180481212592154L;

    private final KryoCollectionSerializer<ArrayList> listSerializer = new KryoCollectionSerializer<>();

    @Override
    public void write(Kryo kryo, Output output, ListObjectComparator<IN> object) {
        listSerializer.write(kryo, output, new ArrayList<>(object.getBooleanList()));
    }

    @Override
    public ListObjectComparator<IN> read(Kryo kryo, Input input, Class<? extends ListObjectComparator<IN>> type) {
        List<Boolean> booleanList = listSerializer.read(kryo, input, ArrayList.class);
        ListObjectComparator<IN> listObjectComparator = new ListObjectComparator<>();
        listObjectComparator.setBooleanList(booleanList);
        return listObjectComparator;
    }

}
