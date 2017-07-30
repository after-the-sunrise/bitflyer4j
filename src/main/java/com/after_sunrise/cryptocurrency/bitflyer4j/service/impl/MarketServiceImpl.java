package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarketServiceImpl extends BaseService implements MarketService {

    private static final Type TYPE_STRINGS = new TypeToken<List<String>>() {
    }.getType();

    private static final Type TYPE_PRODUCTS = new TypeToken<List<ProductImpl>>() {
    }.getType();

    private static final Type TYPE_EXECUTIONS = new TypeToken<List<ExecutionImpl>>() {
    }.getType();

    private static final Type TYPE_CHATS = new TypeToken<List<ChatImpl>>() {
    }.getType();

    @Inject
    public MarketServiceImpl(Injector injector) {
        super(injector);
    }

    @Override
    public CompletableFuture<List<Product>> getProducts() {

        HttpRequest req = HttpRequest.builder().type(PathType.MARKET).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_PRODUCTS));

    }

    @Override
    public CompletableFuture<Board> getBoard(String product) {

        Map<String, String> parameters = prepareParameter(PRODUCT_CODE, product);

        HttpRequest req = HttpRequest.builder().type(PathType.BOARD).parameters(parameters).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), BoardImpl.class));

    }

    @Override
    public CompletableFuture<Tick> getTick(String product) {

        Map<String, String> parameters = prepareParameter(PRODUCT_CODE, product);

        HttpRequest req = HttpRequest.builder().type(PathType.TICKER).parameters(parameters).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TickImpl.class));

    }

    @Override
    public CompletableFuture<List<Execution>> getExecutions(String product, Pagination pagination) {

        Map<String, String> params = prepareParameter(PRODUCT_CODE, product);

        params = prepareParameter(params, pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.EXECUTION).parameters(params).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_EXECUTIONS));

    }

    @Override
    public CompletableFuture<Status> getStatus() {

        HttpRequest req = HttpRequest.builder().type(PathType.HEALTH).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), StatusImpl.class));

    }

    @Override
    public CompletableFuture<List<Chat>> getChats(LocalDate date) {

        String d = date == null ? null : date.format(ISO_LOCAL_DATE);

        Map<String, String> params = prepareParameter(FROM_DATE, d);

        HttpRequest req = HttpRequest.builder().type(PathType.CHAT).parameters(params).build();

        CompletableFuture<HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_CHATS));

    }

}
