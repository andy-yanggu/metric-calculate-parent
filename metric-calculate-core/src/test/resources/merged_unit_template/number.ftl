import ${fullName};
import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import java.util.Map;
import com.yanggu.metric_calculate.core.annotation.MergeType;


MergeType mergeType = (MergeType) ${fullName}.class.getAnnotation(MergeType.class);
NumberUnit numberUnit;
<#if useParam == true>
    numberUnit = new ${fullName}<>(initValue, param);
<#else>
    numberUnit = new ${fullName}<>(initValue);
</#if>
return numberUnit;