

package com.yanggu.metric_calculate.core.unit;



import com.yanggu.metric_calculate.client.magiccube.enums.BasicType;
import com.yanggu.metric_calculate.core.annotation.Collective;
import com.yanggu.metric_calculate.core.annotation.Numerical;
import com.yanggu.metric_calculate.core.annotation.Objective;
import com.yanggu.metric_calculate.core.number.CubeDecimal;
import com.yanggu.metric_calculate.core.number.CubeLong;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.collection.SortedListUnit;
import com.yanggu.metric_calculate.core.unit.collection.UniqueCountUnit;
import com.yanggu.metric_calculate.core.unit.collection.UniqueListUnit;
import com.yanggu.metric_calculate.core.unit.numeric.*;
import com.yanggu.metric_calculate.core.unit.obj.*;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.client.magiccube.enums.BasicType.*;

public class UnitFactory {

    private UnitFactory() {

    }

    private static final Map<String, Class<? extends MergedUnit>> methodReflection = new HashMap<>();

    static {
        //数值型
        methodReflection.put("SUM", SumUnit.class);
        methodReflection.put("AVG", AvgUnit.class);
        methodReflection.put("COUNT", CountUnit.class);
        methodReflection.put("MAX", MaxUnit.class);
        methodReflection.put("MIN", MinUnit.class);
        methodReflection.put("VARP", VarpUnit.class);
        methodReflection.put("VARS", VarsUnit.class);
        methodReflection.put("COV", CovUnit.class);
        methodReflection.put("DISTINCTCOUNT", UniqueCountUnit.class);
        methodReflection.put("DISTINCTLIST", UniqueListUnit.class);
        methodReflection.put("INCREASECOUNT", IncreaseCountUnit.class);
        methodReflection.put("DECREASECOUNT", DecreaseCountUnit.class);
        methodReflection.put("MAXINCREASECOUNT", MaxIncreaseCountUnit.class);
        methodReflection.put("MAXDECREASECOUNT", MaxDecreaseCountUnit.class);
        methodReflection.put("MAXCONTINUOUSCOUNT", MaxContinuousCountUnit.class);

        //对象型
        methodReflection.put("REPLACED", ReplacedUnit.class);
        methodReflection.put("OCCUPIED", OccupiedUnit.class);
        methodReflection.put("MAXOBJECT", MaxObjectUnit.class);
        methodReflection.put("MINOBJECT", MinObjectUnit.class);
        methodReflection.put("SORTEDLISTOBJECT", SortedListUnit.class);

        //TODO 以后支持添加自定义的聚合函数
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
    public static MergedUnit createObjectiveUnit(Class<ObjectiveUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().value(initValue);
    }

    /**
     * Create collective unit.
     */
    public static MergedUnit createCollectiveUnit(Class<CollectionUnit> clazz, Object initValue) throws Exception {
        return clazz.newInstance().add(initValue);
    }

    /**
     * Create number unit.
     */
    public static NumberUnit createNumericUnit(Class<NumberUnit> clazz, Object initValue) throws Exception {
        Constructor<NumberUnit> constructor = clazz.getConstructor(CubeNumber.class);
        BasicType valueType = ofValue(initValue);
        switch (valueType) {
            case LONG:
                return constructor.newInstance(CubeLong.of((Number) initValue));
            case DECIMAL:
                return constructor.newInstance(CubeDecimal.of((Number) initValue));
            default:
                throw new IllegalStateException("Unexpected value type: " + valueType);
        }
    }

    public static BasicType ofValue(Object value) {
        if (value instanceof Long) {
            return LONG;
        }
        if (value instanceof String) {
            return STRING;
        }
        if (value instanceof Boolean) {
            return BOOLEAN;
        }
        if (value instanceof BigDecimal) {
            return DECIMAL;
        }
        throw new IllegalArgumentException(String.format("Not support type: %s", value.getClass().getName()));
    }

}
