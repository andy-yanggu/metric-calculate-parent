package com.yanggu.metric_calculate.config.config;


import com.mybatisflex.annotation.InsertListener;
import com.yanggu.metric_calculate.config.util.ThreadLocalUtil;
import lombok.SneakyThrows;
import org.dromara.hutool.core.reflect.FieldUtil;
import org.springframework.stereotype.Component;

import static com.yanggu.metric_calculate.config.util.Constant.USER_ID;

/**
 * UserIdInsert监听器
 */
@Component
public class UserIdInsertListener implements InsertListener {

    @Override
    @SneakyThrows
    public void onInsert(Object entity) {
        boolean hasField = FieldUtil.hasField(entity.getClass(), USER_ID);
        Object fieldValue = FieldUtil.getFieldValue(entity, USER_ID);
        if (hasField && fieldValue == null) {
            FieldUtil.setFieldValue(entity, USER_ID, ThreadLocalUtil.getUserId());
        }
    }

}
