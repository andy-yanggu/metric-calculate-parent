package com.yanggu.metric_calculate.core2.middle_store;


import com.yanggu.metric_calculate.core2.kryo.KryoUtil;

public abstract class AbstractDeriveMetricMiddleStore implements DeriveMetricMiddleStore {

    protected <T> T deserialize(byte[] bytes) {
        return KryoUtil.deserialize(bytes);
    }

    protected byte[] serialize(Object object) {
        return KryoUtil.serialize(object);
    }

}
