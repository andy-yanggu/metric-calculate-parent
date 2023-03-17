package com.yanggu.metric_calculate.core2.field_process.origin;

import cn.hutool.json.JSONObject;
import com.yanggu.metric_calculate.core2.field_process.FieldProcessor;


public class OriginFieldProcessor implements FieldProcessor<JSONObject, JSONObject> {

    @Override
    public void init() throws Exception {
    }

    @Override
    public JSONObject process(JSONObject input) throws Exception {
        return input;
    }

}
