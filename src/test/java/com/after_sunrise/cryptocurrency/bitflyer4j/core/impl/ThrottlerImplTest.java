package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.Duration.between;
import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ThrottlerImplTest {

    private static final long TIMEOUT = SECONDS.toMillis(10);

    private ThrottlerImpl target;

    private TestModule module;

    private ExecutorService executor;

    private ExecutorService workers;

    @BeforeMethod
    public void setUp() throws Exception {

        executor = Executors.newSingleThreadExecutor();
        workers = Executors.newCachedThreadPool();

        module = new TestModule();
        when(module.getEnvironment().getHttpLimitInterval()).thenReturn(ofMillis(400));
        when(module.getEnvironment().getHttpLimitAddress()).thenReturn(8);
        when(module.getEnvironment().getHttpLimitPrivate()).thenReturn(4);
        when(module.getEnvironment().getHttpLimitDormant()).thenReturn(2);

        when(module.getMock(ExecutorFactory.class).get(ThrottlerImpl.class)).thenReturn(executor);

        target = new ThrottlerImpl(module.createInjector());

    }

    @AfterMethod
    public void tearDown() throws Exception {

        workers.shutdown();

        executor.shutdown();

        workers.awaitTermination(TIMEOUT, MILLISECONDS);

        executor.awaitTermination(TIMEOUT, MILLISECONDS);

    }

    @Test(timeOut = 10 * 1000L)
    public void testRun() throws Exception {

        Thread.currentThread().interrupt();

        target.run();

    }

    @Test(timeOut = 10 * 1000L)
    public void testThrottleAddress() throws Exception {

        // 3 batches (8 + 8 + 4) = 2 clean ups
        testThrottle(target::throttleAddress, 20, ofMillis(800));

    }

    @Test(timeOut = 10 * 1000L)
    public void testThrottlePrivate() throws Exception {

        // 3 batches (4 + 4 + 2) = 2 clean ups
        testThrottle(target::throttlePrivate, 10, ofMillis(800));

    }

    @Test(timeOut = 10 * 1000L)
    public void testThrottleDormant() throws Exception {

        // 3 batches (2 + 2 + 1) = 2 clean ups
        testThrottle(target::throttleDormant, 5, ofMillis(800));

    }


    private void testThrottle(Runnable method, int count, Duration time) throws InterruptedException {

        Instant start = Instant.now();

        for (int i = 0; i < count; i++) {
            workers.submit(method);
        }

        workers.shutdown();

        workers.awaitTermination(TIMEOUT, MILLISECONDS);

        Duration elapsed = between(start, Instant.now());

        assertTrue(elapsed.compareTo(time) >= 0, String.format("Elapsed %s > Throttle %s", elapsed, time));

    }

}