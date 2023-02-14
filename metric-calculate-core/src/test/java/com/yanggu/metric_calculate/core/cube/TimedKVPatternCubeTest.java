package com.yanggu.metric_calculate.core.cube;


import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.calculate.TimeWindow;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.fieldprocess.dimension.DimensionSet;
import com.yanggu.metric_calculate.core.fieldprocess.FieldProcessor;
import com.yanggu.metric_calculate.core.pojo.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.table.TimeSeriesKVTable;
import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.unit.pattern.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * TimedKVPatternCube单元测试类
 * <p>CEP事件模式进行单元测试</p>
 */
public class TimedKVPatternCubeTest {

    public static final String NAME = "TEST_CUBE";

    public static final String KEY = "TEST_KEY";

    public static final TimeBaselineDimension BASELINE_DIMENSION = new TimeBaselineDimension().setUnit(TimeUnit.SECOND).setLength(1);

    public static DimensionSet dimensionSet;

    private static TimedKVPatternCube<Trade, MatchState<Trade>> cube;

    private static PatternNode<MatchState<Trade>> start;

    private static PatternNode<MatchState<Trade>> mid;

    private static PatternNode<MatchState<Trade>> end;

    private static EventConnector startConnectMid;

    private static EventConnector midConnectEnd;

    private static Pattern pattern;

    private static List<Trade> objects;

    @Before
    public void init() {
        //构建CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)
        start = new PatternNode<>("start");
        start.setCond(state -> state.value().getAmt() > 100);
        start.setStart(true);

        mid = new PatternNode<>("mid");
        mid.setCond(state -> state.value().getAmt() > 200);

        end = new PatternNode<>("end");
        end.setCond(state -> state.value().getAmt() > 300);
        end.setEnd(true);
        end.setFieldProcessor(state -> new SumUnit<>(CubeLong.of(state.value().getAmt())));

        startConnectMid = new EventConnector(
            start.getName(),
            mid.getName(),
            new TimeBaselineCond(new TimeBaselineDimension().setLength(10).setUnit(TimeUnit.MILLS))
        );
        start.setNextNode(mid);

        midConnectEnd = new EventConnector(
            mid.getName(),
            end.getName(),
            new TimeBaselineCond(new TimeBaselineDimension().setLength(10).setUnit(TimeUnit.MILLS))
        );
        mid.setNextNode(end);

        start.setConnector(startConnectMid);
        mid.setConnector(midConnectEnd);

        List<EventConnector> connectors = Arrays.asList(startConnectMid, midConnectEnd);

        pattern = new Pattern<>(start, connectors, (FieldProcessor<EventState<Trade, ?>, Long>) input -> input.value().getTime());
        pattern.setValue(new CountUnit(CubeLong.of(1)));

        dimensionSet = new DimensionSet();
        Map<String, Object> dimensionMap = new LinkedHashMap<>();
        dimensionMap.put("id", 1L);
        dimensionMap.put("city", "city");
        dimensionSet.setDimensionMap(dimensionMap);
        dimensionSet.setKey("1_1");
        dimensionSet.setMetricName("metricName");

        cube = new TimedKVPatternCube<>();
        cube.setTimeBaselineDimension(BASELINE_DIMENSION);
        cube.setPattern(pattern);

        cube.init();

        objects = Arrays.asList(
            new Trade(1, 111, 1, "a"),
            new Trade(1, 111, 1, "a"),
            new Trade(1, 111, 1, "a"),
            new Trade(1, 111, 1, "a"),
            new Trade(1, 222, 5, "a"),
            new Trade(1, 222, 5, "a"),
            new Trade(1, 222, 5, "a"),
            new Trade(1, 222, 5, "a"),
            new Trade(1, 333, 10, "a"),
            new Trade(1, 333, 12, "a"),
            new Trade(1, 333, 13, "a"),
            new Trade(1, 333, 14, "a"));
    }

    @After
    public void after() {
        cube.init();
        cube.setReferenceTime(0);
    }

    /**
     * 测试put方法
     */
    @Test
    public void testPut() {
        //CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)
        MatchState<Trade> matchState = new MatchState<>(objects.get(0));
        cube.put(objects.get(0).getTime(), matchState);
        TimeSeriesKVTable<MatchState<Trade>> table = cube.getNodeTables().get(start.getName());
        assertSame(table.get(objects.get(0).getTime()), matchState);

        matchState = new MatchState<>(objects.get(5));
        cube.put(objects.get(5).getTime(), matchState);
        table = cube.getNodeTables().get(start.getName());
        assertSame(table.get(objects.get(5).getTime()), matchState);
        table = cube.getNodeTables().get(mid.getName());
        assertSame(table.get(objects.get(5).getTime()), matchState);

        matchState = new MatchState<>(objects.get(objects.size() - 1));
        cube.put(12000L, matchState);
        table = cube.getNodeTables().get(start.getName());
        assertSame(table.get(12000L), matchState);
        table = cube.getNodeTables().get(mid.getName());
        assertSame(table.get(12000L), matchState);
        table = cube.getNodeTables().get(end.getName());
        assertEquals(table.get(12000L), new MatchState<>(new SumUnit<>(CubeLong.of(333))));
    }

    /**
     * 测试查询方法
     */
    @Test
    public void testQuery() {
        objects.forEach(t -> cube.put(t.getTime(), new MatchState<>(t)));
        List<TimeWindow> timeWindowList = cube.getTimeBaselineDimension().getTimeWindow(CollUtil.getLast(objects).getTime());
        TimeWindow timeWindow = CollUtil.getFirst(timeWindowList);
        assertEquals(1332L, cube.query(timeWindow.getWindowStart(), true, timeWindow.getWindowEnd(), false).value());
    }

    /**
     * 测试删除过期数据方法
     */
    @Test
    public void eliminateExpiredData() {
        objects.forEach(t -> cube.put(t.getTime(), new MatchState<>(t)));
        cube.put(10000L, new MatchState<>(objects.get(0)));
        cube.eliminateExpiredData();
        //assertThat(cube.nodeTables.get(start.getName()).size()).isEqualTo(1);
        //assertThat(cube.nodeTables.get(start.getName()).firstKey()).isEqualTo(10000L);
        //assertThat(cube.nodeTables.get(start.getName()).firstRow()).isEqualTo(new MatchState<>(objects.get(0)));
        //assertThat((Map<Long, MatchState<Trade>>) cube.nodeTables.get(mid.getName())).isEmpty();
        //assertThat((Map<Long, MatchState<Trade>>) cube.nodeTables.get(end.getName())).isEmpty();
    }

    @Test
    public void referenceTime() {
        objects.forEach(t -> cube.put(t.getTime(), new MatchState<>(t)));
        //assertThat(cube.getReferenceTime()).isEqualTo(14L);
    }

    @Test
    public void testMerge() {
        objects.forEach(t -> cube.put(t.getTime(), new MatchState<>(t)));
        TimedKVPatternCube<Trade, MatchState<Trade>> cube1 = cube.fastClone();
        objects.forEach(t -> cube1.put(t.getTime() + 30, new MatchState<>(t)));
        cube.merge(cube1);
        System.out.println(cube);
        //assertThat(cube.query(0, cube.getReferenceTime()).value()).isEqualTo(2664L);
    }

    @Test
    public void testCloneEmpty() {
        assertTrue(cube.cloneEmpty().isEmpty());
    }

    @Test
    public void testFastClone() {
        assertEquals(cube, cube.fastClone());
    }

}