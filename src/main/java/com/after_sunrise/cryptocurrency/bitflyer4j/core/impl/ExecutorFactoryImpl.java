package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
@Slf4j
public class ExecutorFactoryImpl implements ExecutorFactory, UncaughtExceptionHandler {

    private static class ThreadFactoryImpl implements ThreadFactory {

        private static final String NAME_SUFFIX = "_%03d";

        private final AtomicLong count = new AtomicLong();

        private final String name;

        private final ThreadFactory delegate;

        private final UncaughtExceptionHandler handler;

        private ThreadFactoryImpl(Class<?> cls, ThreadFactory delegate, UncaughtExceptionHandler handler) {
            this.name = cls.getSimpleName() + NAME_SUFFIX;
            this.delegate = delegate;
            this.handler = handler;
        }

        @Override
        public Thread newThread(Runnable r) {

            Thread t = delegate.newThread(r);

            t.setDaemon(true);

            t.setName(String.format(name, count.incrementAndGet()));

            t.setUncaughtExceptionHandler(handler);

            return t;

        }

    }

    private final ThreadFactory delegate = Executors.defaultThreadFactory();

    private final Map<Class<?>, ExecutorService> services = new IdentityHashMap<>();

    @Override
    public void shutdown() {

        synchronized (services) {

            int count = 0;

            Iterator<Entry<Class<?>, ExecutorService>> itr = services.entrySet().iterator();

            while (itr.hasNext()) {

                Entry<Class<?>, ExecutorService> entry = itr.next();

                log.debug("Terminating executor : {}", entry.getKey().getSimpleName());

                entry.getValue().shutdown();

                itr.remove();

                count++;

            }

            log.debug("Terminated {} executors.", count);

        }

    }

    @Override
    public ExecutorService get(Class<?> clazz) {
        return get(clazz, NumberUtils.INTEGER_ONE);
    }

    @Override
    public ExecutorService get(Class<?> clazz, int size) {

        Class<?> cls = clazz == null ? getClass() : clazz;

        ExecutorService executor;

        synchronized (services) {

            executor = services.computeIfAbsent(cls, c -> {

                log.debug("Creating executor : {} ({})", c.getSimpleName(), size);

                ThreadFactory factory = new ThreadFactoryImpl(c, delegate, this);

                return Executors.newFixedThreadPool(size, factory);

            });

        }

        return executor;

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.warn("Uncaught exception : " + t.getName(), e);
    }

}
