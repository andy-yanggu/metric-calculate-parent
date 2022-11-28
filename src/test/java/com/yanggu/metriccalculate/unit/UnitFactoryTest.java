package com.yanggu.metriccalculate.unit;

import com.yanggu.metriccalculate.unit.collection.UniqueCountUnit;
import com.yanggu.metriccalculate.unit.numeric.MaxUnit;
import com.yanggu.metriccalculate.unit.numeric.SumUnit;
import com.yanggu.metriccalculate.value.CloneableWrapper;
import com.yanggu.metriccalculate.value.Key;
import com.yanggu.metriccalculate.value.KeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;


public class UnitFactoryTest {

    @Test
    public void createObjectiveUnit() throws Exception {
        KeyValue keyValue = new KeyValue(new Key(1), CloneableWrapper.wrap(101));
        MergedUnit unit = (MergedUnit) UnitFactory.initInstanceByValue("maxObject", keyValue);
        Assertions.assertTrue(unit instanceof MaxUnit);
        Assertions.assertEquals(keyValue.value(), ((MaxUnit) unit).value());
    }

    @Test
    public void createNumericUnit() throws Exception {
        MergedUnit unit = (MergedUnit) UnitFactory.initInstanceByValue("sum", 100L);
        Assertions.assertTrue(unit instanceof SumUnit);
        Assertions.assertEquals(100L, ((SumUnit) unit).value());
    }

    @Test
    public void createCollectionUnit() throws Exception {
        KeyValue keyValue = new KeyValue(new Key(1), CloneableWrapper.wrap(101));
        MergedUnit unit = (MergedUnit) UnitFactory.initInstanceByValue("distinctCount", keyValue);
        Assertions.assertTrue(unit instanceof UniqueCountUnit);
        Assertions.assertEquals(new HashSet(Collections.singleton(keyValue)), ((UniqueCountUnit) unit).asCollection());
        Assertions.assertEquals(1, ((UniqueCountUnit) unit).value());
    }
}