package com.yanggu.metric_calculate.core.calculate;

import cn.hutool.core.util.StrUtil;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.fieldprocess.DimensionSetProcessor;
import com.yanggu.metric_calculate.core.fieldprocess.TimeFieldProcessor;
import com.yanggu.metric_calculate.core.pojo.RoundAccuracy;
import com.yanggu.metric_calculate.core.pojo.Store;
import com.yanggu.metric_calculate.core.pojo.StoreTable;
import com.yanggu.metric_calculate.core.util.RoundAccuracyUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 复合指标计算类
 */
@Data
@Slf4j
public class CompositeMetricCalculate implements Calculate<Map<String, Object>, Object> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 维度字段处理器
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 编译后的计算表达式
     */
    private Expression expression;

    /**
     * 计算公式.
     */
    private String expressString;

    /**
     * 表达式解析后的参数.
     */
    private List<String> paramList;

    /**
     * 计算精度
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 存储相关信息
     */
    private Store store;

    @Override
    public Object exec(Map<String, Object> env) {
        //执行表达式
        Object result = expression.execute(env);
        if (log.isDebugEnabled()) {
            log.debug("表达式: {}, env: {}, 结果: {}", expressString, env, StrUtil.toString(result));
        }
        //处理精度
        result = RoundAccuracyUtil.handlerRoundAccuracy(result, roundAccuracy);

        //存储到外部数据库
        if (result != null) {
            save(result);
        }

        return result;
    }

    @Override
    public void save(Object result) throws RuntimeException {
        if (Boolean.FALSE.equals(store.getIsStore())) {
            return;
        }

        //List<StoreTable> storeTableList = store.getStoreTableList();
        //StoreTable storeTable = storeTableList.get(0);
        //System.out.println(storeTable);


    }
}
