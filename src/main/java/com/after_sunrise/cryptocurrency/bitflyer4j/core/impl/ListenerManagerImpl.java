package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ListenerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
@Slf4j
public class ListenerManagerImpl<L> implements ListenerManager<L> {

    private final Set<L> listeners = new ConcurrentSkipListSet<>();

    @Override
    public CompletableFuture<Boolean> addListener(L listener) {

        if (listener == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        boolean result = listeners.add(listener);

        log.trace("Add listener : {} - {}", result, listener);

        return CompletableFuture.completedFuture(result);

    }

    @Override
    public CompletableFuture<Boolean> removeListener(L listener) {

        if (listener == null) {
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }

        boolean result = listeners.remove(listener);

        log.trace("Remove listener : {} - {}", result, listener);

        return CompletableFuture.completedFuture(result);

    }

    public void forEach(Consumer<L> consumer) {

        listeners.forEach(listener -> {

            try {

                log.trace("Listener consumption : {}", listener);

                consumer.accept(listener);

            } catch (RuntimeException e) {

                log.warn("Listener consumption failed : {}", e);

            }

        });

    }

}
