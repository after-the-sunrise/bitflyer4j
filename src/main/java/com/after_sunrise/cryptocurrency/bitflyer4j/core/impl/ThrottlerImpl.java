package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Throttler;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class ThrottlerImpl implements Throttler, Runnable {

    private final Map<String, BlockingQueue<AtomicReference<Instant>>> queues = new ConcurrentHashMap<>();

    private final Environment environment;

    private final ExecutorService executor;

    @Inject
    public ThrottlerImpl(Injector injector) {

        this.environment = injector.getInstance(Environment.class);

        this.executor = injector.getInstance(ExecutorFactory.class).get(getClass());

        this.executor.submit(this);

    }

    @Override
    public void run() {

        try {

            while (!executor.isShutdown()) {

                Instant now = environment.getNow();

                Duration interval = environment.getHttpLimitInterval();

                Instant cutoff = now.minus(interval);

                log.trace("Cleaning : Now=[{}] Interval=[{}] Cutoff=[{}]", now, interval, cutoff);

                SortedSet<Instant> times = new TreeSet<>();

                queues.forEach((k, v) -> {

                    while (!v.isEmpty()) {

                        Instant time = v.peek().get();

                        if (time == null) {
                            break; // Race Condition with "AtomicLong#set(long)"
                        }

                        if (time.isAfter(cutoff)) {

                            times.add(time);

                            break; // Not expired yet.

                        }

                        v.remove();

                        log.trace("Cleared : {} - {} ", k, time);

                    }

                });

                Instant earliest = times.isEmpty() ? now : times.first();

                Duration sleep = Duration.between(now, earliest.plus(interval));

                log.trace("Scheduling next clean : {}", sleep);

                NANOSECONDS.sleep(sleep.toNanos());

            }

        } catch (InterruptedException e) {

            log.warn("Cleaning interrupted.");

        }

    }

    @Override
    public void throttleAddress() {
        throttle("Address", environment::getHttpLimitAddress);
    }

    @Override
    public void throttlePrivate() {
        throttle("Private", environment::getHttpLimitPrivate);
    }

    @Override
    public void throttleDormant() {
        throttle("Dormant", environment::getHttpLimitDormant);
    }

    @VisibleForTesting
    void throttle(String key, Supplier<Integer> limitSupplier) {

        try {

            log.trace("Throttling {}", key);

            BlockingQueue<AtomicReference<Instant>> queue = queues.computeIfAbsent(key, t -> {

                Integer size = limitSupplier.get();

                log.debug("Capacity : {} - {}", t, size);

                return new LinkedBlockingQueue<>(size);

            });

            Duration timeout = environment.getHttpLimitInterval();

            // Do not set the current time yet.
            AtomicReference<Instant> ref = new AtomicReference<>();

            while (!queue.offer(ref, timeout.toMillis(), MILLISECONDS)) {

                log.trace("Pending... {}", key);

            }

            // Only set when the throttle finishes.
            ref.set(environment.getNow());

            log.trace("Throttled : {} - {}", key, ref);

        } catch (Exception e) {

            log.warn("Bypassed : {} - {}", key, e);

        }

    }

}
