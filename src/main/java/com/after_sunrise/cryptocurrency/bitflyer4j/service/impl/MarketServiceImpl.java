package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Product;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.BoardImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.ProductImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.TickImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
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

}
