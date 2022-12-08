import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import lombok.experimental.FieldNameConstants;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 转出账户近7天90%的交易集中在连续3小时完成的天数
 *
 * @param <N>
 */
@MergeType(value = "MYUNIT", useParam = true)
@FieldNameConstants
public class MyUnit<N extends CubeNumber<N>> extends NumberUnit<N, MyUnit<N>> {

    private final TreeMap<String, Long> treeMap = new TreeMap<>();

    private double ratio;

    private Integer continueHour;

    public MyUnit(CubeNumber<N> value, Map<String, Object> params) {
        treeMap.put(DateUtil.format(new Date(value.longValue()), "yyyy-MM-dd HH:00:00"), 1L);
        this.ratio = Double.parseDouble(params.get(Fields.ratio).toString());
        this.continueHour = Integer.parseInt(params.get(Fields.continueHour).toString());
    }

    @Override
    public MyUnit<N> merge(MyUnit<N> that) {
        Map<String, Long> thisMap = this.treeMap;
        that.treeMap.forEach((k, v) -> thisMap.merge(k, v, Long::sum));
        this.ratio = that.ratio;
        this.continueHour = that.continueHour;
        return this;
    }

    @Override
    public MyUnit<N> fastClone() {
        return null;
    }

    @Override
    public Number value() {

        if (CollUtil.isEmpty(treeMap)) {
            return 0;
        }

        //现将treeMap按照一天进行切割
        Map<String, TreeMap<String, Long>> map = new HashMap<>();
        Set<String> strings = treeMap.keySet();
        for (String string : strings) {
            String date = DateUtil.formatDate(DateUtil.parseDateTime(string));
            TreeMap<String, Long> treeMap1 = map.getOrDefault(date, new TreeMap<>());
            map.put(date, treeMap1);
            treeMap1.put(string, treeMap.get(string));
        }

        map.values().forEach(tempTreeMap -> {

        });
        //Map<Integer, Integer> rangMap = new HashMap<>();
        //for (Integer key : treeMap.keySet()) {
        //    int end = key + continueHour;
        //    rangMap.put(key, end);
        //}
        //
        //long total = treeMap.values().stream().mapToLong(Long::longValue).sum();
        //for (Map.Entry<Integer, Integer> entry : rangMap.entrySet()) {
        //    SortedMap<Integer, Long> subMap = treeMap.subMap(entry.getKey(), entry.getValue());
        //    double tempRatio = 1.0 * subMap.values().stream().mapToLong(Long::longValue).sum() / total;
        //    if (tempRatio >= ratio) {
        //        return 1;
        //    }
        //}
        //return 0;
        return 0;
    }

}
