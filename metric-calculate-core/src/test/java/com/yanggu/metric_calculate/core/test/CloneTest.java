package com.yanggu.metric_calculate.core.test;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.ObjectUtil;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.unit.collection.ListObjectUnit;

/**
 * 测试几种深克隆对象方式的性能
 * 序列化、手动new的、json序列化和反序列化、kryo序列化和反序列化方式
 */
public class CloneTest {

    private final int count = 100_0000;

    /**
     * ObjectUtil_cloneByStream工具类
     */
    //@Test
    public void testObjectUtil_cloneByStream() {
        ListObjectUnit<CubeLong> listObjectUnit = new ListObjectUnit<>();
        listObjectUnit.add(CubeLong.of(1L));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            ObjectUtil.cloneByStream(listObjectUnit);
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("ObjectUtil_cloneByStream执行" + count + "次, 总共耗费" + totalTimeMillis + "毫秒");
    }

    /**
     * 测试自己手动new的方式
     */
    //@Test
    public void testFastClone() {
        ListObjectUnit<CubeLong> listObjectUnit = new ListObjectUnit<>();
        listObjectUnit.add(CubeLong.of(1L));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < count; i++) {
            listObjectUnit.fastClone();
        }
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();
        System.out.println("手动new的方式执行" + count + "次, 总共耗费" + totalTimeMillis + "毫秒");
    }

}
