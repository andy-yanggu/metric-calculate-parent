package com.yanggu.metric_calculate.flink.process_function;


import cn.hutool.json.JSONObject;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

public class DeriveCalculateProcessFunction extends ProcessFunction<JSONObject, JSONObject> {



    @Override
    public void processElement(JSONObject entries, ProcessFunction<JSONObject, JSONObject>.Context context, Collector<JSONObject> collector) throws Exception {

    }
}
