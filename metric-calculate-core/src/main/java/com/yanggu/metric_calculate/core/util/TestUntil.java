package com.yanggu.metric_calculate.core.util;


import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core.enums.TimeUnit;
import com.yanggu.metric_calculate.core.pojo.TimeBaselineDimension;
import com.yanggu.metric_calculate.core.unit.pattern.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestUntil {

    @SneakyThrows
    public static Pattern buildPattern(ChainPattern chainPattern
            /*,
            TimeBaselineDimension<Long> timeBaselineDimension*/) {
        ChainPattern.Node chainStartNode = chainPattern.getStartNode();
        ChainPattern.Node chainEndNode = chainPattern.getEndNode();
        ChainPattern.Connector chainStartLine = chainPattern.getNextNode(chainStartNode.getName());

        // 1. check the pattern contains StartNode and EndNode
        if (chainStartNode == null || chainEndNode == null || chainStartLine == null) {
            log.error("StartNode: [{}], EndNode: [{}], StartLine: [{}] is null in pattern.", chainStartNode, chainEndNode, chainStartLine);
            return null;
        }

        // 2. construct the start pattern
        Pattern patternResult = null;
        List<PatternNode> patternList = new ArrayList<>();
        ChainPattern.Node node = chainStartNode;
        String nextNodeName = chainStartLine.getNextNode();
        // 2.1 get all the pattern node by connectors
        while (node != null) {
            Cond cond = buildCondition(node.getMatchExpress());

            PatternNode patternNode = new PatternNode(node.getName());
            patternNode.setCond(cond);
            if (node.isStartNode()) {
                patternNode.setStart(true);
            } else if (node.isEndNode()) {
                patternNode.setEnd(true);
            }

            patternList.add(patternNode);
            node = chainPattern.getNode(nextNodeName);
            ChainPattern.Connector line = chainPattern.getNextNode(nextNodeName);
            if (line != null) {
                nextNodeName = line.getNextNode();
            } else {
                nextNodeName = null;
            }
        }

        if (patternList.size() != chainPattern.getNodes().size() || patternList.size() != (chainPattern.getLines().size() + 1)) {
            String errMsg = "Expect chain pattern node size[" + patternList.size() + "] but real is [" + chainPattern.getNodes().size() + "]";
        }

        // 2.2 constructor the connectors
        List<EventConnector> connectList = new ArrayList<>();
        int index = 0;
        for (ChainPattern.Connector chainConn : chainPattern.getLines()) {
            PatternNode left = patternList.get(index);
            PatternNode right = patternList.get(index + 1);

            EventConnector connector = new EventConnector(left.getName(), right.getName(), new TimeBaselineCond(new TimeBaselineDimension().setLength(chainConn.getInterval()).setUnit(TimeUnit.MILLS)));
            connectList.add(connector);
            index++;
        }

        for (index = 0; index < connectList.size(); index++) {
            PatternNode pNode = patternList.get(index);
            EventConnector connector = connectList.get(index);
            pNode.setConnector(connector);
            for (PatternNode patternNode : patternList) {
                if (patternNode.getName().equals(connector.getNextNode())) {
                    pNode.setNextNode(patternNode);
                    break;
                }
            }
            patternList.set(index, pNode);
        }
        PatternNode pStartNode = patternList.get(0);

        // 2.3 set EndNode FieldProcessorFunc
        PatternNode pEndNode = patternList.get(patternList.size() - 1);
        pEndNode.setFieldProcessor(null);

        // 2.4 return the pattern
        patternResult = new Pattern(pStartNode, connectList, null);
        patternResult.setMatchAll(chainPattern.isMatchAll());
        String actionName = chainPattern.getAggregateType();
        Number intValue = (chainPattern.getInitValue() != null) ? chainPattern.getInitValue() : 0L;
        if (actionName != null && !actionName.trim().isEmpty() && pEndNode.getFieldProcessor() == null) {
            //patternResult.setValue(UnitFactory.initInstanceByValue(actionName, intValue, BasicType.LONG));
        }
        return patternResult;
    }

    private static Cond<JSONObject> buildCondition(String matchExpress) throws Exception {
        return null;
    }

}
