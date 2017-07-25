package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdraw;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.math.BigDecimal.ONE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jTest {

    private static final Logger LOG = LoggerFactory.getLogger(Bitflyer4jTest.class);

    private static final boolean GET = Boolean.valueOf(System.getProperty("bitflyer4j.test_get"));

    private static final boolean POST = Boolean.valueOf(System.getProperty("bitflyer4j.test_post"));

    public static void main(String[] args) {

        Bitflyer4jFactory factory = new Bitflyer4jFactory();

        try (Bitflyer4j api = factory.createInstance()) {

            MarketService marketService = api.getMarketService();

            LOG.info("Status : {}", marketService.getStatus().get());

            if (GET) {

                LOG.info("Markets : {}", marketService.getProducts().get());

                LOG.info("Board : {}", marketService.getBoard(null).get());

                LOG.info("Tick : {}", marketService.getTick(null).get());

                LOG.info("Execs : {}", marketService.getExecutions(null, null).get());

                LOG.info("Chats : {}", marketService.getChats(null).get());

            }

            AccountService accountService = api.getAccountService();

            LOG.info("Perms : {}", accountService.getPermissions().get());

            if (GET) {

                LOG.info("Balances : {}", accountService.getBalances().get());

                LOG.info("Collateral : {}", accountService.getCollateral().get());

                LOG.info("Margins : {}", accountService.getMargins().get());

                LOG.info("Addresses : {}", accountService.getAddresses().get());

                LOG.info("CoinIns : {}", accountService.getCoinIns(null).get());

                LOG.info("CoinOuts : {}", accountService.getCoinOuts(null).get());

                LOG.info("Banks : {}", accountService.getBanks().get());

                LOG.info("Deposits : {}", accountService.getDeposits(null).get());

                LOG.info("Withdrawals : {}", accountService.getWithdrawals(null).get());

            }

            if (POST) {

                Withdraw.Request request = new Withdraw.Request("JPY", 0L, ONE, "000000");

                LOG.info(" : {}", accountService.withdraw(request).get());

            }

            OrderService orderService = api.getOrderService();


        } catch (Exception e) {

            LOG.info("API failure.", e);

        }

    }

}
