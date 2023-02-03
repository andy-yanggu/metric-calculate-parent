package com.yanggu.metric_calculate.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultClassResolver;
import com.esotericsoftware.kryo.util.IdentityObjectIntMap;
import com.esotericsoftware.kryo.util.IntMap;
import com.esotericsoftware.kryo.util.ObjectMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.esotericsoftware.kryo.util.Util.getWrapperClass;

public class KryoClassResolver extends DefaultClassResolver {

    private final Map<String, Registration> classNameToRegistration;

    /**
     * Construct.
     */
    public KryoClassResolver() {
        super();
        nameToClass = new ObjectMap<>();
        nameIdToClass = new IntMap<>();
        classToNameId = new IdentityObjectIntMap<>();
        classNameToRegistration = new ConcurrentHashMap<>();
    }

    @Override
    public Registration register(Registration registration) {
        Class type = registration.getType();
        if (type.isPrimitive()) {
            classToRegistration.put(getWrapperClass(type), registration);
            classNameToRegistration.put(getWrapperClass(type).getName(), registration);
        }
        nameToClass.put(type.getName(), type);
        classToRegistration.put(type, registration);
        classNameToRegistration.put(type.getName(), registration);
        return registration;
    }

    @Override
    public Registration writeClass(Output output, Class type) {
        if (type == null) {
            output.writeByte(Kryo.NULL);
            return null;
        }
        Registration registration = kryo.getRegistration(type);
        writeName(output, type, registration);
        return registration;
    }

    @Override
    public Registration readClass(Input input) {
        return input.readByte() == Kryo.NULL ? null : readName(input);
    }

    @Override
    protected void writeName(Output output, Class type, Registration registration) {
        output.writeByte(NAME + 2);
        output.writeString(type.getName());
    }

    @Override
    protected Registration readName(Input input) {
        String className = input.readString();
        Registration registration = classNameToRegistration.get(className);
        if (registration == null ) {
            try {
                return kryo.getRegistration(Class.forName(className));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return registration;
    }

    @Override
    public Registration getRegistration(Class type) {
        return classToRegistration.get(type);
    }

    @Override
    public Registration getRegistration(int classID) {
        return super.getRegistration(classID);
    }

    public Registration unregister(Class<?> clazz) {
        nameToClass.remove(clazz.getCanonicalName());
        return classToRegistration.remove(clazz);
    }

    public Registration unregister(int id) {
        nameIdToClass.remove(id);
        return idToRegistration.remove(id);
    }

}
