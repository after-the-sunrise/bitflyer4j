package com.after_sunrise.cryptocurrency.bitflyer4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jTest {

    private static final Logger LOG = LoggerFactory.getLogger(Bitflyer4jTest.class);

    public static void main(String[] args) {

        Bitflyerj4Factory factory = new Bitflyerj4Factory();

        try (Bitflyer4j api = factory.createInstance()) {

            CompletableFuture<?> markets = api.getMarketService().getMarkets();

            LOG.info("Markets : {}", markets.get());

        } catch (Exception e) {

            LOG.info("API failure.", e);

        }

    }

}
