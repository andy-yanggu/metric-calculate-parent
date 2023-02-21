import ${fullName};
import cn.hutool.core.collection.CollUtil;
import com.yanggu.metric_calculate.core.unit.collection.CollectionUnit;
import java.util.Map;
import com.yanggu.metric_calculate.core.annotation.MergeType;


MergeType mergeType = (MergeType) ${fullName}.class.getAnnotation(MergeType.class);
CollectionUnit collectionUnit;
<#if useParam == true>
    collectionUnit = new ${fullName}<>(param);
<#else>
    collectionUnit = new ${fullName}<>();
</#if>
collectionUnit.add(initValue);
return collectionUnit;