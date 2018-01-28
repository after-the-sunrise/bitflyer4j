package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Miscellaneous code snippets demonstrating the API usage.
 *
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

        LOG.info("Markets : {}", marketService.getProducts().get());

        LOG.info("Markets USA : {}", marketService.getProductsUsa().get());

        LOG.info("Markets EU : {}", marketService.getProductsEu().get());

        LOG.info("Status : {}", marketService.getStatus(Status.Request.builder().build()).get());

        LOG.info("Board Status : {}", marketService.getBoardStatus(
                BoardStatus.Request.builder().product("BTC_JPY").build()).get());

        LOG.info("Board : {}", marketService.getBoard(
                Board.Request.builder().product("BTC_JPY").build()).get());

        LOG.info("Tick : {}", marketService.getTick(
                Tick.Request.builder().product("BTC_JPY").build()).get());

        LOG.info("Execs : {}", marketService.getExecutions(
                Execution.Request.builder().product("BTC_JPY").count(10).build()).get());

        LOG.info("Chats : {}", marketService.getChats(
                Chat.Request.builder().fromDate(LocalDate.of(2017, 4, 14)).build()).get());

        LOG.info("Chats USA : {}", marketService.getChatsUsa(
                Chat.Request.builder().fromDate(LocalDate.of(2017, 4, 14)).build()).get());

        LOG.info("Chats EU : {}", marketService.getChatsEu(
                Chat.Request.builder().fromDate(LocalDate.of(2017, 4, 14)).build()).get());

    }

    @Test(enabled = false)
    public void testAccount() throws Exception {

        AccountService accountService = target.getAccountService();

        LOG.info("Perms : {}", accountService.getPermissions().get());

        LOG.info("Balances : {}", accountService.getBalances().get());

        LOG.info("Collateral : {}", accountService.getCollateral().get());

        LOG.info("Margins : {}", accountService.getMargins().get());

        LOG.info("Addresses : {}", accountService.getAddresses().get());

        LOG.info("CoinIns : {}", accountService.getCoinIns(
                CoinIn.Request.builder().count(5).build()).get());

        LOG.info("CoinOuts : {}", accountService.getCoinOuts(
                CoinOut.Request.builder().count(5).build()).get());

        LOG.info("Banks : {}", accountService.getBanks().get());

        LOG.info("Deposits : {}", accountService.getDeposits(
                Deposit.Request.builder().count(5).build()).get());

        LOG.info("Withdrawals : {}", accountService.getWithdrawals(
                Withdrawal.Request.builder().count(5).build()).get());

    }

    @Test(enabled = false)
    public void testAccount_Withdraw() throws Exception {

        AccountService service = target.getAccountService();

        LOG.info("Withdraw : {}", service.withdraw(Withdraw.Request.builder()
                .currency("JPY").bank(0L).amount(ONE).pin("000000").build()).get());

    }

    @Test(enabled = false)
    public void testOrder() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Orders : {}", service.listOrders(OrderList.Request.builder()
                .product("BTC_JPY").state(StateType.ACTIVE).build()).get());

        LOG.info("Parents : {}", service.listParents(ParentList.Request.builder()
                .product("BTC_JPY").state(StateType.ACTIVE).build()).get());

        LOG.info("Executions : {}", service.listExecutions(TradeExecution.Request.builder()
                .product("BTC_JPY").childOrderId("JOR20150707-055555-022222").build()).get());

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
                .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CancelOrder() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Order Cancel : {}", service.cancelOrder(OrderCancel.Request.builder()
                .product("BTC_JPY").orderId("JOR20150707-055555-022222").build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateParent_STOP() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Create : {}", service.sendParent(ParentCreate.Request.builder()
                .type(ParentType.SIMPLE).expiry(10000).timeInForce(TimeInForceType.GTC)
                .parameters(Collections.singletonList(ParentCreate.Request.Parameter.builder()
                        .product("BTC_JPY").condition(ConditionType.STOP)
                        .side(SideType.SELL).triggerPrice(new BigDecimal("1000000"))
                        .size(new BigDecimal("0.001")).build()
                )).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateParent_STOP_LIMIT() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Create : {}", service.sendParent(ParentCreate.Request.builder()
                .type(ParentType.SIMPLE).expiry(10000).timeInForce(TimeInForceType.GTC)
                .parameters(Collections.singletonList(ParentCreate.Request.Parameter.builder()
                        .product("BTC_JPY").condition(ConditionType.STOP_LIMIT)
                        .side(SideType.SELL).triggerPrice(new BigDecimal("1000000"))
                        .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build()
                )).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateParent_IFD() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Create : {}", service.sendParent(ParentCreate.Request.builder()
                .type(ParentType.IFD).expiry(10000).timeInForce(TimeInForceType.GTC)
                .parameters(Arrays.asList(
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.BUY)
                                .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build(),
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.SELL)
                                .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build()
                )).build()).get());

    }

    @Test(enabled = false)
    public void testOrder_CreateParent_IFDOCO() throws Exception {

        OrderService service = target.getOrderService();

        LOG.info("Parent Create : {}", service.sendParent(ParentCreate.Request.builder()
                .type(ParentType.IFDOCO).expiry(10000).timeInForce(TimeInForceType.GTC)
                .parameters(Arrays.asList(
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.BUY)
                                .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build(),
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.LIMIT).side(SideType.SELL)
                                .price(new BigDecimal("1000000")).size(new BigDecimal("0.001")).build(),
                        ParentCreate.Request.Parameter.builder()
                                .product("BTC_JPY").condition(ConditionType.STOP_LIMIT).side(SideType.SELL)
                                .price(new BigDecimal("1000000")).triggerPrice(new BigDecimal("1000000"))
                                .size(new BigDecimal("0.001")).build()
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
            public void onBoardsSnapshot(String p, Board value) {
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
        LOG.info("Sub boards : {}", service.subscribeBoardSnapshot(Arrays.asList(product)).get());
        LOG.info("Sub exec : {}", service.subscribeExecution(Arrays.asList(product)).get());
        LOG.info("Sub tick : {}", service.subscribeTick(Arrays.asList(product)).get());

        Thread.sleep(SECONDS.toMillis(10));

        LOG.info("Unsub board : {}", service.unsubscribeBoard(Arrays.asList(product)).get());
        LOG.info("Unsub boards : {}", service.unsubscribeBoardSnapshot(Arrays.asList(product)).get());
        LOG.info("Unsub exec : {}", service.unsubscribeExecution(Arrays.asList(product)).get());
        LOG.info("Unsub tick : {}", service.unsubscribeTick(Arrays.asList(product)).get());

    }

}
