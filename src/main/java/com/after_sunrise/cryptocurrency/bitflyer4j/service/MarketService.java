package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface MarketService {

    CompletableFuture<List<Product>> getProducts();

    CompletableFuture<Board> getBoard(String product);

    CompletableFuture<Tick> getTick(String product);

    CompletableFuture<List<Execution>> getExecutions(String product, Pagination pagination);

    CompletableFuture<Status> getStatus();

    CompletableFuture<List<Chat>> getChats(LocalDate date);

    CompletableFuture<List<String>> getPermissions();

}
