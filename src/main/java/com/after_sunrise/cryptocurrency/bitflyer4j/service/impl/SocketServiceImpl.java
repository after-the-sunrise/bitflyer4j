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
import io.socket.client.Socket;
import io.socket.emitter.Emitter.Listener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.Loggers.PubNubLogger;
import static java.util.Collections.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
@Slf4j
public class SocketServiceImpl implements RealtimeService {

    private static final Type TYPE_EXECUTIONS = new TypeToken<List<ExecutionImpl>>() {
    }.getType();

    static final String CHANNEL_BOARD = "lightning_board_";

    static final String CHANNEL_BOARD_SNAPSHOT = "lightning_board_snapshot_";

    static final String CHANNEL_TICK = "lightning_ticker_";

    static final String CHANNEL_EXEC = "lightning_executions_";

    static final Logger CLIENT_LOG = LoggerFactory.getLogger(PubNubLogger.class);

    private final Set<RealtimeListener> listeners = synchronizedSet(new HashSet<>());

    private final Map<String, ChannelListener> subscriptions = new ConcurrentHashMap<>();

    private final ExecutorService executor;

    private final Gson gson;

    private final Socket socket;

    @Inject
    public SocketServiceImpl(Injector injector) {

        this.executor = injector.getInstance(ExecutorFactory.class).get(getClass());

        this.gson = injector.getInstance(Gson.class);

        this.socket = injector.getInstance(Socket.class);

        this.socket.connect();

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

                log.warn("Listener consumption failed : " + listener, e);

            }

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeBoard(List<String> products) {

        return subscribe(CHANNEL_BOARD, products, (channel, json) -> {

            String product = StringUtils.removeStart(channel, CHANNEL_BOARD);

            Board value = gson.fromJson(json, BoardImpl.class);

            forEach(l -> l.onBoards(product, value));

        });

    }

    @Override
    public CompletableFuture<List<String>> subscribeBoardSnapshot(List<String> products) {

        return subscribe(CHANNEL_BOARD_SNAPSHOT, products, (channel, json) -> {

            String product = StringUtils.removeStart(channel, CHANNEL_BOARD_SNAPSHOT);

            Board value = gson.fromJson(json, BoardImpl.class);

            forEach(l -> l.onBoardsSnapshot(product, value));

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
    CompletableFuture<List<String>> subscribe(String prefix, List<String> products, BiConsumer<String, String> c) {

        return CompletableFuture.supplyAsync(() -> {

            List<Pair<String, ChannelListener>> channels = new ArrayList<>();

            List<String> sources = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                String channel = prefix + id;

                ChannelListener current = new ChannelListener(channel, executor, c);

                ChannelListener previous = subscriptions.putIfAbsent(channel, current);

                if (previous != null) {

                    log.trace("Already subscribed : {}", channel);

                } else {

                    channels.add(Pair.of(channel, current));

                    sources.add(id);

                }

            }));

            channels.forEach(pair -> {

                log.debug("Subscribing : {}", pair.getLeft());

                socket.on(pair.getLeft(), pair.getRight());

                socket.emit("subscribe", pair.getLeft());


            });

            return unmodifiableList(sources);

        }, executor);

    }


    @Override
    public CompletableFuture<List<String>> unsubscribeBoard(List<String> products) {
        return unsubscribe(CHANNEL_BOARD, products);
    }

    @Override
    public CompletableFuture<List<String>> unsubscribeBoardSnapshot(List<String> products) {
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

            List<Pair<String, ChannelListener>> channels = new ArrayList<>();

            List<String> sources = new ArrayList<>();

            Optional.ofNullable(products).ifPresent(ids -> ids.stream().filter(StringUtils::isNotEmpty).forEach(id -> {

                String channel = prefix + id;

                ChannelListener current = subscriptions.remove(channel);

                if (current == null) {

                    log.trace("Already unsubscribed : {}", channel);

                } else {

                    channels.add(Pair.of(channel, current));

                    sources.add(id);

                }

            }));

            channels.forEach(pair -> {

                log.debug("Unsubscribing : {}", pair.getLeft());

                socket.off(pair.getLeft(), pair.getRight());

                socket.emit("unsubscribe", pair.getLeft());


            });

            return unmodifiableList(sources);

        }, executor);

    }

    static class ChannelListener implements Listener {

        final String channel;

        final ExecutorService executor;

        final BiConsumer<String, String> consumer;

        ChannelListener(String channel, ExecutorService executor, BiConsumer<String, String> c) {
            this.channel = channel;
            this.executor = executor;
            this.consumer = c;
        }

        @Override
        public void call(Object... args) {

            if (ArrayUtils.isEmpty(args)) {
                return;
            }

            for (Object object : args) {

                CLIENT_LOG.trace("REC : {}", object);

                try {

                    executor.submit(() -> {

                        log.trace("Message update : {}", object);

                        consumer.accept(channel, Objects.toString(object));

                    });

                } catch (RejectedExecutionException e) {

                    log.trace("Message update (skipped) : {}", object);

                }

            }

        }

    }

}
