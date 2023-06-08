package com.yanggu.metric_calculate.core2.kryo.serializer.acc;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core2.field_process.multi_field_order.FieldOrder;

public class FieldOrderSerializer extends Serializer<FieldOrder> {

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
