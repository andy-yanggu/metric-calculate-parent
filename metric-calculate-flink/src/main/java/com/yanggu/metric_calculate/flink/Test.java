package com.yanggu.metric_calculate.flink;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core2.calculate.metric.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import com.yanggu.metric_calculate.core2.pojo.metric.Derive;
import com.yanggu.metric_calculate.core2.pojo.metric.Global;
import com.yanggu.metric_calculate.core2.util.MetricUtil;
import com.yanggu.metric_calculate.flink.process_function.MyBroadcastProcessFunction;
import com.yanggu.metric_calculate.flink.source_function.TableDataSourceFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.runtime.state.JavaSerializer;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;
import java.util.Map;

@Slf4j
public class Test {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        MapStateDescriptor<Long, DataDetailsWideTable> dataDetailsWideTableMapStateDescriptor =
                new MapStateDescriptor<>("DataDetailsWideTable", Long.class, DataDetailsWideTable.class);

        DataStreamSource<DataDetailsWideTable> tableSource = env
                .addSource(new TableDataSourceFunction(), "Table-Source");

        SingleOutputStreamOperator<Void> process1 = tableSource.process(new ProcessFunction<DataDetailsWideTable, Void>() {
            @Override
            public void processElement(DataDetailsWideTable dataDetailsWideTable,
                                       ProcessFunction<DataDetailsWideTable, Void>.Context ctx,
                                       Collector<Void> out) throws Exception {
                List<Derive> deriveList = dataDetailsWideTable.getDerive();

                MetricCalculate metricCalculate = BeanUtil.copyProperties(dataDetailsWideTable, MetricCalculate.class);
                MetricUtil.setFieldMap(metricCalculate);
                Map<String, Class<?>> fieldMap = metricCalculate.getFieldMap();
                Long tableId = metricCalculate.getId();

                if (CollUtil.isNotEmpty(deriveList)) {
                    deriveList.forEach(tempDerive -> {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.set("tableId", tableId);
                        jsonObject.set("fieldMap", fieldMap);
                        jsonObject.set("derive", tempDerive);
                        ctx.output(new OutputTag<>("derive-config"), jsonObject);
                    });
                }
                List<Global> globalList = dataDetailsWideTable.getGlobal();
                if (CollUtil.isNotEmpty(globalList)) {
                    //TODO 全局指标
                }
            }
        });

        SideOutputDataStream<JSONObject> deriveConfigDataStream = process1.getSideOutput(new OutputTag<>("derive-config"));

        BroadcastStream<DataDetailsWideTable> broadcast = tableSource
                .broadcast(dataDetailsWideTableMapStateDescriptor);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("172.20.7.143:9092")
                .setGroupId("metric-calculate")
                .setTopics("metric-calculate")
                .build();
        SingleOutputStreamOperator<Object> dataStream = env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "Kafka-Source")
                .connect(broadcast)
                .process(new MyBroadcastProcessFunction());

        MiniBatchOperator<JSONObject> deriveMiniBatchOperator = new MiniBatchOperator<>();
        deriveMiniBatchOperator.setElementSerializer(new JavaSerializer<>());

        //派生指标数据流
        dataStream
                //分流出派生指标数据
                .getSideOutput(new OutputTag<JSONObject>("derive"))
                //.transform("Derive-miniBatchOperator", TypeInformation.of(new TypeHint<List<JSONObject>>() {}), deriveMiniBatchOperator)
                //.process()

                .keyBy(tempData -> tempData.getLong("deriveId"))
                .connect(deriveConfigDataStream)
                .process(new KeyedCoProcessFunction<Long, JSONObject, JSONObject, Object>() {

                    private MetricCalculate metricCalculate;

                    @Override
                    public void processElement1(JSONObject value,
                                                KeyedCoProcessFunction<Long, JSONObject, JSONObject, Object>.Context ctx,
                                                Collector<Object> out) throws Exception {
                        Long deriveId = ctx.getCurrentKey();
                        DeriveMetricCalculate deriveMetricCalculate = metricCalculate.getDeriveMetricCalculateById(deriveId);
                        //deriveMetricCalculate.addInput()
                    }

                    @Override
                    public void processElement2(JSONObject value,
                                                KeyedCoProcessFunction<Long, JSONObject, JSONObject, Object>.Context ctx,
                                                Collector<Object> out) throws Exception {

                    }
                })
        ;

        SideOutputDataStream<JSONObject> globalDataStream = dataStream.getSideOutput(new OutputTag<>("global"));

        env.execute("指标计算服务");
    }



}
