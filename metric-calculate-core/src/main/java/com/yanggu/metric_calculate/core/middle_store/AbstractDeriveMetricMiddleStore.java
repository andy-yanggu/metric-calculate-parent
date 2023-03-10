package com.yanggu.metric_calculate.core.middle_store;


import com.yanggu.metric_calculate.core.kryo.KryoUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeriveMetricMiddleStore implements DeriveMetricMiddleStore {

    public static final String DEFAULT_IMPL = "MEMORY";

    /**
     * DeriveMetricMiddleStore实现类初始化完成后, 需要放入其中
     */
    public static final Map<String, DeriveMetricMiddleStore> STORE_MAP = new HashMap<>();

    static {
        //默认放入memory
        DeriveMetricMiddleHashMapStore memory = new DeriveMetricMiddleHashMapStore();
        memory.init();
        STORE_MAP.put(DEFAULT_IMPL, memory);
    }

    protected <T> T deserialize(byte[] bytes) {
        return KryoUtil.deserialize(bytes);
    }

    protected byte[] serialize(Object object) {
        return KryoUtil.serialize(object);
    }

}
