package com.yanggu.metric_calculate.flink.source_function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.DataDetailsWideTable;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class TableDataSourceFunction extends RichSourceFunction<DataDetailsWideTable> {

    private volatile boolean flag = true;

    private String url = "http://localhost:8888/mock-model/all-data";

    private Long interval = 5L;

    @Override
    public void run(SourceContext<DataDetailsWideTable> sourceContext) throws Exception {
        while (flag) {
            //获取所有指标配置数据
            String jsonArray = HttpUtil.get(url);
            if (StrUtil.isBlank(jsonArray)) {
                sleep();
                continue;
            }
            List<DataDetailsWideTable> list = JSONUtil.toList(jsonArray, DataDetailsWideTable.class);
            if (CollUtil.isEmpty(list)) {
                sleep();
                continue;
            }
            for (DataDetailsWideTable dataDetailsWideTable : list) {
                sourceContext.collect(dataDetailsWideTable);
            }
            sleep();
        }
    }

    @Override
    public void cancel() {
        flag = false;
    }

    private void sleep() throws InterruptedException {
        TimeUnit.SECONDS.sleep(interval);
    }

}
