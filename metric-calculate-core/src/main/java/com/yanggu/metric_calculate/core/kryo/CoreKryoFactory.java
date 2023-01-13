package com.yanggu.metric_calculate.core.kryo;

import cn.hutool.core.collection.CollUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.yanggu.metric_calculate.core.cube.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.kryo.serializer.*;
import com.yanggu.metric_calculate.core.number.*;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListUnit;
import com.yanggu.metric_calculate.core.unit.collection.SortedListUnit;
import com.yanggu.metric_calculate.core.unit.collection.UniqueListUnit;
import com.yanggu.metric_calculate.core.unit.numeric.*;
import com.yanggu.metric_calculate.core.unit.obj.*;
import com.yanggu.metric_calculate.core.unit.pattern.EventConnector;
import com.yanggu.metric_calculate.core.unit.pattern.Pattern;
import com.yanggu.metric_calculate.core.unit.pattern.PatternNode;
import com.yanggu.metric_calculate.core.value.NoneValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreKryoFactory extends BaseKryoFactory {

    private List<Class<? extends MergedUnit>> classList;

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

        //数值型
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

        //对象型和集合型
        kryo.register(MaxUnit.class);
        kryo.register(MinUnit.class);
        kryo.register(ReplacedObjectUnit.class);
        kryo.register(OccupiedObjectUnit.class);
        kryo.register(ListUnit.class);
        kryo.register(MapUnit.class);
        kryo.register(UniqueListUnit.class);
        kryo.register(SortedListUnit.class);

        kryo.register(TimeUnit.class);

        kryo.register(NoneValue.class);

        kryo.register(EventConnector.class, new EventConnectorSerializer());
        kryo.register(PatternNode.class, new PatternNodeSerializer());
        kryo.register(Pattern.class, new PatternSerializer());

        kryo.register(TimedKVMetricCube.class, new TimedKVMetricCubeSerializer());
        kryo.register(TimeSeriesKVTable.class, new TimeSeriesKVTableSerializer());

        //这里主要是注册自定义的MergeUnit
        if (CollUtil.isNotEmpty(classList)) {
            classList.forEach(kryo::register);
        }
        return kryo;
    }

}
