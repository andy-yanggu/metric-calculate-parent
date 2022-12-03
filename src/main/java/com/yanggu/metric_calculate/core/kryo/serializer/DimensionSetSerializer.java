package com.yanggu.metric_calculate.core.kryo.serializer;

//public class DimensionSetSerializer extends CollectionSerializer {
//
//    @Override
//    public void write(Kryo kryo, Output output, Collection collection) {
//        super.write(kryo, output, collection);
//        kryo.writeObjectOrNull(output, ((DimensionSet)collection).getName(), String.class);
//    }
//
//    @Override
//    public Collection read(Kryo kryo, Input input, Class<Collection> type) {
//        DimensionSet dimensionSet = (DimensionSet) super.read(kryo, input, type);
//        dimensionSet.setName(kryo.readObject(input, String.class));
//        return dimensionSet;
//    }
//
//}
