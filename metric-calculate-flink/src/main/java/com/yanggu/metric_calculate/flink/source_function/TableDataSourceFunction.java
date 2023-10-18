package com.yanggu.metric_calculate.flink.source_function;

import com.yanggu.metric_calculate.core.pojo.data_detail_table.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.json.JSONUtil;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@EqualsAndHashCode(callSuper = true)
public class TableDataSourceFunction extends RichSourceFunction<Model> implements Serializable {

    @Serial
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
