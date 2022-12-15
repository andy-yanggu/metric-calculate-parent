package com.yanggu.metric_calculate.core.aviatorfunction;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.json.JSONObject;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.yanggu.metric_calculate.core.calculate.DeriveMetricCalculate;
import com.yanggu.metric_calculate.core.calculate.MetricCalculate;
import com.yanggu.metric_calculate.core.util.MetricUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yanggu.metric_calculate.core.constant.Constant.*;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GetFunctionTest {

    private JSONObject originData;

    private Expression compile;

    private Map<String, Object> env;

    private DeriveMetricCalculate derive;

    @Before
    public void init() {
        String expression = "get(out_amount_178_sum, -1)";
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        //在Aviator中添加自定义函数
        instance.addFunction(new GetFunction());
        compile = instance.compile(expression, false);

        env = new HashMap<>();

        //准备原始数据
        originData = mockOriginData();
        env.put(ORIGIN_DATA, originData);

        //准备DingoClient

        //准备指标元数据
        HashMap<String, DeriveMetricCalculate> deriveMap = new HashMap<>();
        env.put(DERIVE_METRIC_META_DATA, deriveMap);

        //衍生指标
        MetricCalculate metricCalculate = JSONUtil.toBean(FileUtil.readUtf8String("test.json"), MetricCalculate.class);
        Map<String, Class<?>> fieldMap = MetricUtil.getFieldMap(metricCalculate);

        derive = MetricUtil.initDerive(metricCalculate.getDerive().get(0), fieldMap);

        deriveMap.put("out_amount_178_sum", derive);

    }

    /**
     * 测试没有传递原始数据, 应该报运行时异常
     */
    @Test
    public void testNoOriginData() {
        RuntimeException runtimeException = Assert.assertThrows(RuntimeException.class, () -> compile.execute(new HashMap<>()));
        assertEquals("原始数据为空", runtimeException.getMessage());
    }

    /**
     * 测试生成key的逻辑是否正确
     */
    @Test
    public void testGenerateKey() {
        //when(dingoClient.get(any(Key.class))).thenReturn(record);
        //compile.execute(env);
        //
        ////捕获参数key
        //verify(dingoClient).get(keyCaptor.capture());
        //
        //Key key = keyCaptor.getValue();
        //
        ////验证数据库
        //assertEquals("default", key.getDatabase());
        ////验证表名
        //assertEquals(derive.getStore().getStoreTableList().get(0).getStoreTable(), key.getTable());
        //
        ////验证指标存储宽表的主键(时间字段和维度字段)
        //List<Value> valueList = Arrays.asList(Value.get("20221027"), Value.get("客户1"));
        //assertEquals(valueList.toString(), key.getUserKey().toString());

    }

    /**
     * 场景1，Dingo正常返回数据
     */
    @Test
    public void testDingoReturnValue() {
        double value = 100.0D;
        //when(record.getValue(derive.getStore().getStoreTableList().get(0).getStoreColumn())).thenReturn(value);
        //
        //List<Value> valueList = Arrays.asList(Value.get("20221027"), Value.get("客户1"));
        //Key key = new Key("default", derive.getStore().getStoreTableList().get(0).getStoreTable(), valueList);
        //when(dingoClient.get(eq(key))).thenReturn(record);
        //
        //Object result = compile.execute(env);
        //assertEquals(value, result);
    }

    /**
     * 场景2，Dingo中没有查到数据, 应该返回null
     */
    @Test
    public void testDingoNoReturn() {
        //when(dingoClient.get(any(Key.class))).thenReturn(null);
        //Object execute = compile.execute(env);
        //assertNull(execute);
    }

    @Test
    public void test2() {
        String expression = "get(out_amount_178_sum, -1)";
        AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();
        //在Aviator中添加自定义函数
        instance.addFunction(new GetFunction());
        compile = instance.compile(expression, false);
        List<String> variableNames = compile.getVariableNames();
        System.out.println(compile);
    }

    private JSONObject mockOriginData() {
        JSONObject jsonObject = new JSONObject();
        //客户号
        jsonObject.set("account_no_out", "客户1");
        //交易时间戳
        jsonObject.set("trans_timestamp", 1666950737822L);
        return jsonObject;
    }

}
