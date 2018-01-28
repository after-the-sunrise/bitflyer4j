package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Application;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDate.of;
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.4
 */
public class MarketServiceImplTest extends Application {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private MarketServiceImpl target;

    private TestModule module;

    private HttpClient client;

    private HttpResponse response;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        module.putMock(Gson.class, new GsonProvider().get());

        client = module.getMock(HttpClient.class);

        response = module.getMock(HttpResponse.class);

        target = new MarketServiceImpl(module.createInjector());

    }

    private CompletableFuture<HttpResponse> loadResponse(PathType type) throws IOException {

        when(response.getCode()).thenReturn(HttpURLConnection.HTTP_OK);

        when(response.getMessage()).thenReturn("OK");

        URL url = Resources.getResource("json/" + type.name() + ".json");
        when(response.getBody()).thenReturn(new String(Resources.toByteArray(url)));

        return CompletableFuture.completedFuture(response);

    }

    @Test
    public void testGetProducts() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Product> values = target.getProducts().get().iterator();

        Product p = values.next();
        assertEquals(p.getProduct(), "BTC_JPY");
        assertEquals(p.getAlias(), null);

        p = values.next();
        assertEquals(p.getProduct(), "FX_BTC_JPY");
        assertEquals(p.getAlias(), null);

        p = values.next();
        assertEquals(p.getProduct(), "ETH_BTC");
        assertEquals(p.getAlias(), null);

        p = values.next();
        assertEquals(p.getProduct(), "BTCJPY28APR2017");
        assertEquals(p.getAlias(), "BTCJPY_MAT1WK");

        p = values.next();
        assertEquals(p.getProduct(), "BTCJPY05MAY2017");
        assertEquals(p.getAlias(), "BTCJPY_MAT2WK");

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetProductsUsa() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Product> values = target.getProductsUsa().get().iterator();

        Product p = values.next();
        assertEquals(p.getProduct(), "BTC_USD");
        assertEquals(p.getAlias(), null);

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetProductsEu() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Product> values = target.getProductsEu().get().iterator();

        Product p = values.next();
        assertEquals(p.getProduct(), "BTC_EUR");
        assertEquals(p.getAlias(), null);

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetBoard() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("product_code"), "BTC_JPY");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Board value = target.getBoard(Board.Request.builder().product("BTC_JPY").build()).get();

        assertEquals(value.getMid(), new BigDecimal("33320"));

        List<Board.Quote> bids = value.getBid();
        assertEquals(bids.size(), 2);
        assertEquals(bids.get(0).getPrice(), new BigDecimal("30000"));
        assertEquals(bids.get(0).getSize(), new BigDecimal("0.1"));
        assertEquals(bids.get(1).getPrice(), new BigDecimal("25570"));
        assertEquals(bids.get(1).getSize(), new BigDecimal("3"));

        List<Board.Quote> asks = value.getAsk();
        assertEquals(asks.size(), 2);
        assertEquals(asks.get(0).getPrice(), new BigDecimal("36640"));
        assertEquals(asks.get(0).getSize(), new BigDecimal("5"));
        assertEquals(asks.get(1).getPrice(), new BigDecimal("36700"));
        assertEquals(asks.get(1).getSize(), new BigDecimal("1.2"));

    }

    @Test
    public void testGetTick() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("product_code"), "ETH_BTC");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Tick value = target.getTick(Tick.Request.builder().product("ETH_BTC").build()).get();

        assertEquals(value.getProduct(), "BTC_JPY");
        assertEquals(value.getTimestamp(), parse("2015-07-08T02:50:59.97", DTF));
        assertEquals(value.getId(), (Long) 3579L);
        assertEquals(value.getBestBidPrice(), new BigDecimal("30000"));
        assertEquals(value.getBestAskPrice(), new BigDecimal("36640"));
        assertEquals(value.getBestBidSize(), new BigDecimal("0.1"));
        assertEquals(value.getBestAskSize(), new BigDecimal("5"));
        assertEquals(value.getTotalBidDepth(), new BigDecimal("15.13"));
        assertEquals(value.getTotalAskDepth(), new BigDecimal("20"));
        assertEquals(value.getTradePrice(), new BigDecimal("31690"));
        assertEquals(value.getTradeVolume(), new BigDecimal("16819.26"));
        assertEquals(value.getProductVolume(), new BigDecimal("6819.26"));

    }

    @Test
    public void testGetExecutions() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 3);
            assertEquals(request.getParameters().get("product_code"), "FX_BTC_JPY");
            assertEquals(request.getParameters().get("count"), "123");
            assertEquals(request.getParameters().get("before"), "456");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Execution.Request p = Execution.Request.builder().count(123).before(456L).product("FX_BTC_JPY").build();
        Iterator<Execution> values = target.getExecutions(p).get().iterator();

        Execution exec = values.next();
        assertEquals(exec.getId(), (Long) 39287L);
        assertEquals(exec.getSide(), SideType.BUY);
        assertEquals(exec.getPrice(), new BigDecimal("31690"));
        assertEquals(exec.getSize(), new BigDecimal("27.04"));
        assertEquals(exec.getTimestamp(), parse("2015-07-08T02:43:34.823", DTF));
        assertEquals(exec.getBuyOrderId(), "JRF20150707-200203-452209");
        assertEquals(exec.getSellOrderId(), "JRF20150708-024334-060234");

        exec = values.next();
        assertEquals(exec.getId(), (Long) 39286L);
        assertEquals(exec.getSide(), SideType.SELL);
        assertEquals(exec.getPrice(), new BigDecimal("33170"));
        assertEquals(exec.getSize(), new BigDecimal("0.36"));
        assertEquals(exec.getTimestamp(), parse("2015-07-08T02:43:34.72", DTF));
        assertEquals(exec.getBuyOrderId(), "JRF20150708-010230-400876");
        assertEquals(exec.getSellOrderId(), "JRF20150708-024334-197755");

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetStatus() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            assertEquals(request.getParameters().get("product_code"), "BTC_JPY");

            return loadResponse(request.getType());

        });

        Status value = target.getStatus(Status.Request.builder().product("BTC_JPY").build()).get();

        assertEquals(value.getStatus(), StatusType.NORMAL);

    }

    @Test
    public void testGetBoardStatus() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            assertEquals(request.getParameters().get("product_code"), "BTCJPY01DEC2017");

            return loadResponse(request.getType());

        });

        BoardStatus value = target.getBoardStatus(BoardStatus.Request.builder().product("BTCJPY01DEC2017").build()).get();

        assertEquals(value.getHealth(), StatusType.NORMAL);

        assertEquals(value.getState(), BoardStatusType.MATURED);

        assertEquals(value.getData().get("special_quotation"), "410897");

    }

    @Test
    public void testGetChats() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("from_date"), "2016-02-16");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Chat.Request request = Chat.Request.builder().fromDate(of(2016, 2, 16)).build();
        Iterator<Chat> values = target.getChats(request).get().iterator();

        Chat chat = values.next();
        assertEquals(chat.getName(), "User1234567");
        assertEquals(chat.getMessage(), "Hello world!");
        assertEquals(chat.getTimestamp(), parse("2016-02-16T10:58:08.833", DTF));

        chat = values.next();
        assertEquals(chat.getName(), "ビット太郎");
        assertEquals(chat.getMessage(), "サンプルメッセージ");
        assertEquals(chat.getTimestamp(), parse("2016-02-15T10:18:06.67", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetChats_Usa() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("from_date"), "2017-11-27");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Chat.Request request = Chat.Request.builder().fromDate(of(2017, 11, 27)).build();
        Iterator<Chat> values = target.getChatsUsa(request).get().iterator();

        Chat chat = values.next();
        assertEquals(chat.getName(), "User49ADEAB");
        assertEquals(chat.getMessage(), "hi");
        assertEquals(chat.getTimestamp(), parse("2017-11-27T21:13:20.457", DTF));

        chat = values.next();
        assertEquals(chat.getName(), "77");
        assertEquals(chat.getMessage(), "hello");
        assertEquals(chat.getTimestamp(), parse("2017-11-27T23:43:59.68", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetChats_Eu() throws Exception {

        when(client.request(any(HttpRequest.class))).thenAnswer(invocation -> {

            HttpRequest request = invocation.getArgumentAt(0, HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("from_date"), "2017-11-27");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Chat.Request request = Chat.Request.builder().fromDate(of(2017, 11, 27)).build();
        Iterator<Chat> values = target.getChatsEu(request).get().iterator();

        Chat chat = values.next();
        assertEquals(chat.getName(), "User22458BD");
        assertEquals(chat.getMessage(), "it just opened in Europe");
        assertEquals(chat.getTimestamp(), parse("2018-01-23T08:21:25.647", DTF));

        chat = values.next();
        assertEquals(chat.getName(), "User5829740");
        assertEquals(chat.getMessage(), "Hehe no activity at all :P");
        assertEquals(chat.getTimestamp(), parse("2018-01-23T08:35:00.503", DTF));

        assertFalse(values.hasNext());

    }

}
