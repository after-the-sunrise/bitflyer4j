package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.TimeInForceType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
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

    private Bitflyer4j target;

    @BeforeMethod
    public void setUp() {
        target = new Bitflyer4jFactory().createInstance();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        target.close();
    }

    @Test(enabled = false)
    public void testMarket() throws Exception {

        MarketService marketService = target.getMarketService();

        LOG.info("Status : {}", marketService.getStatus().get());

        LOG.info("Markets : {}", marketService.getProducts().get());

        LOG.info("Board : {}", marketService.getBoard(null).get());

        LOG.info("Tick : {}", marketService.getTick(null).get());

        LOG.info("Execs : {}", marketService.getExecutions(null).get());

        LOG.info("Chats : {}", marketService.getChats(null).get());

    }

    @Test(enabled = false)
    public void testAccount() throws Exception {

        AccountService accountService = target.getAccountService();

        LOG.info("Perms : {}", accountService.getPermissions().get());

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

    @Test(enabled = false)
    public void testAccount_Withdraw() throws Exception {

        AccountService service = target.getAccountService();

        Withdraw.Request request = Withdraw.Request.builder().currency("JPY").bank(0L).amount(ONE).pin("000000").build();

        LOG.info("Withdraw : {}", service.withdraw(request).get());

    }

    @Test(enabled = false)
    public void testOrder() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Orders : {}", service.listOrders(null).get());

        LOG.info("Parents : {}", service.listParents(null).get());

        LOG.info("Executions : {}", service.listExecutions(null).get());

        LOG.info("Commission : {}", service.getCommission(
                TradeCommission.Request.builder().product("BTC_JPY").build()).get());

        LOG.info("Position : {}", service.listPositions(
                TradePosition.Request.builder().product("FX_BTC_JPY").build()).get());

        LOG.info("Collateral : {}", service.listCollaterals(
                TradeCollateral.Request.builder().build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateOrder() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Order Create : {}", service.sendOrder(OrderCreate.Request.builder()
                .product("BTC_JPY").type(ConditionType.LIMIT).side(SideType.BUY)
                .price(new BigDecimal("12345")).size(new BigDecimal("0.01")).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CancelOrder() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Order Cancel : {}", service.cancelOrder(OrderCancel.Request.builder()
                .product("BTC_JPY").orderId("JOR20150707-055555-022222").build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateParent() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Create : {}", service.sendParent(ParentCreate.Request.builder()
                .type(ParentType.IFDOCO).expiry(10000).timeInForce(TimeInForceType.GTC)
                .parameters(Arrays.asList(
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.BUY)
                                .price(new BigDecimal("30000")).size(new BigDecimal("0.1")).build(),
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.SELL)
                                .price(new BigDecimal("32000")).size(new BigDecimal("0.1")).build(),
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.STOP_LIMIT).side(SideType.SELL)
                                .price(new BigDecimal("28800")).triggerPrice(new BigDecimal("29000"))
                                .size(new BigDecimal("0.1")).build()
                )).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CancelParent() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Cancel : {}", service.cancelParent(ParentCancel.Request.builder()
                .product("BTC_JPY").orderId("JOR20150707-055555-022222").build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CancelProduct() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Cancel Product : {}", service.cancelProduct(
                ProductCancel.Request.builder().product("BTC_JPY").build()).get());

    }

    @Test(enabled = false)
    public void testRealtime() throws Exception {

        RealtimeService service = target.getRealtimeService();

        service.addListener(new RealtimeListener() {
            @Override
            public void onBoards(String p, Board value) {
                LOG.info("Realtime : ({}) {}", p, value);
            }

            @Override
            public void onTicks(String p, List<Tick> values) {
                LOG.info("Realtime : ({}) {}", p, values);
            }

            @Override
            public void onExecutions(String p, List<Execution> values) {
                LOG.info("Realtime : ({}) {}", p, values);
            }
        });

        String product = "BTC_JPY";

        LOG.info("Sub board : {}", service.subscribeBoard(Arrays.asList(product)).get());
        LOG.info("Sub exec : {}", service.subscribeExecution(Arrays.asList(product)).get());
        LOG.info("Sub tick : {}", service.subscribeTick(Arrays.asList(product)).get());

        Thread.sleep(SECONDS.toMillis(10));

        LOG.info("Unsub board : {}", service.unsubscribeBoard(Arrays.asList(product)).get());
        LOG.info("Unsub exec : {}", service.unsubscribeExecution(Arrays.asList(product)).get());
        LOG.info("Unsub tick : {}", service.unsubscribeTick(Arrays.asList(product)).get());

    }

}
