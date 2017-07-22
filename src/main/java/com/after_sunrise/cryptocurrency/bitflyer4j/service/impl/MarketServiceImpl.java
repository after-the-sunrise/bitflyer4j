package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Market;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.MarketImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.configuration.Configuration;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.HTTP_URL_BASE;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.HTTP_URL_MARKET;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarketServiceImpl implements MarketService {

    private static final Type TYPE = new TypeToken<List<MarketImpl>>() {
    }.getType();

    private final HttpClient client;

    private final Gson gson;

    private final String urlBase;

    private final String urlMarket;

    @Inject
    public MarketServiceImpl(Injector injector) {

        Configuration c = injector.getInstance(Configuration.class);

        urlBase = HTTP_URL_BASE.apply(c);

        urlMarket = HTTP_URL_MARKET.apply(c);

        client = injector.getInstance(HttpClient.class);

        gson = injector.getInstance(Gson.class);

    }

    @Override
    public CompletableFuture<List<Market>> getMarkets() {

        CompletableFuture<String> future = client.get(urlBase, urlMarket);

        return future.thenApply(s -> gson.<List<Market>>fromJson(s, TYPE));

    }

}
