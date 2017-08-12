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
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
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

    private final Map<String, BiConsumer<String, JsonElement>> subscriptions = new ConcurrentHashMap<>();

    private final Logger clientLog = LoggerFactory.getLogger(PubNubLogger.class);

    private final ExecutorService executor;

    private final Gson gson;

    private final PubNub pubNub;

    @Inject
    public RealtimeServiceImpl(Injector injector) {

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

        return subscribe(CHANNEL_BOARD_SNAPSHOT, products, (channel, json) -> {

            String product = StringUtils.removeStart(channel, CHANNEL_BOARD_SNAPSHOT);

            Board value = gson.fromJson(json, BoardImpl.class);

            forEach(l -> l.onBoards(product, value));

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeTick(List<String> products) {

        return subscribe(CHANNEL_TICK, products, (channel, json) -> {

            String product = StringUtils.removeStart(channel, CHANNEL_TICK);

            Tick value = gson.fromJson(json, TickImpl.class);

            forEach(l -> l.onTicks(product, singletonList(value)));

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeExecution(List<String> products) {

        return subscribe(CHANNEL_EXEC, products, (channel, json) -> {

            String product = StringUtils.removeStart(channel, CHANNEL_EXEC);

            List<Execution> values = gson.fromJson(json, TYPE_EXECUTIONS);

            forEach(l -> l.onExecutions(product, unmodifiableList(values)));

        });

    }

    @VisibleForTesting
    CompletableFuture<List<String>> subscribe(String prefix, List<String> products, BiConsumer<String, JsonElement> c) {

        return CompletableFuture.supplyAsync(() -> {

            List<String> channels = new ArrayList<>();

            List<String> sources = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> {

                ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                    String channel = prefix + id;

                    BiConsumer<String, JsonElement> previous = subscriptions.putIfAbsent(channel, c);

                    if (previous != null) {

                        log.trace("Already subscribed : {}", channel);

                    } else {

                        channels.add(channel);

                        sources.add(id);

                    }

                });

            });

            log.debug("Subscribing : {}", channels);

            if (!channels.isEmpty()) {
                pubNub.subscribe().channels(channels).execute();
            }

            return unmodifiableList(sources);

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

            List<String> sources = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> {

                ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                    String channel = prefix + id;

                    BiConsumer<String, JsonElement> previous = subscriptions.remove(channel);

                    if (previous == null) {

                        log.trace("Already unsubscribed : {}", channel);

                    } else {

                        channels.add(channel);

                        sources.add(id);

                    }

                });

            });

            log.debug("Unsubscribing : {}", channels);

            if (!channels.isEmpty()) {
                pubNub.unsubscribe().channels(channels).execute();
            }

            return unmodifiableList(sources);

        }, executor);

    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {

        List<String> channels = status.getAffectedChannels();

        int code = status.getStatusCode();

        clientLog.trace("STS : [{}] [{}]", channels, code);

        executor.submit(() -> log.debug("Status update : {} - {}", code, channels));

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

        String channel = presence.getChannel();

        String event = presence.getEvent();

        clientLog.trace("PRC : [{}] [{}]", channel, event);

        executor.submit(() -> log.debug("Presence update : {} - {}", channel, event));

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {

        String channel = StringUtils.trimToEmpty(message.getChannel());

        JsonElement je = message.getMessage();

        clientLog.trace("REC : [{}] [{}]", channel, je);

        executor.submit(() -> {

            log.trace("Message update : {} - {}", channel, je);

            try {

                BiConsumer<String, JsonElement> c = subscriptions.get(channel);

                if (c == null) {

                    log.trace("Skipping : {} - [{}]", channel, je);

                } else {

                    log.trace("Processing : {} - [{}]", channel, je);

                    c.accept(channel, je);

                }

            } catch (RuntimeException e) {

                String msg = "Failed to process : {} - [{}] {}";

                log.warn(msg, channel, je, e);

            }

        });

    }

}
