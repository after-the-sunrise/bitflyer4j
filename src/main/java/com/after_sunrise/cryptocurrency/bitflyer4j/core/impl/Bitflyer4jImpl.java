package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.Bitflyer4j;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.google.inject.Injector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;

import javax.inject.Inject;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.VERSION;

/**
 * Implementation using dependency injection.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class Bitflyer4jImpl implements Bitflyer4j {

    @Getter
    private final String version;

    @Getter
    private final ExecutorFactory executorFactory;

    @Getter
    private final MarketService marketService;

    @Getter
    private final AccountService accountService;

    @Getter
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

}
