package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.Bitflyer4j;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.google.inject.Injector;
import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.VERSION;

/**
 * Implementation using dependency injection.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jImpl implements Bitflyer4j {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String version;

    private final ExecutorFactory executorFactory;

    private final MarketService marketService;

    private final AccountService accountService;

    private final OrderService orderService;

    @Inject
    public Bitflyer4jImpl(Injector injector) {

        executorFactory = injector.getInstance(ExecutorFactory.class);

        marketService = injector.getInstance(MarketService.class);

        accountService = injector.getInstance(AccountService.class);

        orderService = injector.getInstance(OrderService.class);

        version = VERSION.apply(injector.getInstance(Configuration.class));

        log.info("Initialized : {}", version);

    }

    @Override
    public void close() throws Exception {

        executorFactory.shutdown();

        log.info("Terminated.");

    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public MarketService getMarketService() {
        return marketService;
    }

    @Override
    public AccountService getAccountService() {
        return accountService;
    }

    @Override
    public OrderService getOrderService() {
        return orderService;
    }

}
