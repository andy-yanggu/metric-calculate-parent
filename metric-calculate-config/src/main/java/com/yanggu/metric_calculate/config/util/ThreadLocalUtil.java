package com.yanggu.metric_calculate.config.util;


import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.HashMap;
import java.util.Map;

import static com.yanggu.metric_calculate.config.util.Constant.USER_ID;

/**
 * ThreadLocalUtil工具类
 */
public class ThreadLocalUtil {

    private static final TransmittableThreadLocal<Map<String, Object>> TRANSMITTABLE_THREAD_LOCAL = TransmittableThreadLocal.withInitial(HashMap::new);

    private ThreadLocalUtil() {
    }

    public static Integer getUserId() {
        return (Integer) TRANSMITTABLE_THREAD_LOCAL.get().get(USER_ID);
    }

    public static void setUserId(Integer userId) {
        TRANSMITTABLE_THREAD_LOCAL.get().put(USER_ID, userId);
    }

    public static void removeUserId() {
        TRANSMITTABLE_THREAD_LOCAL.get().remove(USER_ID);
    }

    public static <T> T getValue(String key) {
        return (T) TRANSMITTABLE_THREAD_LOCAL.get().get(key);
    }

    public static <T> void setValue(String key, T value) {
        TRANSMITTABLE_THREAD_LOCAL.get().put(key, value);
    }

    public static void removeValue(String key) {
        TRANSMITTABLE_THREAD_LOCAL.get().remove(key);
    }

}
