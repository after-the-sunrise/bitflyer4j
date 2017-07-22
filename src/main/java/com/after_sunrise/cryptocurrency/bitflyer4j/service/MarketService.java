package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Market;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface MarketService {

    CompletableFuture<List<Market>> getMarkets();

}
