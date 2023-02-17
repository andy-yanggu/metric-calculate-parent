package com.yanggu.metric_calculate.core.fieldprocess.pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ChainPattern {
    private static Logger logger = LoggerFactory.getLogger(ChainPattern.class);

    private List<Node> nodes;

    private List<Connector> lines;

    private String aggregateType;

    private Number initValue;

    private boolean isMatchAll;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {

        private String name;

        private boolean isStartNode;

        private boolean isEndNode;

        private String matchExpress;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Connector {
        private String preNode;
        private String nextNode;
        private Integer interval;
    }

    /**
     * getStartNode.
     *
     * @return Node
     */
    public Node getStartNode() {
        Node startNode = null;
        List<Node> nodeList = nodes.stream()
                .filter(x -> x.isStartNode)
                .collect(Collectors.toList());
        if (nodeList.size() != 1) {
            logger.error("Get start node from chain pattern failed, size: [{}]", nodeList.size());
        } else {
            startNode = nodeList.get(0);
        }
        return startNode;
    }

    /**
     * getEndNode.
     *
     * @return endNode
     */
    public Node getEndNode() {
        Node endNode = null;
        List<Node> nodeList = nodes.stream()
                .filter(x -> x.isEndNode)
                .collect(Collectors.toList());
        if (nodeList.size() != 1) {
            logger.error("Get end node from chain pattern failed, size: [{}]", nodeList.size());
        } else {
            endNode = nodeList.get(0);
        }
        return endNode;
    }

    /**
     * getNodeByName.
     *
     * @param name nodeName
     * @return Node
     */
    public Node getNode(String name) {
        Node node = null;
        List<Node> nodeList = nodes.stream()
                .filter(x -> x.name.equals(name))
                .collect(Collectors.toList());
        if (nodeList.size() != 1) {
            logger.error("Get node from chain pattern failed, size: [{}]", nodeList.size());
        } else {
            node = nodeList.get(0);
        }
        return node;
    }

    /**
     * get the start line of the pattern.
     *
     * @param curNodeName curNodeName
     * @return Connector
     */
    public Connector getNextNode(String curNodeName) {
        Connector startConn = null;
        List<Connector> connectorList = lines.stream()
                .filter(x -> x.preNode.equals(curNodeName))
                .collect(Collectors.toList());
        if (connectorList.size() != 1) {
            logger.error("Get start line from connectors failed, size: [{}]", connectorList.size());
        } else {
            startConn = connectorList.get(0);
        }
        return startConn;
    }
}