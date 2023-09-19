package com.yanggu.metric_calculate.flink.operator;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.operators.ProcessingTimeService;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.runtime.state.StateInitializationContext;
import org.apache.flink.runtime.state.StateSnapshotContext;
import org.apache.flink.streaming.api.operators.AbstractStreamOperator;
import org.apache.flink.streaming.api.operators.OneInputStreamOperator;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.dromara.hutool.core.collection.CollUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地聚合算子
 * <p>实现本地聚合, 达到攒批大小或者攒批时间, 就往下游输出一个{@code Tuple2<KEY, ACC>}</p>
 * <p>攒批大小使用处理的数据量</p>
 * <p>到达攒批时间, 使用处理时间定时器向下游输出</p>
 * <p>需要开启检查点, 实现一致性</p>
 *
 * @param <KEY> 分区类型
 * @param <IN>  输入数据类型
 * @param <ACC> 中间状态数据类型
 * @param <OUT> 输出数据类型
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocalKeyByAccumulateBatchOperator<KEY, IN, ACC, OUT> extends AbstractStreamOperator<Tuple2<KEY, ACC>>
        implements OneInputStreamOperator<IN, Tuple2<KEY, ACC>>, ProcessingTimeService.ProcessingTimeCallback, Serializable {

    @Serial
    private static final long serialVersionUID = -3891085332645805210L;

    /**
     * 攒批大小
     */
    private Integer batchSize = 100;

    /**
     * 攒批时间, 单位毫秒值
     */
    private Long intervalMs = 200L;

    /**
     * list中数据信息
     */
    private TypeInformation<Tuple2<KEY, ACC>> elementTypeInfo;

    /**
     * key提取器
     */
    private KeySelector<IN, KEY> keySelector;

    /**
     * 聚合函数
     */
    private AggregateFunction<IN, ACC, OUT> aggregateFunction;

    /**
     * 计数器，获取当前批次接收的数据量
     */
    private AtomicInteger currentSize;

    /**
     * 本地缓冲
     */
    private transient Map<KEY, ACC> localMap;

    private transient ScheduledFuture<?> scheduledFuture;

    private transient ListState<Tuple2<KEY, ACC>> listState;

    public LocalKeyByAccumulateBatchOperator(Integer batchSize,
                                             Long intervalMs,
                                             KeySelector<IN, KEY> keySelector,
                                             TypeInformation<Tuple2<KEY, ACC>> elementTypeInfo,
                                             AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        this.batchSize = batchSize;
        this.intervalMs = intervalMs;
        this.keySelector = keySelector;
        this.elementTypeInfo = elementTypeInfo;
        this.aggregateFunction = aggregateFunction;
    }

    public LocalKeyByAccumulateBatchOperator(TypeInformation<Tuple2<KEY, ACC>> elementTypeInfo,
                                             KeySelector<IN, KEY> keySelector,
                                             AggregateFunction<IN, ACC, OUT> aggregateFunction) {
        this.elementTypeInfo = elementTypeInfo;
        this.keySelector = keySelector;
        this.aggregateFunction = aggregateFunction;
    }

    /**
     * 初始化方法
     */
    @Override
    public void initializeState(StateInitializationContext context) throws Exception {
        //初始化本地缓存
        localMap = new HashMap<>();
        //初始化计数器
        currentSize = new AtomicInteger(0);
        ListStateDescriptor<Tuple2<KEY, ACC>> listStateDescriptor = new ListStateDescriptor<>("list-state", elementTypeInfo);
        listState = context.getOperatorStateStore().getListState(listStateDescriptor);
        //如果是状态恢复
        if (context.isRestored()) {
            //将状态数据添加到本地缓存中
            for (Tuple2<KEY, ACC> tuple2 : listState.get()) {
                //这里当作业恢复后如果, 算子的并行度发生改变, 那么相同key的数据可能会到同一个subtask中
                //例如原来算子并行度为2, 数据分别为
                //subtask1 [java:2] [scala:2]
                //subtask2 [java:10] [hadoop:2]
                //如果当前算子并行度调整为1, 那么所有算子状态数据都会发往一个subtask
                //因此需要先get一下, 然后进行merge
                ACC acc = localMap.getOrDefault(tuple2.f0, aggregateFunction.createAccumulator());
                localMap.put(tuple2.f0, aggregateFunction.merge(acc, tuple2.f1));
            }
            //状态恢复强制向下游输出
            //由于这时候没有注册定时器, 如果本地缓冲有数据
            //但是后面没有新的数据来, 不会注册新的定时器
            //可能会造成永远不会向下游输出
            //因此这里强制向下游输出
            if (CollUtil.isNotEmpty(localMap)) {
                flush();
            }
        }
    }

    @Override
    public void snapshotState(StateSnapshotContext context) throws Exception {
        //将本地缓存中的数据拷贝到listState中
        listState.clear();
        if (CollUtil.isNotEmpty(localMap)) {
            for (Map.Entry<KEY, ACC> entry : localMap.entrySet()) {
                listState.add(Tuple2.of(entry.getKey(), entry.getValue()));
            }
        }
    }

    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        IN in = element.getValue();
        KEY key = keySelector.getKey(in);
        ACC acc = localMap.getOrDefault(key, aggregateFunction.createAccumulator());
        acc = aggregateFunction.add(in, acc);
        localMap.put(key, acc);
        //如果大于batchSize, 直接flush并且return
        if (currentSize.incrementAndGet() >= batchSize) {
            flush();
            return;
        }

        //如果没有注册定时器, 注册一个定时器
        if (scheduledFuture == null) {
            long tempTimestamp = processingTimeService.getCurrentProcessingTime() + intervalMs;
            scheduledFuture = processingTimeService.registerTimer(tempTimestamp, this);
        }
    }

    /**
     * 定时器回调函数
     */
    @Override
    public void onProcessingTime(long time) {
        if (CollUtil.isNotEmpty(localMap)) {
            flush();
        }
    }

    private void flush() {
        if (CollUtil.isEmpty(localMap)) {
            return;
        }
        //for循环向下游输出Tuple2<KEY, ACC>
        for (Map.Entry<KEY, ACC> entry : localMap.entrySet()) {
            StreamRecord<Tuple2<KEY, ACC>> tuple2StreamRecord = new StreamRecord<>(Tuple2.of(entry.getKey(), entry.getValue()));
            output.collect(tuple2StreamRecord);
        }
        //清空本地缓存
        localMap.clear();
        currentSize.set(0);
        //如果之前注册了定时器, 删除定时器
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

}
