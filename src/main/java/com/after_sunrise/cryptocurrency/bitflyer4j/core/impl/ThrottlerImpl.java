package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Throttler;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;

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
import java.util.concurrent.atomic.AtomicLong;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.lang.Long.MIN_VALUE;
import static java.lang.Long.parseLong;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class ThrottlerImpl implements Throttler, Runnable {

    private final Map<KeyType, BlockingQueue<AtomicLong>> queues = new ConcurrentHashMap<>();

    private final Configuration conf;

    private final Environment environment;

    private final ExecutorService executor;

    @Inject
    public ThrottlerImpl(Injector injector) {

        this.conf = injector.getInstance(Configuration.class);

        this.environment = injector.getInstance(Environment.class);

        this.executor = injector.getInstance(ExecutorFactory.class).get(getClass());

        this.executor.submit(this);

    }

    @Override
    public void run() {

        try {

            while (!executor.isShutdown()) {

                Instant now = Instant.ofEpochMilli(environment.getTimeMillis());

                Duration interval = Duration.ofMillis(parseLong(HTTP_LIMIT_INTERVAL.apply(conf)));

                Instant cutoff = now.minus(interval);

                log.trace("Cleaning : Now=[{}] Interval=[{}] Cutoff=[{}]", now, interval, cutoff);

                SortedSet<Instant> times = new TreeSet<>();

                queues.forEach((k, v) -> {

                    while (!v.isEmpty()) {

                        long time = v.peek().get();

                        if (time == MIN_VALUE) {
                            break; // Race Condition with "AtomicLong#set(long)"
                        }

                        if (time >= cutoff.toEpochMilli()) {

                            times.add(Instant.ofEpochMilli(time));

                            break; // Not expired yet.

                        }

                        AtomicLong al = v.remove();

                        log.trace("Cleared : {} - {} ", k, al);

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
        throttle(HTTP_LIMIT_CRITERIA_ADDRESS);
    }

    @Override
    public void throttlePrivate() {
        throttle(HTTP_LIMIT_CRITERIA_PRIVATE);
    }

    @Override
    public void throttleDormant() {
        throttle(HTTP_LIMIT_CRITERIA_DORMANT);
    }

    private void throttle(KeyType type) {

        try {

            log.trace("Throttling {}", type);

            BlockingQueue<AtomicLong> queue = queues.computeIfAbsent(type, t -> {

                String size = t.apply(conf);

                log.debug("Capacity : {} - {}", t, size);

                return new LinkedBlockingQueue<>(Integer.parseInt(size));

            });

            long timeout = parseLong(HTTP_LIMIT_INTERVAL.apply(conf));

            // Do not set the current time yet.
            AtomicLong ref = new AtomicLong(MIN_VALUE);

            while (!queue.offer(ref, timeout, MILLISECONDS)) {

                log.trace("Pending... {}", type);

            }

            // Only set when the throttle finishes.
            ref.set(environment.getTimeMillis());

            log.trace("Throttled : {} - {}", type, ref);

        } catch (Exception e) {

            // Could be :
            // 1. Capacity is not a number. (NumberFormatException)
            // 2. Interval is not a number. (NumberFormatException)
            // 3. Interrupted.

            log.warn("Bypassed : {} - {}", type, e);

        }

    }

}
