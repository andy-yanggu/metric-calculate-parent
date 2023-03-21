package com.yanggu.metric_calculate.core2.middle_store;


import com.yanggu.metric_calculate.core2.kryo.KryoUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDeriveMetricMiddleStore implements DeriveMetricMiddleStore {

    protected <T> T deserialize(byte[] bytes) {
        return KryoUtil.deserialize(bytes);
    }

    protected byte[] serialize(Object object) {
        return KryoUtil.serialize(object);
    }

    public static class DeriveMetricMiddleStoreHolder {

        private DeriveMetricMiddleStoreHolder() {
        }

        public static final String DEFAULT_IMPL = "MEMORY";

        /**
         * DeriveMetricMiddleStore实现类初始化完成后, 需要放入其中
         */
        private static final Map<String, DeriveMetricMiddleStore> STORE_MAP = new HashMap<>();

        public static Map<String, DeriveMetricMiddleStore> getStoreMap() {
            return STORE_MAP;
        }

        static {
            //默认放入memory
            DeriveMetricMiddleStore memory = new DeriveMetricMiddleHashMapStore();
            memory.init();
            STORE_MAP.put(DEFAULT_IMPL, memory);
        }
    }

}
