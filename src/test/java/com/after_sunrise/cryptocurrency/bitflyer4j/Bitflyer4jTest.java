package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdraw;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.util.concurrent.TimeUnit.SECONDS;

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

                Withdraw request = Withdraw.builder().currency("JPY").bank(0L).amount(ONE).pin("000000").build();

                LOG.info("Withdraw : {}", accountService.withdraw(request).get());

            }

            OrderService orderService = api.getOrderService();

            LOG.info("Orders : {}", orderService.listOrders(null, null).get());

            RealtimeService realtimeService = api.getRealtimeService();

            realtimeService.addListener(new RealtimeListener() {
                @Override
                public void onBoards(List<Board> values) {
                    LOG.info("Realtime : {}", values);
                }

                @Override
                public void onTicks(List<Tick> values) {
                    LOG.info("Realtime : {}", values);
                }

                @Override
                public void onExecutions(List<Execution> values) {
                    LOG.info("Realtime : {}", values);
                }
            });

            realtimeService.subscribeBoard(Arrays.asList("BTC_JPY"));

            Thread.sleep(SECONDS.toMillis(10L));

        } catch (Exception e) {

            LOG.info("API failure.", e);

        }

    }

}
