package com.yanggu.metric_calculate.core2.table;


public class TableFactory<IN, ACC, OUT> {

    public Table<IN, ACC, OUT> createTable() {
        return new TimeTable<>();
    }

}
