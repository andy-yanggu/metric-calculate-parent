package com.yanggu.metric_calculate.flink.operator;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.flink.api.common.operators.ProcessingTimeService;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.runtime.state.StateInitializationContext;
import org.apache.flink.runtime.state.StateSnapshotContext;
import org.apache.flink.streaming.api.operators.AbstractStreamOperator;
import org.apache.flink.streaming.api.operators.OneInputStreamOperator;
import org.apache.flink.streaming.runtime.streamrecord.StreamRecord;
import org.dromara.hutool.core.collection.CollUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * 攒批组件
 * <p>达到攒批大小或者攒批时间, 就往下游输出一个{@code List<T>}</p>
 * <p>使用处理时间, 不需要进行keyBy</p>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NoKeyProcessTimeMiniBatchOperator<T> extends AbstractStreamOperator<List<T>>
        implements OneInputStreamOperator<T, List<T>>, ProcessingTimeService.ProcessingTimeCallback, Serializable {

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
    private TypeInformation<T> elementTypeInfo;

    /**
     * 本地缓冲
     */
    private transient List<T> localBuffer;

    private transient ScheduledFuture<?> scheduledFuture;

    private transient ListState<T> listState;

    /**
     * 初始化方法
     */
    @Override
    public void initializeState(StateInitializationContext context) throws Exception {
        localBuffer = new ArrayList<>(batchSize + 1);
        ListStateDescriptor<T> listStateDescriptor = new ListStateDescriptor<>("list-state", elementTypeInfo);
        listState = context.getOperatorStateStore().getListState(listStateDescriptor);
        //如果是状态恢复
        if (context.isRestored()) {
            //将状态数据添加到本地缓存中
            for (T element : listState.get()) {
                localBuffer.add(element);
            }
            //状态恢复强制向下游输出
            //由于这时候没有注册定时器, 如果本地缓冲有数据
            //但是后面没有新的数据来, 不会注册新的定时器
            //可能会造成永远不会向下游输出
            if (CollUtil.isNotEmpty(localBuffer)) {
                flush();
            }
        }
    }

    @Override
    public void snapshotState(StateSnapshotContext context) throws Exception {
        //将本地缓存中的数据拷贝到listState中
        listState.clear();
        if (CollUtil.isNotEmpty(localBuffer)) {
            listState.addAll(localBuffer);
        }
    }

    @Override
    public void processElement(StreamRecord<T> element) {
        //添加到本地list中
        localBuffer.add(element.getValue());
        //如果大于batchSize, 直接flush并且return
        if (localBuffer.size() >= batchSize) {
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
        if (CollUtil.isNotEmpty(localBuffer)) {
            flush();
        }
    }

    private void flush() {
        if (CollUtil.isEmpty(localBuffer)) {
            return;
        }
        StreamRecord<List<T>> listStreamRecord = new StreamRecord<>(new ArrayList<>(localBuffer));
        //向下游输出一个List<T>
        output.collect(listStreamRecord);
        //清空本地缓存
        localBuffer.clear();
        //如果之前注册了定时器, 删除定时器
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
    }

}
