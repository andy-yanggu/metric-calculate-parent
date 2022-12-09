import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.annotation.MergeType;
import com.yanggu.metric_calculate.core.number.CubeNumber;
import com.yanggu.metric_calculate.core.unit.numeric.NumberUnit;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.*;

/**
 * 转出账户近7天90%的交易集中在连续3小时完成的天数
 *
 * @param <N>
 */
@MergeType(value = "MYUNIT", useParam = true)
@NoArgsConstructor
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
        //treeMap中保留了过去7天所有聚合到小时的数据
        //现将treeMap按照一天进行切割
        Map<String, TreeMap<String, Long>> multiMap = new HashMap<>();
        for (Map.Entry<String, Long> entry : treeMap.entrySet()) {
            String dateString = entry.getKey();
            Long count = entry.getValue();
            String date = DateUtil.formatDate(DateUtil.parseDateTime(dateString));
            TreeMap<String, Long> tempTreeMap = multiMap.getOrDefault(date, new TreeMap<>());
            multiMap.put(date, tempTreeMap);
            tempTreeMap.put(dateString, count);
        }

        int totalDay = 0;
        //这里对按照天分割后的treeMap进行判断
        for (TreeMap<String, Long> tempTreeMap : multiMap.values()) {
            Map<Integer, Integer> rangMap = new HashMap<>();
            TreeMap<Integer, Long> newTreeMap = new TreeMap<>();
            for (Map.Entry<String, Long> entry : tempTreeMap.entrySet()) {
                //将日期字符串, 格式化成24小时
                int hour = Integer.parseInt(DateUtil.format(DateUtil.parseDateTime(entry.getKey()), "HH"));
                newTreeMap.put(hour, entry.getValue());
                //这里是一个滑动窗口, 左闭右开
                //例如continueHour是3, hour是4
                //窗口是[2, 5)、[3, 6)、[4, 7)
                int start = Math.max(hour - continueHour + 1, 0);
                for (int i = start; i <= hour; i++) {
                    rangMap.put(i, i + continueHour);
                }
            }
            //判断1天90%的交易集中在连续3小时完成
            long total = newTreeMap.values().stream().mapToLong(Long::longValue).sum();
            for (Map.Entry<Integer, Integer> entry : rangMap.entrySet()) {
                SortedMap<Integer, Long> subMap = newTreeMap.subMap(entry.getKey(), entry.getValue());
                double tempRatio = 1.0 * subMap.values().stream().mapToLong(Long::longValue).sum() / total;
                if (tempRatio >= ratio) {
                    totalDay++;
                    break;
                }
            }
        }
        return totalDay;
    }

}
