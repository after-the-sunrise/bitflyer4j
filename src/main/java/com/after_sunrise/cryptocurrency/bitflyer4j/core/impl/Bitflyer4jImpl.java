package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.Bitflyer4j;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.google.inject.Injector;

import javax.inject.Inject;
import java.util.concurrent.ExecutorService;

/**
 * Implementation using dependency injection.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jImpl implements Bitflyer4j {

    private final ExecutorService executorService;

    private final MarketService marketService;

    private final OrderService orderService;

    @Inject
    public Bitflyer4jImpl(Injector injector) {

        executorService = injector.getInstance(ExecutorService.class);

        marketService = injector.getInstance(MarketService.class);

        orderService = injector.getInstance(OrderService.class);

    }

    @Override
    public void close() throws Exception {
        executorService.shutdown();
    }

    @Override
    public MarketService getMarketService() {
        return marketService;
    }

    @Override
    public OrderService getOrderService() {
        return orderService;
    }

}
