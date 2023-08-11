package com.yanggu.metric_calculate.core.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.field_process.multi_field_order.FieldOrder;

import java.io.Serializable;

/**
 * 字段排序序列化器
 */
public class FieldOrderSerializer extends Serializer<FieldOrder> implements Serializable {

    private static final long serialVersionUID = 5359246401789028377L;

    @Override
    public void write(Kryo kryo, Output output, FieldOrder fieldOrder) {
        kryo.writeClassAndObject(output, fieldOrder.getResult());
        output.writeBoolean(fieldOrder.getAsc());
    }

    @Override
    public FieldOrder read(Kryo kryo, Input input, Class<? extends FieldOrder> type) {
        Object result = kryo.readClassAndObject(input);
        boolean asc = input.readBoolean();
        return new FieldOrder().setResult(result).setAsc(asc);
    }

}
