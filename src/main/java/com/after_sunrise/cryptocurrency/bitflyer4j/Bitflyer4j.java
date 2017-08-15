package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeService;

/**
 * Root of the API library.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Bitflyer4j extends AutoCloseable {

    String getVersion();

    String getSite();

    MarketService getMarketService();

    AccountService getAccountService();

    OrderService getOrderService();

    RealtimeService getRealtimeService();

}
