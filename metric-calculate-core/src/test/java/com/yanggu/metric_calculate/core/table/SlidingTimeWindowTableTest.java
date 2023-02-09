package com.yanggu.metric_calculate.core.table;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.RandomUtil;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.unit.numeric.CountUnit;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * SlidingTimeWindowTable滑动时间窗口单元测试类
 */
public class SlidingTimeWindowTableTest {

    private SlidingTimeWindowTable<CountUnit> slidingTimeWindowTable;

    @Before
    public void init() {
        this.slidingTimeWindowTable = new SlidingTimeWindowTable<>();
    }

    /**
     * 测试putValue方法
     * <p>场景1</p>
     * <p>当twoKeyTable中不存在对应时间区间内的CountUnit</p>
     */
    @Test
    public void putValue1() {
        CountUnit countUnit = new CountUnit(CubeLong.of(1L));
        long windowStartTime = 0L;
        long windowEndTime = 10L;
        slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, countUnit);

        CountUnit unit = slidingTimeWindowTable.getTwoKeyTable().get(new Tuple(windowStartTime, windowEndTime));
        assertSame(countUnit, unit);
        assertEquals(countUnit, unit);
    }

    /**
     * 测试putValue方法
     * <p>场景2</p>
     * <p>当twoKeyTable中存在对应时间区间内的CountUnit, 需要进行merge</p>
     */
    @Test
    public void putValue2() {
        CountUnit countUnit = new CountUnit(CubeLong.of(1L));

        long windowStartTime = 0L;
        long windowEndTime = 10L;
        //先在[0, 10)区间中放入一个count:1,value:1
        slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, countUnit);

        //再在[0, 10)区间中放入一个1, 应该得到count:2,value:2
        slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, countUnit.fastClone());

        CountUnit unit = slidingTimeWindowTable.getTwoKeyTable().get(new Tuple(windowStartTime, windowEndTime));
        assertEquals(unit, new CountUnit(CubeLong.of(2L), 2L));
    }

    /**
     * 测试merge方法
     */
    @Test
    public void merge() {
        //slidingTimeWindowTable.merge()
    }

    /**
     * 测试query方法
     * <p>场景1, 当twoKeyTable没有数据应该返回null</p>
     */
    @Test
    public void query1() {
        CountUnit countUnit = slidingTimeWindowTable.query(0L, true, 10L, false);
        assertNull(countUnit);
    }

    /**
     * 测试query方法
     * <p>场景2, 当twoKeyTable有数据应该返回对应的CountUnit</p>
     */
    @Test
    public void query2() {
        long windowStartTime = 0L;
        long windowEndTime = 10L;
        CountUnit countUnit1 = new CountUnit(CubeLong.of(1L));
        slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, countUnit1);

        CountUnit countUnit2 = slidingTimeWindowTable.query(windowStartTime, true, windowEndTime, false);
        assertSame(countUnit1, countUnit2);
        assertEquals(countUnit1, countUnit2);
    }

    /**
     * 测试cloneEmpty方法
     * <p>应该new一个新的SlidingTimeWindowTable</p>
     * <p>并且TwoKeyTable为空</p>
     */
    @Test
    public void cloneEmpty() {
        SlidingTimeWindowTable<CountUnit> windowTable = slidingTimeWindowTable.cloneEmpty();
        assertNotSame(windowTable, slidingTimeWindowTable);
        assertTrue(CollUtil.isEmpty(windowTable.getTwoKeyTable()));
    }

    /**
     * 测试fastClone方法
     * <p>应该new一个新的slidingTimeWindowTable</p>
     * <p>并且new一个新的TwoKeyTable, 数据和原来的一样</p>
     */
    @Test
    public void fastClone() {
        long windowStartTime = 0L;
        long windowEndTime = 10L;
        CountUnit countUnit = new CountUnit(CubeLong.of(1L));
        slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, countUnit);

        SlidingTimeWindowTable<CountUnit> table = slidingTimeWindowTable.fastClone();
        assertNotSame(table, slidingTimeWindowTable);
        assertNotSame(table.getTwoKeyTable(), slidingTimeWindowTable.getTwoKeyTable());
        assertEquals(table.getTwoKeyTable(), slidingTimeWindowTable.getTwoKeyTable());
    }

    /**
     * 测试isEmpty方法
     * <p>当twoKeyTable为空时, 返回true</p>
     */
    @Test
    public void isEmpty1() {
        assertTrue(slidingTimeWindowTable.isEmpty());
    }

    /**
     * 测试isEmpty方法
     * <p>当twoKeyTable为不空时, 返回false</p>
     */
    @Test
    public void isEmpty2() {
        slidingTimeWindowTable.putValue(0L, 10L, new CountUnit(CubeLong.of(1L)));
        assertFalse(slidingTimeWindowTable.isEmpty());
    }

    /**
     * 当twoKeyTable为空时, 应该返回0, 表示没有删除一条数据
     */
    @Test
    public void eliminateExpiredData1() {
        int count = slidingTimeWindowTable.eliminateExpiredData(RandomUtil.randomLong());
        assertEquals(0, count);
    }

    /**
     * 测试是否是windowStart > minTimestamp才删除的逻辑
     */
    @Test
    public void eliminateExpiredData2() {
        slidingTimeWindowTable.putValue(10L, 20L, new CountUnit(CubeLong.of(1L)));

        int count = slidingTimeWindowTable.eliminateExpiredData(9L);
        //如果大于, 不删除
        assertEquals(0, count);

        count = slidingTimeWindowTable.eliminateExpiredData(10L);
        //等于时, 不删除, 只有小于才删除
        assertEquals(0, count);

        count = slidingTimeWindowTable.eliminateExpiredData(11L);
        //应该只删除1条
        assertEquals(1, count);

    }

    /**
     * 测试是否删除所有大于minTimestamp的数据
     */
    @Test
    public void eliminateExpiredData3() {

        long windowStart = RandomUtil.randomLong();
        int count = RandomUtil.randomInt(10, 20);
        for (int i = 0; i < count; i++) {
            long windowStartTime = windowStart + (long) count * i;
            long windowEndTime = windowStart + (long) count * (i + 1);
            slidingTimeWindowTable.putValue(windowStartTime, windowEndTime, new CountUnit(CubeLong.of(1L)));
        }

        //比最大的windowStartTime大1ms, 应该全部删除
        long minTimestamp = windowStart + (long) count * (count - 1) + 1L;
        int eliminateExpiredDataCount = slidingTimeWindowTable.eliminateExpiredData(minTimestamp);

        //删除的数据量, 应该等于放入的数据量
        assertEquals(count, eliminateExpiredDataCount);
        //全部删除, TwoKeyTable应该为空
        assertTrue(slidingTimeWindowTable.getTwoKeyTable().isEmpty());
    }

}
