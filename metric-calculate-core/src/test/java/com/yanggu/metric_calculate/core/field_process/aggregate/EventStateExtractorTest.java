package com.yanggu.metric_calculate.core.field_process.aggregate;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core.pojo.udaf_param.BaseUdafParam;
import com.yanggu.metric_calculate.core.pojo.udaf_param.ChainPattern;
import com.yanggu.metric_calculate.core.pojo.udaf_param.NodePattern;
import com.yanggu.metric_calculate.core.test.Trade;
import com.yanggu.metric_calculate.core.unit.pattern.MatchState;
import com.yanggu.metric_calculate.core.util.FieldProcessorUtil;
import com.yanggu.metric_calculate.core.value.CloneWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.yanggu.metric_calculate.core.unit.UnitFactoryTest.getTestUnitFactory;
import static org.junit.Assert.*;

/**
 * CEP事件模式聚合字段处理器单元测试类
 */
public class EventStateExtractorTest {

    private ChainPattern chainPattern;

    private List<Trade> objects;

    @Before
    public void before() {
        String jsonString = FileUtil.readUtf8String("node_pattern_list.json");
        //CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)
        List<NodePattern> nodePatternList = JSONUtil.toList(jsonString, NodePattern.class);
        ChainPattern tempChainPattern = new ChainPattern();
        tempChainPattern.setNodePatternList(nodePatternList);
        tempChainPattern.setAggregateType("pattern");
        this.chainPattern = tempChainPattern;

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
    public void init() {
    }

    /**
     * 测试process方法
     */
    @Test
    public void testProcess() {
        BaseUdafParam baseUdafParam = new BaseUdafParam();
        baseUdafParam.setAggregateType("SUM");
        baseUdafParam.setMetricExpress("amt");
        Map<String, Class<?>> fieldMap = new HashMap<>();
        fieldMap.put("amt", Long.class);

        EventStateExtractor<Trade, MatchState<TreeMap<NodePattern, CloneWrapper<Trade>>>>  eventStateExtractor =
                FieldProcessorUtil.getEventStateExtractor(chainPattern, baseUdafParam, fieldMap, getTestUnitFactory());

        List<NodePattern> nodePatternList = chainPattern.getNodePatternList();

        NodePattern start = nodePatternList.get(0);
        NodePattern mid = nodePatternList.get(1);
        NodePattern end = nodePatternList.get(2);

        //CEP链
        //start     amt > 100
        //mid       amt > 200   不超过10ms(start和mid之间时间差)
        //end       amt > 300   不超过10ms(mid和end之间时间差)

        MatchState<TreeMap<NodePattern, CloneWrapper<Trade>>> process = eventStateExtractor.process(objects.get(0));

        assertEquals(process.value().get(start).value(), objects.get(0));
        assertNull(process.value().get(mid));
        assertNull(process.value().get(end));

        process = eventStateExtractor.process(objects.get(4));
        assertEquals(process.value().get(start).value(), objects.get(4));
        assertEquals(process.value().get(mid).value(), objects.get(4));
        assertNull(process.value().get(end));

        process = eventStateExtractor.process(objects.get(8));
        assertEquals(process.value().get(start).value(), objects.get(8));
        assertEquals(process.value().get(mid).value(), objects.get(8));
        assertEquals(process.value().get(end).value(), objects.get(8));
    }

}