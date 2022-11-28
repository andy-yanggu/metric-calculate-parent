/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.unit;



import com.yanggu.client.magiccube.enums.BasicType;
import com.yanggu.metriccalculate.annotation.Collective;
import com.yanggu.metriccalculate.annotation.Numerical;
import com.yanggu.metriccalculate.annotation.Objective;
import com.yanggu.metriccalculate.number.CubeDecimal;
import com.yanggu.metriccalculate.number.CubeDouble;
import com.yanggu.metriccalculate.number.CubeLong;
import com.yanggu.metriccalculate.number.CubeNumber;
import com.yanggu.metriccalculate.unit.collection.SortedListUnit;
import com.yanggu.metriccalculate.unit.collection.UniqueCountUnit;
import com.yanggu.metriccalculate.unit.collection.UniqueListUnit;
import com.yanggu.metriccalculate.unit.numeric.*;
import com.yanggu.metriccalculate.unit.obj.ObjectiveUnit;
import com.yanggu.metriccalculate.unit.obj.OccupiedUnit;
import com.yanggu.metriccalculate.unit.obj.ReplacedUnit;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class UnitFactory {

    private static final Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap();

    static {
        methodReflection.put("sum", SumUnit.class);
        methodReflection.put("avg", AvgUnit.class);
        methodReflection.put("count", CountUnit.class);
        methodReflection.put("max", com.yanggu.metriccalculate.unit.numeric.MaxUnit.class);
        methodReflection.put("min", com.yanggu.metriccalculate.unit.numeric.MinUnit.class);
        methodReflection.put("varp", VarpUnit.class);
        methodReflection.put("vars", VarsUnit.class);
        methodReflection.put("cov", CovUnit.class);
        methodReflection.put("distinctCount", UniqueCountUnit.class);
        methodReflection.put("distinctList", UniqueListUnit.class);
        methodReflection.put("increaseCount", IncreaseCountUnit.class);
        methodReflection.put("decreaseCount", DecreaseCountUnit.class);
        methodReflection.put("maxIncreaseCount", MaxIncreaseCountUnit.class);
        methodReflection.put("maxDecreaseCount", MaxDecreaseCountUnit.class);
        methodReflection.put("maxContinuousCount", MaxContinuousCountUnit.class);

        methodReflection.put("replaced", ReplacedUnit.class);
        methodReflection.put("occupied", OccupiedUnit.class);
        methodReflection.put("maxObject", MaxUnit.class);
        methodReflection.put("minObject", MinUnit.class);
        methodReflection.put("sortedListObject", SortedListUnit.class);
    }


    public static Class<? extends MergedUnit> getMergeableClass(String actionType) {
        return methodReflection.get(actionType);
    }

    /**
     * Get unit instance by init value.
     */
    public static MergedUnit initInstanceByValue(String mergeable, Object initValue) throws Exception {
        Class clazz = UnitFactory.getMergeableClass(mergeable);
        if (clazz == null) {
            throw new NullPointerException("MergedUnit class not found.");
        }
        if (clazz.isAnnotationPresent(Numerical.class)) {
            return createNumericUnit(clazz, initValue);
        } else if (clazz.isAnnotationPresent(Collective.class)) {
            return createCollectiveUnit(clazz, initValue);
        } else if (clazz.isAnnotationPresent(Objective.class)) {
            return createObjectiveUnit(clazz, initValue);
        }
        throw new RuntimeException(clazz.getName() + " not support.");
    }

    /**
     * Create unit.
     */
    public static MergedUnit createObjectiveUnit(
            Class<ObjectiveUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().value(initValue);
    }

    /**
     * Create collective unit.
     */
    public static MergedUnit createCollectiveUnit(
            Class<CollectionUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().add(initValue);
    }

    /**
     * Create number unit.
     */
    public static NumberUnit createNumericUnit(
            Class<NumberUnit> clazz, Object initValue) throws Exception {
        Constructor<NumberUnit> constructor = clazz.getConstructor(CubeNumber.class);
        BasicType valueType = BasicType.ofValue(initValue);
        switch (valueType) {
            case LONG:
                return constructor.newInstance(CubeLong.of((Number) initValue));
            case DECIMAL:
                return constructor.newInstance(CubeDecimal.of((Number) initValue));
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
    }
}
