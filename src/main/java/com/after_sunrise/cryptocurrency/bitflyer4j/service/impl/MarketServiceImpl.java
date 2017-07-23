package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonMap;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarketServiceImpl implements MarketService {

    private static final Type TYPE_PRODUCTS = new TypeToken<List<ProductImpl>>() {
    }.getType();

    private static final Type TYPE_EXECUTIONS = new TypeToken<List<ExecutionImpl>>() {
    }.getType();

    private static final Type TYPE_CHATS = new TypeToken<List<ChatImpl>>() {
    }.getType();

    private static final Type TYPE_STRINGS = new TypeToken<List<String>>() {
    }.getType();

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final HttpClient client;

    private final Gson gson;

    @Inject
    public MarketServiceImpl(Injector injector) {

        Configuration c = injector.getInstance(Configuration.class);

        client = injector.getInstance(HttpClient.class);

        gson = injector.getInstance(Gson.class);

        log.debug("Initialized.");

    }

    @Override
    public CompletableFuture<List<Product>> getProducts() {

        HttpRequest req = new HttpRequest(PathType.MARKET);

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_PRODUCTS));

    }

    @Override
    public CompletableFuture<Board> getBoard(String product) {

        HttpRequest req;

        if (product != null) {
            req = new HttpRequest(PathType.BOARD, singletonMap("product_code", product));
        } else {
            req = new HttpRequest(PathType.BOARD);
        }

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), BoardImpl.class));

    }

    @Override
    public CompletableFuture<Tick> getTick(String product) {

        HttpRequest req;

        if (product != null) {
            req = new HttpRequest(PathType.TICKER, singletonMap("product_code", product));
        } else {
            req = new HttpRequest(PathType.TICKER);
        }

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TickImpl.class));

    }

    @Override
    public CompletableFuture<List<Execution>> getExecutions(String product, Pagination pagination) {

        // TODO : Handle Pagination

        HttpRequest req;

        if (product != null) {
            req = new HttpRequest(PathType.EXECUTION, singletonMap("product_code", product));
        } else {
            req = new HttpRequest(PathType.EXECUTION);
        }

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_EXECUTIONS));

    }

    @Override
    public CompletableFuture<Status> getStatus() {

        HttpRequest req = new HttpRequest(PathType.HEALTH);

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), StatusImpl.class));

    }

    @Override
    public CompletableFuture<List<Chat>> getChats(LocalDate date) {

        // TODO : Handle Date

        HttpRequest req = new HttpRequest(PathType.CHAT);

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_CHATS));

    }

    @Override
    public CompletableFuture<List<String>> getPermissions() {

        HttpRequest req = new HttpRequest(PathType.PERMISSION);

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_STRINGS));

    }

}
