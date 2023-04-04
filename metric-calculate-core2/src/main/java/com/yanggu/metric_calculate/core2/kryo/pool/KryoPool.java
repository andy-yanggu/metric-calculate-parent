package com.yanggu.metric_calculate.core2.kryo.pool;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.esotericsoftware.kryo.util.Pool;
import com.yanggu.metric_calculate.core2.kryo.serializer.TimeTableSerializer;
import com.yanggu.metric_calculate.core2.table.TumblingTimeTimeTable;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class KryoPool extends Pool<Kryo> {

    //private List<Class<? extends MergedUnit>> classList;

    public KryoPool(boolean threadSafe, boolean softReferences, int maximumCapacity) {
        super(threadSafe, softReferences, maximumCapacity);
    }

    @Override
    protected Kryo create() {
        Kryo kryo = new Kryo();

        //检测循环依赖，默认值为true,避免版本变化显式设置
        kryo.setReferences(true);
        //默认值为true，避免版本变化显式设置
        kryo.setRegistrationRequired(false);
        //设定默认的实例化器
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));

        kryo.register(TumblingTimeTimeTable.class, new TimeTableSerializer(), 1);
        //自定义的包装类
        //kryo.register(CubeLong.class, 1);
        //kryo.register(CubeInteger.class, 2);
        //kryo.register(CubeDecimal.class, 3);
        //kryo.register(CubeDouble.class, 4);
        //kryo.register(CubeFloat.class, 5);
        //kryo.register(CubeZero.class, 6);
        //
        ////数值型
        //kryo.register(AvgUnit.class, 7);
        //kryo.register(CountUnit.class, 8);
        //kryo.register(SumUnit.class, 9);
        //kryo.register(MaxUnit.class, 10);
        //kryo.register(MinUnit.class, 11);
        //kryo.register(VarpUnit.class, 12);
        //kryo.register(VarsUnit.class, 13);
        //kryo.register(IncreaseCountUnit.class, 14);
        //kryo.register(DecreaseCountUnit.class, 15);
        //kryo.register(MaxIncreaseCountUnit.class, 16);
        //kryo.register(MaxDecreaseCountUnit.class, 17);
        //kryo.register(MaxContinuousCountUnit.class, 18);
        //
        ////对象型
        //kryo.register(MaxObjectUnit.class, 19);
        //kryo.register(MinObjectUnit.class, 20);
        //kryo.register(ReplacedObjectUnit.class, 21);
        //kryo.register(OccupiedObjectUnit.class, 22);
        //
        ////集合型
        //kryo.register(ListObjectUnit.class, 23);
        //kryo.register(DistinctListObjectUnit.class, 24);
        //kryo.register(SortedListObjectUnit.class, 25);
        //
        ////映射型
        //kryo.register(BaseMapUnit.class, 26);
        //
        //kryo.register(TimeUnit.class, 27);
        //
        //kryo.register(NoneValue.class, 28);
        //
        //kryo.register(TimedKVMetricCube.class, new TimedKVMetricCubeSerializer(), 29);
        //kryo.register(TimeSeriesKVTable.class, new TimeSeriesKVTableSerializer(), 30);
        //kryo.register(Tuple.class, new TupleSerializer(), 31);
        //kryo.register(BoundedPriorityQueue.class, new BoundedPriorityQueueSerializer(), 32);
        //kryo.register(NodePattern.class, new BeanSerializer<>(kryo, NodePattern.class), 33);
        //kryo.register(PatternTable.class, new PatternTableSerializer(kryo), 34);

        //目前自定义的MergeUnit先不注册, 防止注册的时候id冲突
        //应该有udaf的管理界面, 内置的MergeUnit和自定义的MergeUnit进行管理
        //自定义的udaf注册的id可以使用数据库的主键id
        //这里主要是注册自定义的MergeUnit
        //if (CollUtil.isNotEmpty(classList)) {
        //    for (int i = 1; i <= classList.size(); i++) {
        //        kryo.register(classList.get(i), 32 + i);
        //    }
        //}
        return kryo;
    }

}
