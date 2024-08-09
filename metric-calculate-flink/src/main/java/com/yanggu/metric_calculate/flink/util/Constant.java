package com.yanggu.metric_calculate.flink.util;


import com.yanggu.metric_calculate.core.function_factory.AggregateFunctionFactory;
import com.yanggu.metric_calculate.core.kryo.KryoUtil;
import com.yanggu.metric_calculate.core.kryo.pool.InputPool;
import com.yanggu.metric_calculate.core.kryo.pool.KryoPool;
import com.yanggu.metric_calculate.core.kryo.pool.OutputPool;
import com.yanggu.metric_calculate.core.middle_store.AbstractDeriveMetricMiddleStore;
import com.yanggu.metric_calculate.core.middle_store.DeriveMetricMiddleHashMapKryoStore;

public class Constant {

    public static final String DERIVE = "derive";
    public static final String DERIVE_CONFIG = "derive-config";
    public static final AbstractDeriveMetricMiddleStore DERIVE_METRIC_MIDDLE_STORE;

    static {
        //派生指标外部存储
        DERIVE_METRIC_MIDDLE_STORE = new DeriveMetricMiddleHashMapKryoStore();
        KryoPool kryoPool = new KryoPool(100, AggregateFunctionFactory.ACC_CLASS_LOADER);
        InputPool inputPool = new InputPool(100);
        OutputPool outputPool = new OutputPool(100);
        KryoUtil kryoUtil = new KryoUtil(kryoPool, inputPool, outputPool);
        DERIVE_METRIC_MIDDLE_STORE.setKryoUtil(kryoUtil);
        DERIVE_METRIC_MIDDLE_STORE.init();
    }

    private Constant() {
    }

}
