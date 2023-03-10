package com.yanggu.metric_calculate.core.kryo.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.table.PatternTable;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.value.Clone;

import java.util.TreeMap;


public class PatternTableSerializer<T extends Clone<T>> extends Serializer<PatternTable<T>> {

    private final DefaultSerializers.TreeMapSerializer treeMapSerializer;

    public PatternTableSerializer(Kryo kryo) {
        treeMapSerializer = new DefaultSerializers.TreeMapSerializer();
        treeMapSerializer.setKeyClass(NodePattern.class, new BeanSerializer<>(kryo, NodePattern.class));
        treeMapSerializer.setValueClass(TimeSeriesKVTable.class, new TimeSeriesKVTableSerializer());
    }

    @Override
    public void write(Kryo kryo, Output output, PatternTable<T> object) {
        treeMapSerializer.write(kryo, output, object.getDataMap());
    }

    @Override
    public PatternTable<T> read(Kryo kryo, Input input, Class<? extends PatternTable<T>> type) {
        TreeMap<NodePattern, TimeSeriesKVTable<MatchState<T>>> dataMap = treeMapSerializer.read(kryo, input, TreeMap.class);
        PatternTable<T> patternTable = new PatternTable<>();
        patternTable.setDataMap(dataMap);
        return patternTable;
    }

}
