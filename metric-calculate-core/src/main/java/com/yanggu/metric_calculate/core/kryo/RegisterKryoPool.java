package com.yanggu.metric_calculate.core.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.pool.KryoCallback;
import com.esotericsoftware.kryo.pool.KryoPool;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class RegisterKryoPool implements KryoPool {

    private final Queue<Kryo> queue;
    private final List<Kryo> allKryoInstance;
    private final RegisterKryoFactory factory;
    private final ReentrantLock lock;

    /**
     * Construct.
     */
    public RegisterKryoPool(RegisterKryoFactory factory, boolean softReference) {
        if (softReference) {
            queue = createSoftReferenceQueue();
        } else {
            queue = new ConcurrentLinkedQueue();
        }
        this.factory = factory;
        allKryoInstance = new ArrayList();
        lock = new ReentrantLock();
    }

    private Queue<Kryo> createSoftReferenceQueue()  {
        try {
            Class<?> softReferenceQueueClass = Class.forName("com.esotericsoftware.kryo.pool.SoftReferenceQueue");
            Constructor<?> softReferenceQueueClassConstructor = softReferenceQueueClass.getConstructor(Queue.class);
            softReferenceQueueClassConstructor.setAccessible(true);
            return (Queue<Kryo>) softReferenceQueueClassConstructor.newInstance(new ConcurrentLinkedQueue());
        } catch (Exception e) {
            return new ConcurrentLinkedQueue();
        }
    }

    @Override
    public Kryo borrow() {
        Kryo res;
        if ((res = queue.poll()) != null) {
            return res;
        }
        Kryo kryo;
        lock.lock();
        try {
            kryo = factory.create();
            allKryoInstance.add(kryo);
        } finally {
            lock.unlock();
        }
        return kryo;
    }

    @Override
    public void release(Kryo kryo) {
        queue.offer(kryo);
    }

    @Override
    public <T> T run(KryoCallback<T> callback) {
        Kryo kryo = borrow();
        try {
            return callback.execute(kryo);
        } finally {
            release(kryo);
        }
    }

    /**
     * register.
     */
    public void register(Class<?> type) {
        lock.lock();
        try {
            factory.register(type);
            if (allKryoInstance.isEmpty()) {
                return;
            } else {
                allKryoInstance.forEach(kryo -> kryo.register(type));
            }
            //register(kryo -> kryo.register(type));
        } finally {
            lock.unlock();
        }
    }

    /**
     * register.
     */
    public void register(Class<?> type, Serializer serializer) {
        lock.lock();
        try {
            factory.register(type);
            if (allKryoInstance.isEmpty()) {
                return;
            } else {
                allKryoInstance.forEach(kryo -> kryo.register(type, serializer));
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * class is register.
     */
    public boolean isRegister(Class<?> clazz) {
        return factory.isRegister(clazz);
    }

    /**
     * unregister.
     */
    public void unregister(Class<?> clazz) {
        lock.lock();
        try {
            if (clazz == null) {
                return;
            }
            allKryoInstance.forEach(kryo -> ((KryoClassResolver)kryo.getClassResolver()).unregister(clazz));
            factory.unregister(clazz);
        } finally {
            lock.unlock();
        }
    }

    /**
     * unregister.
     */
    public void unregister(Registration registration) {
        lock.lock();
        try {
            if (registration == null) {
                return;
            }
            allKryoInstance
                    .forEach(kryo -> ((KryoClassResolver)kryo.getClassResolver()).unregister(registration.getType()));
            factory.unregister(registration);
        } finally {
            lock.unlock();
        }
    }

    /**
     * unregister.
     */
    public void unregister(Integer id) {
        lock.lock();
        try {
            if ( id == null || id <= 0) {
                return;
            }
            allKryoInstance.forEach(kryo -> ((KryoClassResolver)kryo.getClassResolver()).unregister(id));
        } finally {
            lock.unlock();
        }
    }

}
