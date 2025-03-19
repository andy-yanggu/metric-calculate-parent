package com.yanggu.metric_calculate.core.util;


import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONUtil;

import java.util.*;

/**
 * 有向无环图（DAG）工具类
 */
public class DAGUtil {

    /**
     * DAG的拓扑排序
     */
    public static List<String> topologicalSort(Map<String, Set<String>> graph) {
        // 初始化每个节点的入度
        Map<String, Integer> inDegree = new HashMap<>();
        for (String node : graph.keySet()) {
            inDegree.put(node, 0);
        }
        // 计算每个节点的入度
        for (Set<String> neighbors : graph.values()) {
            for (String neighbor : neighbors) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        // 将入度为 0 的节点加入队列（删除冗余检查）
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        // 存储拓扑排序的结果
        List<String> sortedList = new ArrayList<>();
        while (!queue.isEmpty()) {
            String node = queue.poll();
            sortedList.add(node);
            Set<String> neighborList = graph.get(node);
            if (CollUtil.isEmpty(neighborList)) {
                continue;
            }
            // 减少该节点的邻居节点的入度
            for (String neighbor : neighborList) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                // 如果邻居节点的入度变为 0，则加入队列
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        return sortedList;
    }

    public static void main(String[] args) {
        String json = "{\"trans_no\":[],\"account_no_out\":[],\"amount\":[],\"account_no_in\":[],\"trans_timestamp\":[\"trans_date\",\"trans_hour\"]}";
        Map<String, Set<String>> graph = JSONUtil.toBean(json, new TypeReference<Map<String, Set<String>>>() {
        });
        //graph.put("A", new HashSet<>(Arrays.asList("B", "C")));
        //graph.put("B", new HashSet<>(Arrays.asList("D")));
        //graph.put("C", new HashSet<>(Arrays.asList("D")));
        //graph.put("D", new HashSet<>());

        List<String> result = topologicalSort(graph);
        System.out.println(result); // 预期输出：[A, B, C, D] 或 [A, C, B, D]
    }
}
