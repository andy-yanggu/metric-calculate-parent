package com.yanggu.metric_calculate.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.factories.ReflectionSerializerFactory;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.util.MapReferenceResolver;

public class KryoUtils {

    private KryoUtils() {
    }

    public static KryoPool createRegisterKryoPool(KryoFactory kryoFactory) {
        return new RegisterKryoPool(new RegisterKryoFactory(kryoFactory), true);
    }

    /**
     * Create kryo.
     */
    public static Kryo createKryo() {
        Kryo kryo = new Kryo(new KryoClassResolver(), new MapReferenceResolver());
        kryo.setDefaultSerializer(new ReflectionSerializerFactory(CompatibleFieldSerializer.class));
        return kryo;
    }

}
