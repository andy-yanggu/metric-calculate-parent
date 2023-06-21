package com.yanggu.metric_calculate.flink.source_function;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class TableDataSourceFunction extends RichSourceFunction<DataDetailsWideTable> {

    private volatile boolean flag = true;

    @Override
    public void run(SourceContext<DataDetailsWideTable> sourceContext) throws Exception {
        while (flag) {
            String jsonArray = HttpUtil.get("http://localhost:8888/mock-model/all-data");
            if (StrUtil.isBlank(jsonArray)) {
                return;
            }
            List<DataDetailsWideTable> list = JSONUtil.toList(jsonArray, DataDetailsWideTable.class);
            for (DataDetailsWideTable dataDetailsWideTable : list) {
                sourceContext.collect(dataDetailsWideTable);
            }
            TimeUnit.SECONDS.sleep(5L);
        }
    }

    @Override
    public void cancel() {
        flag = false;
    }

}
