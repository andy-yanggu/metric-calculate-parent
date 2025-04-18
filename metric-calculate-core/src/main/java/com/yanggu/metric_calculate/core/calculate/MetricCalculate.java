package com.yanggu.metric_calculate.core.calculate;


import com.yanggu.metric_calculate.core.calculate.field.FieldCalculate;
import com.yanggu.metric_calculate.core.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.enums.MetricTypeEnum;
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.MapUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标计算类
 * <p>包含了字段计算、派生指标计算</p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MetricCalculate extends Model implements Serializable {

    @Serial
    private static final long serialVersionUID = 9035602780530630814L;

    /**
     * 字段计算类
     */
    private transient List<List<FieldCalculate<Map<String, Object>, Object>>> fieldCalculateListList;

    /**
     * 字段计算类
     */
    private transient List<FieldCalculate<Map<String, Object>, Object>> fieldCalculateList;

    /**
     * 派生指标计算类
     */
    private transient List<DeriveMetricCalculate> deriveMetricCalculateList;

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

    private KryoUtil kryoUtil;

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
    public Map<String, Object> getParam(Map<String, Object> input) {
        if (MapUtil.isEmpty(input)) {
            throw new RuntimeException("输入的明细数据为空");
        }
        Map<String, Object> processMap = new HashMap<>(input);
        for (List<FieldCalculate<Map<String, Object>, Object>> fieldCalculates : fieldCalculateListList) {
            //这个fieldCalculates可以优化成多线程计算。
            for (FieldCalculate<Map<String, Object>, Object> fieldCalculate : fieldCalculates) {
                //计算得到的结果作为下一个计算的输入
                Object process = fieldCalculate.process(processMap);
                processMap.put(fieldCalculate.getName(), process);
            }
        }
        return processMap;
    }

    /**
     * 根据派生指标id查询派生指标计算类
     *
     * @param deriveId
     * @param <IN>
     * @param <ACC>
     * @param <OUT>
     * @return
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

    /**
     * 根据派生指标id列表获取派生指标计算类列表
     *
     * @param deriveIdList
     * @return
     */
    public List<DeriveMetricCalculate> getDeriveMetricCalculateListById(List<Long> deriveIdList) {
        if (CollUtil.isEmpty(deriveIdList)) {
            throw new RuntimeException("传入的派生指标列表为空");
        }
        if (CollUtil.isEmpty(deriveMetricCalculateList)) {
            throw new RuntimeException("派生指标列表为空");
        }
        return deriveMetricCalculateList.stream()
                .filter(tempDerive -> deriveIdList.contains(tempDerive.getId()))
                .toList();
    }

}

