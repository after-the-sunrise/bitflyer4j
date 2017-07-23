package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public interface ListenerManager<L> {

    CompletableFuture<Boolean> addListener(L listener);

    CompletableFuture<Boolean> removeListener(L listener);

}
