import ${fullName};
import cn.hutool.core.collection.CollUtil;
import java.util.Map;
import com.yanggu.metric_calculate.core.annotation.MergeType;


MergeType mergeType = ${fullName}.class.getAnnotation(MergeType.class);
CollectionUnit collectionUnit;
if (mergeType.useParam() && CollUtil.isEmpty(param)) {
    collectionUnit = new ${fullName}<>();
} else {
    collectionUnit = new ${fullName}<>(param);
}
collectionUnit.add(initValue);
return collectionUnit;