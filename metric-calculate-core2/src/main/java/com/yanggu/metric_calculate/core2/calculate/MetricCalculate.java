package com.yanggu.metric_calculate.core2.calculate;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.field.FieldCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.GlobalMetricCalculate;
import com.yanggu.metric_calculate.core2.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标计算类
 * <p>包含了衍生指标、复合指标、全局指标</p>
 */
@Data
public class MetricCalculate extends DataDetailsWideTable {

    /**
     * 字段计算类
     */
    private List<FieldCalculate<JSONObject, Object>> fieldCalculateList;

    /**
     * 派生指标计算类
     */
    private List<DeriveMetricCalculate> deriveMetricCalculateList;

    /**
     * 全局指标计算类
     */
    private List<GlobalMetricCalculate> globalMetricCalculateList;

    /**
     * 指标名称和指标类型映射
     */
    private Map<String, MetricTypeEnum> metricTypeMap;

    /**
     * 明细宽表字段名称和数据类型的map
     * <p>key 字段名称
     * <p>value class数据类型
     */
    private Map<String, Class<?>> fieldMap;

    /**
     * 从原始数据中提取数据, 进行手动数据类型转换
     * <p>虚拟类型的字段进行计算</p>
     * <p>防止输入的数据类型和数据明细宽表定义的数据类型不匹配</p>
     * <p>主要是数值型</p>
     *
     * @param input
     * @return
     */
    @SneakyThrows
    public JSONObject getParam(JSONObject input) {
        if (CollUtil.isEmpty((Map<?, ?>) input)) {
            throw new RuntimeException("输入的明细数据为空");
        }
        JSONObject data = new JSONObject();
        for (FieldCalculate<JSONObject, Object> fieldCalculate : fieldCalculateList) {
            Object process = fieldCalculate.process(input);
            if (process != null) {
                data.set(fieldCalculate.getName(), process);
            }
        }
        return data;
    }

    /**
     * 根据派生指标id查询派生指标
     *
     * @param deriveId
     * @return
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     */
    public <IN, ACC, OUT> DeriveMetricCalculate<IN, ACC, OUT> getDeriveMetricCalculateById(Long deriveId) {
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            throw new RuntimeException("派生指标列表为空");
        }
        return deriveMetricCalculateList.stream()
                .filter(tempDerive -> deriveId.equals(tempDerive.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("传入的deriveId错误"));
    }

}

