package com.yanggu.metric_calculate.flink.source_function;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.concurrent.TimeUnit;


public class TableDataSourceFunction extends RichSourceFunction<DataDetailsWideTable> {

    private volatile boolean flag = true;

    @Override
    public void run(SourceContext<DataDetailsWideTable> sourceContext) throws Exception {
        while (flag) {
            String jsonString = FileUtil.readUtf8String("1.json");
            sourceContext.collect(JSONUtil.toBean(jsonString, DataDetailsWideTable.class));
            TimeUnit.SECONDS.sleep(5L);
        }
    }

    @Override
    public void cancel() {
        flag = false;
    }

}
