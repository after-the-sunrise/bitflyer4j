package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;

/**
 * Root of the API library.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Bitflyer4j extends AutoCloseable {

    String getVersion();

    MarketService getMarketService();

    AccountService getAccountService();

    OrderService getOrderService();

}
