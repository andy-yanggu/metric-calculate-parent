/*
 * Copyright 2019, Zetyun MagicCube All rights reserved.
 */

package com.yanggu.metriccalculate.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.pool.KryoFactory;

import java.util.HashSet;
import java.util.Set;

public class RegisterKryoFactory extends BaseKryoFactory {

    private final Set<Registration> registrations;
    private final Set<Class> classes;

    public RegisterKryoFactory() {
        this(null);
    }

    /**
     * Construct.
     */
    public RegisterKryoFactory(KryoFactory kryoFactory) {
        super(kryoFactory);
        registrations = new HashSet();
        classes = new HashSet();
    }

    @Override
    public Kryo create() {
        Kryo kryo = super.create();
        registrations.forEach(kryo.getClassResolver()::register);
        classes.forEach(kryo::register);
        return kryo;
    }

    /**
     * Register class.
     */
    public void register(Class clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        }
        classes.add(clazz);
    }

    /**
     * Register registration.
     */
    public void register(Registration registration) {
        if (registration == null) {
            throw new NullPointerException();
        }
        registrations.add(registration);
    }

    /**
     * class is register.
     */
    public boolean isRegister(Class<?> clazz) {
        boolean isRegister;
        isRegister = classes.contains(clazz);
        for (Registration registration : registrations) {
            if (registration.getType().equals(clazz)) {
                isRegister = true;
                break;
            }
        }
        return isRegister;
    }

    public void unregister(Class<?> clazz) {
        classes.remove(clazz);
    }

    public void unregister(Registration registration) {
        registrations.remove(registration);
    }
}
