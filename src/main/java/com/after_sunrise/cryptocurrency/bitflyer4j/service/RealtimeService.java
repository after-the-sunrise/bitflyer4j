package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public interface RealtimeService {

    CompletableFuture<Boolean> addListener(RealtimeListener listener);

    CompletableFuture<Boolean> removeListener(RealtimeListener listener);

    CompletableFuture<List<String>> subscribeBoard(List<String> products);

    CompletableFuture<List<String>> subscribeBoardSnapshot(List<String> products);

    CompletableFuture<List<String>> subscribeTick(List<String> products);

    CompletableFuture<List<String>> subscribeExecution(List<String> products);

    CompletableFuture<List<String>> unsubscribeBoard(List<String> products);

    CompletableFuture<List<String>> unsubscribeBoardSnapshot(List<String> products);

    CompletableFuture<List<String>> unsubscribeTick(List<String> products);

    CompletableFuture<List<String>> unsubscribeExecution(List<String> products);

}
