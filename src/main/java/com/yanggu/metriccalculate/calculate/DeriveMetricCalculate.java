package com.yanggu.metriccalculate.calculate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanggu.client.magiccube.pojo.*;
import com.yanggu.metriccalculate.fieldprocess.*;
import com.yanggu.metriccalculate.util.RoundAccuracyUtil;
import io.dingodb.common.operation.Column;
import io.dingodb.common.operation.DingoExecResult;
import io.dingodb.common.operation.Operation;
import io.dingodb.common.operation.Value;
import io.dingodb.sdk.client.DingoClient;
import io.dingodb.sdk.common.Key;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.yanggu.client.magiccube.enums.StoreColumnTypeEnum.DIMENSION;
import static com.yanggu.client.magiccube.enums.StoreColumnTypeEnum.TIME;

/**
 * 派生指标计算类
 */
@Data
@Slf4j
@NoArgsConstructor
public class DeriveMetricCalculate implements Calculate<JSONObject, Object> {

    /**
     * 指标名称
     */
    private String name;

    /**
     * 前置过滤条件处理器, 进行过滤处理
     */
    private FilterProcessor filterProcessor;

    /**
     * 度量字段处理器, 提取出度量值
     */
    private MetricFieldProcessor<?> metricFieldProcessor;

    /**
     * 时间字段, 提取出时间戳
     */
    private TimeFieldProcessor timeFieldProcessor;

    /**
     * 时间聚合粒度。包含时间单位和时间长度
     */
    private TimeBaselineDimension timeBaselineDimension;

    /**
     * 聚合类型
     */
    private String aggregateType;

    /**
     * 维度字段处理器
     */
    private DimensionSetProcessor dimensionSetProcessor;

    /**
     * 精度数据
     */
    private RoundAccuracy roundAccuracy;

    /**
     * 存储宽表
     */
    private Store store;

    /**
     * dingo客户端
     */
    private DingoClient dingoClient;

    @Override
    public Object exec(JSONObject rtEvent) throws Exception {
        //执行前置过滤条件
        if (!filterProcessor.process(rtEvent)) {
            if (log.isDebugEnabled()) {
                log.debug("Input discard, input = {}", JSONUtil.toJsonStr(rtEvent));
            }
            return null;
        }

        //执行度量表达式, 提取出度量字段的值
        Object process = metricFieldProcessor.process(rtEvent);
        if (process == null) {
            if (log.isDebugEnabled()) {
                log.debug("Get unit from input, but get null, input = {}", JSONUtil.toJsonStr(rtEvent));
            }
        }

        //计算并存储
        Object save = save(rtEvent, process);

        //精度处理
        return RoundAccuracyUtil.handlerRoundAccuracy(save, roundAccuracy);
    }

    //计算并存储数据到dingo-db数据库中
    public Object save(JSONObject rtEvent, Object process) throws Exception {

        //构建出dingo的主键
        Key key = buildKey(rtEvent, 0);

        StoreTable storeTable = CollUtil.getFirst(store.getStoreTableList());
        List<Operation> operations = Collections.singletonList(
                Operation.add(true, new Column(storeTable.getStoreColumn(), process)));
        List<DingoExecResult> operate = dingoClient.operate(key, key, operations);

        if (CollUtil.isEmpty(operate)) {
            throw new RuntimeException("dingo db计算失败");
        }
        for (DingoExecResult dingoExecResult : operate) {
            if (!dingoExecResult.isSuccess()) {
                throw new RuntimeException("dingo db计算失败");
            }
        }
        return null;
    }

    //根据明细数据和指标存储宽表构建出主键key
    public Key buildKey(JSONObject rtEvent, int offset) {

        //提取出时间戳
        Long timestamp = timeFieldProcessor.process(rtEvent);
        //提取出维度值
        Map<String, Object> dimensionSet = dimensionSetProcessor.process(rtEvent);

        //取第一个指标存储宽表
        StoreTable storeTable = CollUtil.getFirst(store.getStoreTableList());

        //指标存储宽表的时间字段、维度字段和指标字段
        //过滤出时间字段和维度字段作为指标存储宽表的联合主键
        List<Value> collect = storeTable.getStoreColumnDtoList().stream()
                //按照StoreColumnIndex升序排序
                .sorted(Comparator.comparingInt(StoreTableColumn::getStoreColumnIndex))
                .filter(temp -> DIMENSION.equals(temp.getStoreColumnType()) || TIME.equals(temp.getStoreColumnType()))
                .map(temp -> {
                    //如果是维度字段, 从dimensionDataMap获取维度数据
                    if (DIMENSION.equals(temp.getStoreColumnType())) {
                        Object o = dimensionSet.get(temp.getDimensionName());
                        return Value.get(o);
                    } else {
                        //如果是时间字段
                        String storeTimeFormat = storeTable.getStoreTimeFormat();

                        //指标存储宽表日期格式
                        String format = DateUtil.format(DateUtil.offsetMillisecond(new Date(timestamp),
                                timeBaselineDimension.getUnit().toMillis(offset).intValue()), storeTimeFormat);
                        return Value.get(format);
                    }
                })
                .collect(Collectors.toList());

        return new Key("default", storeTable.getStoreTable(), collect);
    }

}
