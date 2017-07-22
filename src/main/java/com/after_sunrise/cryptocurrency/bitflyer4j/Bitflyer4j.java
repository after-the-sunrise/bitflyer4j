package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;

/**
 * Root of the API library.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Bitflyer4j extends AutoCloseable {

    MarketService getMarketService();

    OrderService getOrderService();

}
