package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.UncaughtExceptionHandler;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ThreadFactoryImpl implements ThreadFactory, UncaughtExceptionHandler, Provider<ExecutorService> {

    private static final String NAME = "bitflyer4j_%05d";

    private final ThreadFactory delegate = Executors.defaultThreadFactory();

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final AtomicLong count = new AtomicLong();

    @Override
    public Thread newThread(Runnable r) {

        Thread t = delegate.newThread(r);

        t.setName(String.format(NAME, count.incrementAndGet()));

        t.setDaemon(true);

        t.setUncaughtExceptionHandler(this);

        return t;

    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.warn("Uncaught exception from thread : " + t.getName(), e);
    }

    @Override
    public ExecutorService get() {
        return Executors.newSingleThreadExecutor(this);
    }

}
