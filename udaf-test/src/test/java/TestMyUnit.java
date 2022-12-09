import cn.hutool.core.date.DateUtil;
import com.yanggu.metric_calculate.core.number.CubeLong;

import java.util.HashMap;
import java.util.Map;

public class TestMyUnit {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put(MyUnit.Fields.ratio, 0.9D);
        params.put(MyUnit.Fields.continueHour, 3);

        long currentTimeMillis = DateUtil.parseDateTime("2022-12-09 11:06:11").getTime();

        MyUnit<CubeLong> myUnit = new MyUnit<>(CubeLong.of(currentTimeMillis), params);

        System.out.println(myUnit.value());
    }

}
