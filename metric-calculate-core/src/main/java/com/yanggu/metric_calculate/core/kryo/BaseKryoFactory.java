package com.yanggu.metric_calculate.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoFactory;

public class BaseKryoFactory implements KryoFactory {

    protected KryoFactory parentFactory;

    public BaseKryoFactory() {
    }

    public BaseKryoFactory(KryoFactory parentFactory) {
        this.parentFactory = parentFactory;
    }

    @Override
    public Kryo create() {
        return parentFactory == null ? KryoUtils.createKryo() : parentFactory.create();
    }
}
