package com.after_sunrise.cryptocurrency.bitflyer4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jTest {

    private static final Logger LOG = LoggerFactory.getLogger(Bitflyer4jTest.class);

    private static final boolean DEBUG = Boolean.valueOf(System.getProperty("bitflyer4j.debug"));

    public static void main(String[] args) {

        Bitflyerj4Factory factory = new Bitflyerj4Factory();

        try (Bitflyer4j api = factory.createInstance()) {

            LOG.info("Status : {}", api.getMarketService().getStatus().get());

            LOG.info("Perms : {}", api.getMarketService().getPermissions().get());

            if (DEBUG) {
                LOG.info("Markets : {}", api.getMarketService().getProducts().get());
            }

            if (DEBUG) {
                LOG.info("Board : {}", api.getMarketService().getBoard(null).get());
            }

            if (DEBUG) {
                LOG.info("Tick : {}", api.getMarketService().getTick("ETH_BTC").get());
            }

            if (DEBUG) {
                LOG.info("Execs : {}", api.getMarketService().getExecutions(null, null).get());
            }

            if (DEBUG) {
                LOG.info("Chats : {}", api.getMarketService().getChats(null).get());
            }

        } catch (Exception e) {

            LOG.info("API failure.", e);

        }

    }

}
