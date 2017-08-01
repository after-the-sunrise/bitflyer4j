package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.BoardImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.ExecutionImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.TickImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeListener;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeService;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.Loggers.PubNubLogger;
import static java.util.Collections.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
@Slf4j
public class RealtimeServiceImpl extends SubscribeCallback implements RealtimeService {

    private static final Type TYPE_EXECUTIONS = new TypeToken<List<ExecutionImpl>>() {
    }.getType();

    static final String CHANNEL_BOARD_SNAPSHOT = "lightning_board_snapshot_";

    static final String CHANNEL_BOARD = "lightning_board_";

    static final String CHANNEL_TICK = "lightning_ticker_";

    static final String CHANNEL_EXEC = "lightning_executions_";

    private final Set<RealtimeListener> listeners = synchronizedSet(new HashSet<>());

    private final Map<String, Consumer<String>> subscriptions = new ConcurrentHashMap<>();

    private final Logger clientLog = LoggerFactory.getLogger(PubNubLogger.class);

    private final Configuration configuration;

    private final ExecutorService executor;

    private final Gson gson;

    private final PubNub pubNub;

    @Inject
    public RealtimeServiceImpl(Injector injector) {

        this.configuration = injector.getInstance(Configuration.class);

        this.executor = injector.getInstance(ExecutorFactory.class).get(getClass());

        this.gson = injector.getInstance(Gson.class);

        this.pubNub = injector.getInstance(PubNub.class);

        this.pubNub.addListener(this);

        log.debug("Initialized.");

    }

    @Override
    public CompletableFuture<Boolean> addListener(RealtimeListener listener) {

        if (listener == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        boolean result = listeners.add(listener);

        log.trace("Add listener : {} - {}", result, listener);

        return CompletableFuture.completedFuture(result);

    }

    @Override
    public CompletableFuture<Boolean> removeListener(RealtimeListener listener) {

        if (listener == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        boolean result = listeners.remove(listener);

        log.trace("Remove listener : {} - {}", result, listener);

        return CompletableFuture.completedFuture(result);

    }

    @VisibleForTesting
    void forEach(Consumer<RealtimeListener> consumer) {

        listeners.forEach(listener -> {

            try {

                log.trace("Listener consumption : {}", listener);

                consumer.accept(listener);

            } catch (RuntimeException e) {

                log.warn("Listener consumption failed : {}", e);

            }

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeBoard(List<String> products) {

        return subscribe(CHANNEL_BOARD_SNAPSHOT, products, json -> {

            Board value = gson.fromJson(json, BoardImpl.class);

            forEach(l -> l.onBoards(singletonList(value)));

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeTick(List<String> products) {

        return subscribe(CHANNEL_TICK, products, json -> {

            Tick value = gson.fromJson(json, TickImpl.class);

            forEach(l -> l.onTicks(singletonList(value)));

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeExecution(List<String> products) {

        return subscribe(CHANNEL_EXEC, products, json -> {

            List<Execution> values = gson.fromJson(json, TYPE_EXECUTIONS);

            forEach(l -> l.onExecutions(unmodifiableList(values)));

        });

    }


    @VisibleForTesting
    CompletableFuture<List<String>> subscribe(String prefix, List<String> products, Consumer<String> consumer) {

        return CompletableFuture.supplyAsync(() -> {

            List<String> channels = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> {

                ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                    String channel = prefix + id;

                    Consumer<String> previous = subscriptions.putIfAbsent(channel, consumer);

                    if (previous != null) {

                        log.debug("Already subscribed : {}", channel);

                    } else {

                        channels.add(channel);

                    }

                });

            });

            log.debug("Subscribing : {}", channels);

            pubNub.subscribe().channels(channels).execute();

            return unmodifiableList(channels);

        }, executor);

    }


    @Override
    public CompletableFuture<List<String>> unsubscribeBoard(List<String> products) {
        return unsubscribe(CHANNEL_BOARD_SNAPSHOT, products);
    }

    @Override
    public CompletableFuture<List<String>> unsubscribeTick(List<String> products) {
        return unsubscribe(CHANNEL_TICK, products);
    }

    @Override
    public CompletableFuture<List<String>> unsubscribeExecution(List<String> products) {
        return unsubscribe(CHANNEL_EXEC, products);
    }

    @VisibleForTesting
    CompletableFuture<List<String>> unsubscribe(String prefix, List<String> products) {

        return CompletableFuture.supplyAsync(() -> {

            List<String> channels = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> {

                ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                    String channel = prefix + id;

                    Consumer<String> previous = subscriptions.remove(channel);

                    if (previous == null) {

                        log.debug("Already unsubscribed : {}", channel);

                    } else {

                        channels.add(channel);

                    }

                });

            });

            log.debug("Unsubscribing : {}", channels);

            pubNub.unsubscribe().channels(channels).execute();

            return unmodifiableList(channels);

        }, executor);


    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {

        int code = status.getStatusCode();

        List<String> channels = status.getAffectedChannels();

        log.debug("Status update : {} - {}", code, channels);

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

        String channel = presence.getChannel();

        String event = presence.getEvent();

        log.debug("Presence update : {} - {}", channel, event);

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        String channel = message.getChannel();

        String json = message.getMessage().toString();

        log.trace("Message update : {} - {}", channel, json);

        clientLog.trace("REC : [{}] [{}]", channel, json);

        executor.submit(() -> {

            try {

                Consumer<String> c = subscriptions.get(channel);

                if (c == null) {

                    log.trace("Skipping : {} - [{}]", channel, json);

                } else {

                    log.trace("Processing : {} - [{}]", channel, json);

                    c.accept(json);

                }

            } catch (RuntimeException e) {

                String msg = "Failed to process : {} - [{}] {}";

                log.warn(msg, channel, json, e);

            }

        });

    }

}
