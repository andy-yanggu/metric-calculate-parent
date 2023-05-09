package com.yanggu.metric_calculate.core2.middle_store;


import com.yanggu.metric_calculate.core2.kryo.KryoUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        public static final Map<String, DeriveMetricMiddleStore> STORE_MAP = new ConcurrentHashMap<>();

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
