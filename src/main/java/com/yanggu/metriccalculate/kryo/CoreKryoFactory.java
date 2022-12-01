/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.yanggu.client.magiccube.enums.TimeUnit;
import com.yanggu.metriccalculate.cube.TimeSeriesKVTable;
import com.yanggu.metriccalculate.cube.TimedKVMetricCube;
import com.yanggu.metriccalculate.kryo.serializer.*;
import com.yanggu.metriccalculate.number.*;
import com.yanggu.metriccalculate.unit.collection.ListUnit;
import com.yanggu.metriccalculate.unit.collection.SortedListUnit;
import com.yanggu.metriccalculate.unit.collection.UniqueListUnit;
import com.yanggu.metriccalculate.unit.numeric.*;
import com.yanggu.metriccalculate.unit.obj.*;
import com.yanggu.metriccalculate.unit.pattern.EventConnector;
import com.yanggu.metriccalculate.unit.pattern.Pattern;
import com.yanggu.metriccalculate.unit.pattern.PatternNode;
import com.yanggu.metriccalculate.value.AutoValueHashMap;
import com.yanggu.metriccalculate.value.ImmutableValue;
import com.yanggu.metriccalculate.value.NoneValue;

public class CoreKryoFactory extends BaseKryoFactory {

    public CoreKryoFactory() {
        super();
    }

    public CoreKryoFactory(KryoFactory parentFactory) {
        super(parentFactory);
    }

    @Override
    public Kryo create() {
        Kryo kryo = super.create();
        kryo.setReferences(false);

        kryo.register(CubeLong.class);
        kryo.register(CubeInteger.class);
        kryo.register(CubeDecimal.class);
        kryo.register(CubeDouble.class);
        kryo.register(CubeFloat.class);
        kryo.register(CubeZero.class);

        kryo.register(AvgUnit.class);
        kryo.register(CountUnit.class);
        kryo.register(SumUnit.class);
        kryo.register(MaxObjectUnit.class);
        kryo.register(MinObjectUnit.class);
        kryo.register(VarpUnit.class);
        kryo.register(VarsUnit.class);
        kryo.register(IncreaseCountUnit.class);
        kryo.register(DecreaseCountUnit.class);
        kryo.register(MaxIncreaseCountUnit.class);
        kryo.register(MaxDecreaseCountUnit.class);
        kryo.register(MaxContinuousCountUnit.class);

        kryo.register(MaxUnit.class);
        kryo.register(MinUnit.class);
        kryo.register(AutoValueHashMap.class);
        kryo.register(ReplacedUnit.class);
        kryo.register(OccupiedUnit.class);
        kryo.register(ListUnit.class);
        kryo.register(MapUnit.class);
        kryo.register(UniqueListUnit.class);
        kryo.register(SortedListUnit.class);

        kryo.register(TimeUnit.class);
        kryo.register(java.util.concurrent.TimeUnit.class);

        kryo.register(NoneValue.class);
        kryo.register(ImmutableValue.class);

        //kryo.register(DimensionSet.class, new DimensionSetSerializer());

        kryo.register(EventConnector.class, new EventConnectorSerializer());
        kryo.register(PatternNode.class, new PatternNodeSerializer());
        kryo.register(Pattern.class, new PatternSerializer());

        kryo.register(TimedKVMetricCube.class, new TimedKVMetricCubeSerializer());
        kryo.register(TimeSeriesKVTable.class, new TimeSeriesKVTableSerializer());

        //kryo.register(NumberFieldExtractProcessor.class);
        //kryo.register(ObjectFieldExtractProcessor.class);
        //kryo.register(DateTimeFormatterExtractor.class);

        //kryo.register(BasicFieldExtractProcessor.class);

        //kryo.register(JaninoExprCond.class);

        return kryo;
    }
}
