package com.yanggu.metric_calculate.flink.util;


import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleStore;

public class Constant {

    private Constant() {
    }

    public static final String DERIVE = "derive";

    public static final String DERIVE_CONFIG = "derive-config";

    public static final DeriveMetricMiddleStore DERIVE_METRIC_MIDDLE_STORE;

    static {
        //派生指标外部存储
        DERIVE_METRIC_MIDDLE_STORE = new DeriveMetricMiddleHashMapKryoStore();
        DERIVE_METRIC_MIDDLE_STORE.init();
    }

}
