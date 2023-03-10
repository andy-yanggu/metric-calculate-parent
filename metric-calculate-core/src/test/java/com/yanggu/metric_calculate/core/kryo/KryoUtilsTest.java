package com.yanggu.metric_calculate.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import com.yanggu.metric_calculate.core.unit.UnitFactoryTest;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class KryoUtilsTest {

    @Test
    public void test1() throws Exception {
        String testJarPath = UnitFactoryTest.testJarPath();
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(testJarPath));
        unitFactory.init();

        KryoPool kryoPool = new KryoPool(true, false, 100);

        Kryo kryo = kryoPool.obtain();
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Output output = new Output(byteArrayOutputStream);
            kryo.writeClassAndObject(output, unitFactory);
            output.close();
            byte[] bytes = byteArrayOutputStream.toByteArray();

            Input input = new Input(bytes);
            Object result = kryo.readClassAndObject(input);
            assertNotSame(unitFactory, result);
            assertEquals(unitFactory, result);
        }
    }

}