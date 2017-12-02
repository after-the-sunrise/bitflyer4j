package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarketServiceImpl extends HttpService implements MarketService {

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

        return request(req, TYPE_PRODUCTS);

    }

    @Override
    public CompletableFuture<List<Product>> getProductsUsa() {

        HttpRequest req = HttpRequest.builder().type(PathType.MARKET_USA).build();

        return request(req, TYPE_PRODUCTS);

    }

    @Override
    public CompletableFuture<Board> getBoard(Board.Request request) {

        Map<String, String> parameters = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.BOARD).parameters(parameters).build();

        return request(req, BoardImpl.class);

    }

    @Override
    public CompletableFuture<Tick> getTick(Tick.Request request) {

        Map<String, String> parameters = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TICKER).parameters(parameters).build();

        return request(req, TickImpl.class);

    }

    @Override
    public CompletableFuture<List<Execution>> getExecutions(Execution.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.EXECUTION).parameters(params).build();

        return request(req, TYPE_EXECUTIONS);

    }

    @Override
    public CompletableFuture<Status> getStatus(Status.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.HEALTH).parameters(params).build();

        return request(req, StatusImpl.class);

    }

    @Override
    public CompletableFuture<List<Chat>> getChats(Chat.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.CHAT).parameters(params).build();

        return request(req, TYPE_CHATS);

    }

    @Override
    public CompletableFuture<List<Chat>> getChatsUsa(Chat.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.CHAT_USA).parameters(params).build();

        return request(req, TYPE_CHATS);

    }

}
