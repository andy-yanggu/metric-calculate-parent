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
     * 拓扑排序，且可以获取到并行节点
     *
     * @param graph 无向无环图（DAG）
     * @return 拓扑排序结果。同一个内层List表示一个层级，内层List中的元素表示一个节点，节点之间可以并行
     */
    public static List<List<String>> parallelTopologicalSort(Map<String, Set<String>> graph) {
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

        if (queue.isEmpty()) {
            throw new IllegalArgumentException("Cyclic dependency detected");
        }

        List<List<String>> parallelGroups = new ArrayList<>();
        while (!queue.isEmpty()) {
            //获当前层级的可并行节点数量
            int levelSize = queue.size();
            List<String> currentLevel = new ArrayList<>();

            //处理当前层级所有可并行节点
            for (int i = 0; i < levelSize; i++) {
                String node = queue.poll();
                currentLevel.add(node);

                Set<String> neighborList = graph.get(node);
                if (CollUtil.isEmpty(neighborList)) {
                    continue;
                }
                //更新邻居节点入度
                neighborList.forEach(neighbor -> {
                    inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                    //如果邻居节点的入度变为 0，则加入队列
                    if (inDegree.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }
                });
            }
            parallelGroups.add(currentLevel);
        }

        //如果排序后的节点数量和所有节点数量不一致，则说明存在循环依赖
        if (parallelGroups.stream().mapToInt(List::size).sum() != inDegree.size()) {
            throw new IllegalArgumentException("Cyclic dependency detected");
        }

        return parallelGroups;
    }

    public static void main(String[] args) {
        Map<String, Set<String>> graph6 = Map.of(
                "A", Set.of("B", "C"),
                "B", Set.of("D"),
                "C", Set.of("E"),
                "D", Set.of("F"),
                "E", Set.of("F")
        );
        /*
          [A],
          [B,C],
          [D,E],
          [F]
        */
        parallelTopologicalSort(graph6).forEach(System.out::println);

        graph6 = Map.of(
                "A", Set.of("B", "C"),
                "B", Set.of("D"),
                "C", Set.of("E"),
                "D", Set.of("E"),
                "E", Set.of("F")
        );
        /*
        [A]
        [B, C]
        [D]
        [E]
        [F]
         */
        parallelTopologicalSort(graph6).forEach(System.out::println);
    }

}
