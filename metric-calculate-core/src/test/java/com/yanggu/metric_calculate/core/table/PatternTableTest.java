package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.field_process.aggregate.EventStateExtractor;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.pojo.metric.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.pojo.metric.TimeWindow;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.test.Trade;
import com.yanggu.metric_calculate.core.pojo.udaf_param.ChainPattern;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getTestUnitFactory;
import static org.junit.Assert.*;

public class PatternTableTest {

    private List<NodePattern> nodePatternList;

    private PatternTable<Trade> patternTable;

    private List<Trade> objects;

    @Before
    public void before() {
        String jsonString = FileUtil.readUtf8String("node_pattern_list.json");
        //CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)
        this.nodePatternList = JSONUtil.toList(jsonString, NodePattern.class);

        PatternTable<Trade> tempPatternTable = new PatternTable<>();
        TreeMap<NodePattern, TimeSeriesKVTable<MatchState<Trade>>> dataMap = new TreeMap<>();
        for (NodePattern nodePattern : this.nodePatternList) {
            dataMap.put(nodePattern, new TimeSeriesKVTable<>());
        }
        tempPatternTable.setDataMap(dataMap);
        this.patternTable = tempPatternTable;

        this.objects = Arrays.asList(
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


    @Test
    public void putValue() {
        TreeMap<NodePattern, CloneWrapper<Trade>> treeMap = new TreeMap<>();
        treeMap.put(nodePatternList.get(0), CloneWrapper.wrap(objects.get(0)));
        MatchState<TreeMap<NodePattern, CloneWrapper<Trade>>> value = new MatchState<>(treeMap);
        patternTable.putValue(objects.get(0).getTime(), null, value);

        MatchState<Trade> tradeMatchState = patternTable.getDataMap().get(nodePatternList.get(0)).get(objects.get(0).getTime());
        assertNotNull(tradeMatchState);
        assertEquals(objects.get(0), tradeMatchState.value());
    }

    @Test
    public void query() {
        //CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)

        ChainPattern chainPattern = new ChainPattern();
        chainPattern.setNodePatternList(nodePatternList);
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("COUNT");
        baseUdafParam.setMetricExpress("1");

        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amt", Long.class);

        EventStateExtractor<Trade, MatchState<TreeMap<NodePattern, CloneWrapper<Trade>>>> eventStateExtractor =
                FieldProcessorUtil.getEventStateExtractor(chainPattern, baseUdafParam, fieldMap, getTestUnitFactory());

        objects.forEach(t -> patternTable.putValue(t.getTime(), null, eventStateExtractor.process(t)));

        TimeBaselineDimension timeBaselineDimension = new TimeBaselineDimension().setUnit(TimeUnit.SECOND).setLength(1);
        List<TimeWindow> timeWindowList = timeBaselineDimension.getTimeWindow(CollUtil.getLast(objects).getTime());
        TimeWindow timeWindow = CollUtil.getFirst(timeWindowList);
        assertEquals(1332L, patternTable.query(timeWindow.getWindowStart(), true, timeWindow.getWindowEnd(), false).value().values().stream().mapToLong(Trade::getAmt).sum());
    }

}