package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface MarketService {

    CompletableFuture<List<Product>> getProducts();

    CompletableFuture<Board> getBoard(Board.Request request);

    CompletableFuture<Tick> getTick(Tick.Request request);

    CompletableFuture<List<Execution>> getExecutions(Execution.Request request);

    CompletableFuture<Status> getStatus();

    CompletableFuture<List<Chat>> getChats(Chat.Request request);

}
