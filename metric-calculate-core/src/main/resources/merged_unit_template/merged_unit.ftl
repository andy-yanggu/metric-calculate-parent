import cn.hutool.core.lang.Tuple;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import com.yanggu.metric_calculate.core.unit.map.MapUnit;
import com.yanggu.metric_calculate.core.unit.mix_unit.MixedUnit;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import com.yanggu.metric_calculate.core.unit.object.ObjectiveUnit;
import com.yanggu.metric_calculate.core.unit.pattern.EventState;
import com.yanggu.metric_calculate.core.annotation.*;
import com.yanggu.metric_calculate.core.unit.UnitFactory;
import java.util.Map;
<#if unitType == 0>
    //数值型
    NumberUnit numberUnit;
    <#if useParam == true>
        <#if multiNumber == true>
            numberUnit = new ${fullName}<>(initValue, param);
        <#else>
            numberUnit = new ${fullName}<>(UnitFactory.createCubeNumber(initValue), param);
        </#if>
    <#else>
        <#if multiNumber == true>
            numberUnit = new ${fullName}<>(initValue);
        <#else>
            numberUnit = new ${fullName}<>(UnitFactory.createCubeNumber(initValue));
        </#if>
    </#if>
    return numberUnit;
<#elseif unitType == 1>
    //集合型
    CollectionUnit collectionUnit;
    <#if useParam == true>
        collectionUnit = new ${fullName}<>(param);
    <#else>
        collectionUnit = new ${fullName}<>();
    </#if>
    collectionUnit.add(initValue);
    return collectionUnit;
<#elseif unitType == 2>
    //对象型
    ObjectiveUnit objectiveUnit;
    <#if useParam == true>
        objectiveUnit = new ${fullName}<>(param);
    <#else>
        objectiveUnit = new ${fullName}<>();
    </#if>
    objectiveUnit.value(initValue);
    return objectiveUnit;
<#elseif unitType == 3>
    //映射型
    MapUnit mapUnit;
    <#if useParam == true>
        mapUnit = new ${fullName}<>(param);
    <#else>
        mapUnit = new ${fullName}<>();
    </#if>
    Tuple tuple = (Tuple) initValue;
    mapUnit.put(tuple.get(0), tuple.get(1));
    return mapUnit;
<#elseif unitType == 4>
    //混合型
    MixedUnit mixedUnit;
    <#if useParam == true>
        mixedUnit = new ${fullName}<>(param);
    <#else>
        mixedUnit = new ${fullName}<>();
    </#if>
    mixedUnit.addMergeUnit((Map) initValue);
    return mixedUnit;
<#elseif unitType == 5>
    //CEP类型
    EventState eventState;
    eventState = new ${fullName}<>(initValue);
    return eventState;
<#else>
    throw new RuntimeException(clazz.getName() + " not support.");
</#if>