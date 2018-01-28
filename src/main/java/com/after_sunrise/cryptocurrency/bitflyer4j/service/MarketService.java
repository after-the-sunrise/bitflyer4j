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

    CompletableFuture<List<Product>> getProductsUsa();

    CompletableFuture<List<Product>> getProductsEu();

    CompletableFuture<Board> getBoard(Board.Request request);

    CompletableFuture<Tick> getTick(Tick.Request request);

    CompletableFuture<List<Execution>> getExecutions(Execution.Request request);

    CompletableFuture<Status> getStatus(Status.Request request);

    CompletableFuture<BoardStatus> getBoardStatus(BoardStatus.Request request);

    CompletableFuture<List<Chat>> getChats(Chat.Request request);

    CompletableFuture<List<Chat>> getChatsUsa(Chat.Request request);

    CompletableFuture<List<Chat>> getChatsEu(Chat.Request request);

}
