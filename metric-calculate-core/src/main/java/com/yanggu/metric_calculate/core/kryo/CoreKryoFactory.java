package com.yanggu.metric_calculate.core.kryo;

import cn.hutool.core.collection.CollUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.yanggu.metric_calculate.core.cube.TimedKVMetricCube;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.kryo.serializer.TimeSeriesKVTableSerializer;
import com.yanggu.metric_calculate.core.kryo.serializer.TimedKVMetricCubeSerializer;
import com.yanggu.metric_calculate.core.number.*;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.MergedUnit;
import com.yanggu.metric_calculate.core.unit.collection.DistinctListObjectUnit;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;
import com.yanggu.metric_calculate.core.unit.collection.SortedListObjectUnit;
import com.yanggu.metric_calculate.core.unit.map.BaseMapUnit;
import com.yanggu.metric_calculate.core.unit.numeric.*;
import com.yanggu.metric_calculate.core.unit.object.MaxObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.MinObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.OccupiedObjectUnit;
import com.yanggu.metric_calculate.core.unit.object.ReplacedObjectUnit;
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
        kryo.register(MaxUnit.class);
        kryo.register(MinUnit.class);
        kryo.register(VarpUnit.class);
        kryo.register(VarsUnit.class);
        kryo.register(IncreaseCountUnit.class);
        kryo.register(DecreaseCountUnit.class);
        kryo.register(MaxIncreaseCountUnit.class);
        kryo.register(MaxDecreaseCountUnit.class);
        kryo.register(MaxContinuousCountUnit.class);

        //对象型
        kryo.register(MaxObjectUnit.class);
        kryo.register(MinObjectUnit.class);
        kryo.register(ReplacedObjectUnit.class);
        kryo.register(OccupiedObjectUnit.class);

        //集合型
        kryo.register(ListObjectUnit.class);
        kryo.register(DistinctListObjectUnit.class);
        kryo.register(SortedListObjectUnit.class);

        //映射型
        kryo.register(BaseMapUnit.class);

        kryo.register(TimeUnit.class);

        kryo.register(NoneValue.class);

        kryo.register(TimedKVMetricCube.class, new TimedKVMetricCubeSerializer());
        kryo.register(TimeSeriesKVTable.class, new TimeSeriesKVTableSerializer());

        //这里主要是注册自定义的MergeUnit
        if (CollUtil.isNotEmpty(classList)) {
            classList.forEach(kryo::register);
        }
        return kryo;
    }

}
