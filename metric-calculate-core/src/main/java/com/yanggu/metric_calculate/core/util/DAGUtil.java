package com.yanggu.metric_calculate.core.util;


import org.dromara.hutool.core.collection.CollUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 有向无环图（DAG）工具类
 */
public class DAGUtil {

    /**
     * 将逆向的DAG转换为正向的DAG
     */
    public static Map<String, Set<String>> rightToLeft(Map<String, Set<String>> rightGraph) {
        Map<String, Set<String>> leftGraph = new HashMap<>();

        //1. 收集所有节点（包括原图的key和value）
        Set<String> allNodes = new HashSet<>(rightGraph.keySet());
        Set<String> collect = rightGraph.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        allNodes.addAll(collect);

        //2. 初始化所有节点（避免遗漏value中的节点）
        allNodes.forEach(node -> leftGraph.put(node, new HashSet<>()));

        //3. 反转边方向
        rightGraph.forEach((fromNode, toNodes) ->
                toNodes.forEach(toNode -> {
                    // 原边 from → to → 逆边 to ← from
                    leftGraph.get(toNode).add(fromNode);
                })
        );

        return leftGraph;
    }

    /**
     * DAG的拓扑排序
     */
    public static List<String> topologicalSort(Map<String, Set<String>> graph) {
        //初始化每个节点的入度
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }
        //计算每个节点的入度
        for (Set<String> neighbors : graph.values()) {
            if (CollUtil.isNotEmpty(neighbors)) {
                for (String neighbor : neighbors) {
                    inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
                }
            }
        }

        //将入度为0的节点加入队列（删除冗余检查）
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        //没有入度为0的节点，则说明存在循环依赖
        if (queue.isEmpty()) {
            throw new IllegalArgumentException("The graph contains a cycle.");
        }

        //存储拓扑排序的结果
        List<String> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            sortedList.add(node);
            Set<String> neighborList = graph.get(node);
            if (CollUtil.isEmpty(neighborList)) {
                continue;
            }
            //减少该节点的邻居节点的入度
            for (String neighbor : neighborList) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                //如果邻居节点的入度变为 0，则加入队列
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        //如果排序后的节点数量和所有节点数量不一致，则说明存在循环依赖
        if (sortedList.size() != inDegree.size()) {
            throw new IllegalArgumentException("The graph contains a cycle.");
        }

        return sortedList;
    }

    public static void main(String[] args) {
        Map<String, Set<String>> graph = new HashMap<>();
        graph.put("A", Set.of("B", "C"));
        graph.put("B", Set.of("D"));
        graph.put("C", Set.of("D"));
        graph.put("D", Set.of("A"));
        graph.put("E", Set.of("F"));

        List<String> result = topologicalSort(graph);
        //存在循环依赖，应该会报错
        System.out.println(result);
    }

}
