package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Throttler;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class ThrottlerImpl implements Throttler {

    private static final long TIMEOUT = SECONDS.toMillis(1L);

    private final Map<KeyType, BlockingQueue<Long>> queues = new ConcurrentHashMap<>();

    private final ReentrantLock removeLock = new ReentrantLock();

    private final Configuration conf;

    private final Environment environment;

    @Inject
    public ThrottlerImpl(Injector injector) {

        this.conf = injector.getInstance(Configuration.class);

        this.environment = injector.getInstance(Environment.class);

    }

    @Override
    public void throttleAddress() {

        log.trace("Throttling address.");

        throttle(HTTP_LIMIT_CRITERIA_ADDRESS);

    }

    @Override
    public void throttlePrivate() {

        log.trace("Throttling private.");

        throttle(HTTP_LIMIT_CRITERIA_PRIVATE);

    }

    @Override
    public void throttleDormant() {

        log.trace("Throttling dormant.");

        throttle(HTTP_LIMIT_CRITERIA_DORMANT);

    }

    @VisibleForTesting
    void throttle(KeyType type) {

        BlockingQueue<Long> queue = queues.computeIfAbsent(type, t -> {

            String size = t.apply(conf);

            return new LinkedBlockingQueue<>(Integer.parseInt(size));

        });

        while (true) {

            long now = environment.getTimeMillis();

            try {

                boolean result = queue.offer(now, TIMEOUT, MILLISECONDS);

                if (result) {
                    break;
                }

            } catch (InterruptedException e) {

                log.warn("Aborting throttle : {}", type);

                break;

            }

            long interval = Long.parseLong(HTTP_LIMIT_INTERVAL.apply(conf));

            log.trace("Cleaning queue : Now={}, Interval=[{}]", now, interval);

            try {

                removeLock.lock();

                while (queue.remainingCapacity() <= 0) {

                    Long head = queue.peek();

                    if (head == null) {
                        break;
                    }

                    if (head >= (now - interval)) {
                        break;
                    }

                    queue.poll();

                    log.trace("Removed queue item : {}", head);

                }

                log.trace("Cleaned queue : RemainingCapacity=[{}]", queue.remainingCapacity());

            } finally {
                removeLock.unlock();
            }


        }

    }

}
