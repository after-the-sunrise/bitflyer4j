package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public class ExecutorFactoryImplTest {

    private ExecutorFactoryImpl target;

    @BeforeMethod
    public void setUp() {
        target = new ExecutorFactoryImpl();
    }

    @AfterMethod
    public void tearDown() {
        target.shutdown();
    }

    @Test(timeOut = 5000L)
    public void testGet() throws Exception {

        List<Class<?>> classes = Arrays.asList(String.class, null, Integer.class);

        List<ExecutorService> services = new ArrayList<>();

        for (Class<?> cls : classes) {

            ExecutorService es = target.get(cls);

            services.add(es);

            Class<?> c = cls == null ? target.getClass() : cls;

            assertEquals(es.submit(() -> {
                return Thread.currentThread().getName();
            }).get(), c.getSimpleName() + "_001");

            assertSame(es.submit(() -> {
                return Thread.currentThread().getUncaughtExceptionHandler();
            }).get(), target);

            assertTrue(es.submit(() -> {
                return Thread.currentThread().isDaemon();
            }).get());

            es.submit(() -> {
                throw new RuntimeException("Test Exception");
            });

            assertSame(target.get(cls), es);

        }

        target.shutdown();

        for (ExecutorService es : services) {

            assertTrue(es.isShutdown());

        }

    }

}