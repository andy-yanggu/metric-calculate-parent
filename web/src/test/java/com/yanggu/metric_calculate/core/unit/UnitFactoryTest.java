package com.yanggu.metric_calculate.core.unit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.kryo.CoreKryoFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtils;
import com.yanggu.metric_calculate.core.unit.collection.UniqueCountUnit;
import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import com.yanggu.metric_calculate.core.unit.numeric.SumUnit;
import com.yanggu.metric_calculate.core.unit.obj.MaxObjectUnit;
import com.yanggu.metric_calculate.core.value.Cloneable2Wrapper;
import com.yanggu.metric_calculate.core.value.Key;
import com.yanggu.metric_calculate.core.value.KeyValue;
import org.apache.maven.shared.invoker.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

import static com.yanggu.metric_calculate.core.unit.UnitFactory.SCAN_PACKAGE;
import static org.junit.Assert.*;

/**
 * UnitFactory单元测试类
 */
public class UnitFactoryTest {

    private UnitFactory unitFactory;

    @Before
    public void init() throws Exception {
        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();
        this.unitFactory = unitFactory;
    }

    /**
     * <p>没有自定义udaf的情况下, init方法应该将SCAN_PACKAGE路径下的Unit</p>
     * <p>按照key和value的形式保存起来</p>
     *
     * @throws Exception
     */
    @Test
    public void testInit1() throws Exception {
        UnitFactory unitFactory = new UnitFactory();
        unitFactory.init();

        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage(SCAN_PACKAGE, classFilter);

        Map<String, Class<? extends MergedUnit>> unitMap = new HashMap<>();
        classSet.forEach(tempClazz -> {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            unitMap.put(annotation.value(), (Class<? extends MergedUnit>) tempClazz);
        });

        assertEquals(unitFactory.getUnitMap(), unitMap);
    }

    /**
     * 测试聚合函数的唯一标识不能重复
     */
    @Test
    public void testInit2() {
        UnitFactory unitFactory = new UnitFactory();
        Map<String, Class<? extends MergedUnit>> unitMap = unitFactory.getUnitMap();
        Class<CountUnit> countUnitClass = CountUnit.class;
        unitMap.put(countUnitClass.getAnnotation(MergeType.class).value(), countUnitClass);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, unitFactory::init);
        assertEquals("自定义聚合函数唯一标识重复, 重复的全类名: " + countUnitClass.getName(), runtimeException.getMessage());
    }

    /**
     * 测试加载jar包中的udaf函数
     *
     * @throws Exception
     */
    @Test
    public void testInit3() throws Exception {
        String testJarPath = testJarPath();
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(testJarPath));
        unitFactory.init();

        Map<String, Class<? extends MergedUnit>> unitMap = unitFactory.getUnitMap();
        //扫描有MergeType注解
        Filter<Class<?>> classFilter = clazz -> clazz.isAnnotationPresent(MergeType.class);
        //扫描系统自带的聚合函数
        Set<Class<?>> classSet = ClassUtil.scanPackage("com.yanggu.metric_calculate.core.test_unit", classFilter);
        if (CollUtil.isEmpty(classSet)) {
            throw new RuntimeException("测试的Unit为空");
        }
        classSet.forEach(tempClazz -> {
            MergeType annotation = tempClazz.getAnnotation(MergeType.class);
            assertTrue(unitMap.containsKey(annotation.value()));
            assertTrue(unitMap.containsValue(tempClazz));
        });
    }

    /**
     * 测试对象型
     *
     * @throws Exception
     */
    @Test
    public void createObjectiveUnit() throws Exception {
        KeyValue keyValue = new KeyValue(new Key(1), Cloneable2Wrapper.wrap(101));
        MergedUnit unit = unitFactory.initInstanceByValue("MAXOBJECT", keyValue, null);
        assertTrue(unit instanceof MaxObjectUnit);
        assertEquals(keyValue.value(), ((MaxObjectUnit) unit).value());
    }

    /**
     * 测试数值型
     *
     * @throws Exception
     */
    @Test
    public void createNumericUnit() throws Exception {
        MergedUnit unit = unitFactory.initInstanceByValue("SUM", 100L, null);
        assertTrue(unit instanceof SumUnit);
        assertEquals(100L, ((SumUnit) unit).value());
    }

    /**
     * 测试集合型
     *
     * @throws Exception
     */
    @Test
    public void createCollectionUnit() throws Exception {
        KeyValue keyValue = new KeyValue(new Key(1), Cloneable2Wrapper.wrap(101));
        MergedUnit unit = unitFactory.initInstanceByValue("DISTINCTCOUNT", keyValue, null);
        assertTrue(unit instanceof UniqueCountUnit);
        assertEquals(new HashSet(Collections.singleton(keyValue)), ((UniqueCountUnit) unit).asCollection());
        assertEquals(1, ((UniqueCountUnit) unit).value());
    }

    /**
     * 测试自定义udaf
     *
     * @throws Exception
     */
    @Test
    public void testUDAF() throws Exception {
        //初始化UnitFactory
        String pathname = testJarPath();
        UnitFactory unitFactory = new UnitFactory(Collections.singletonList(pathname));
        unitFactory.init();

        //COUNT2和COUNT逻辑一致, 计数的逻辑
        NumberUnit count2 = (NumberUnit) unitFactory.initInstanceByValue("COUNT2", 1L, null);
        assertEquals(1L, count2.value());

        NumberUnit count2That = (NumberUnit) count2.fastClone();
        count2.merge(count2That);
        assertEquals(2L, count2.value());

        //测试Kryo序列化和反序列化自定义的udaf
        KryoPool kryoPool = KryoUtils.createRegisterKryoPool(new CoreKryoFactory(new ArrayList<>(unitFactory.getUnitMap().values())));
        Kryo kryo = kryoPool.borrow();

        byte[] bytes;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            Output output = new Output(byteArrayOutputStream);
            kryo.writeClassAndObject(output, count2);
            output.close();
            bytes = byteArrayOutputStream.toByteArray();
        }

        Input input = new Input(bytes);
        Object result = kryo.readClassAndObject(input);

        assertEquals(count2, result);
    }

    /**
     * 使用maven命令生产udaf-test-1.0.0-SNAPSHOT.jar包
     *
     * @return jar路径
     * @throws Exception
     */
    private String testJarPath() throws Exception {
        String canonicalPath = new File("").getCanonicalPath();
        String separator = File.separator;
        canonicalPath = canonicalPath.substring(0, canonicalPath.lastIndexOf(separator));
        String pathname = canonicalPath + separator + "udaf-test" + separator + "target" + separator + "udaf-test-1.0.0-SNAPSHOT.jar";
        if (new File(pathname).exists()) {
            return pathname;
        }

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(canonicalPath + separator + "pom.xml"));
        //System.out.println(canonicalPath + separator + "pom.xml");
        request.setGoals(Arrays.asList("clean", "package"));
        request.setProjects(Collections.singletonList("udaf-test"));
        request.setAlsoMake(true);
        request.setThreads("2.0C");
        request.setMavenOpts("-Dmaven.test.skip=true");
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv("MAVEN_HOME")));
        InvocationResult execute = invoker.execute(request);
        if (execute.getExitCode() != 0) {
            throw new RuntimeException("udaf-test打包失败, 单元测试执行失败");
        }
        return pathname;
    }

}