package com.yanggu.metric_calculate.flink.source_function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yanggu.metric_calculate.core2.pojo.data_detail_table.Model;
import lombok.Data;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class TableDataSourceFunction extends RichSourceFunction<Model> implements Serializable {

    private static final long serialVersionUID = 6954978801103140496L;

    private volatile boolean flag = true;

    private String url = "http://localhost:8888/mock-model/all-data";

    private Long interval = 5L;

    @Override
    public void run(SourceContext<Model> sourceContext) throws Exception {
        while (flag) {
            //获取所有指标配置数据
            String jsonArray = HttpUtil.get(url);
            if (StrUtil.isBlank(jsonArray)) {
                sleep();
                continue;
            }
            List<Model> list = JSONUtil.toList(jsonArray, Model.class);
            if (CollUtil.isEmpty(list)) {
                sleep();
                continue;
            }
            for (Model model : list) {
                sourceContext.collect(model);
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
