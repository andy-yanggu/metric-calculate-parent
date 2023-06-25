package com.yanggu.metric_calculate.core2.test;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.Converter;
import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.convert.impl.ClassConverter;
import cn.hutool.core.lang.ParameterizedTypeImpl;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HutoolTest {

    @Test
    public void test1() {
        String jsonStr =
                "{\n" +
                "      \"trans_no\": \"java.lang.String\",\n" +
                "      \"account_no_out\": \"java.lang.String\",\n" +
                "      \"trans_date\": \"java.lang.String\",\n" +
                "      \"debit_amt_out\": \"java.lang.Double\",\n" +
                "      \"d_c_flag_in\": \"java.lang.String\",\n" +
                "      \"trans_time\": \"java.lang.String\",\n" +
                "      \"account_no_in\": \"java.lang.String\",\n" +
                "      \"debit_amt_in\": \"java.lang.Double\",\n" +
                "      \"trans_timestamp\": \"java.lang.String\",\n" +
                "      \"trans_jnls_no\": \"java.lang.String\",\n" +
                "      \"d_c_flag_out\": \"java.lang.String\",\n" +
                "      \"credit_amt_in\": \"java.lang.Double\",\n" +
                "      \"credit_amt_out\": \"java.lang.Double\"\n" +
                "}";
        //System.out.println(jsonStr);
        Type[] actualTypeArguments = new Type[1];
        //actualTypeArguments[0] = new WildcardTypeI
        //ParameterizedTypeImpl parameterizedType = new ParameterizedTypeImpl(actualTypeArguments, null, Class.class);
        //注册Class转换器
        //ConverterRegistry.getInstance().putCustom(parameterizedType, new ClassConverter(false));
        //Map<String, Class<?>> jsonObject = JSONUtil.toBean(jsonStr, new TypeReference<Map<String, Class<?>>>() {}, false);
        //System.out.println(jsonObject);
    }

}
