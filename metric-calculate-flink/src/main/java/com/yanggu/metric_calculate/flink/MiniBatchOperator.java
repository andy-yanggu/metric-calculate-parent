package com.yanggu.metric_calculate.flink;


import org.apache.flink.streaming.api.operators.AbstractStreamOperator;
import org.apache.flink.streaming.api.operators.StreamOperator;

public class MiniBatchOperator extends AbstractStreamOperator<String> {

    @Override
    public void open() throws Exception {
        super.open();
    }



}
