package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.DepositStatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.WithdrawalStatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.math.BigDecimal.TEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.time.ZonedDateTime.parse;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.4
 */
public class AccountServiceImplTest {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private AccountServiceImpl target;

    private TestModule module;

    private HttpClient client;

    private HttpResponse response;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        module.putMock(Gson.class, new GsonProvider().get());

        client = module.getMock(HttpClient.class);

        response = module.getMock(HttpResponse.class);

        target = new AccountServiceImpl(module.createInjector());

    }

    private CompletableFuture<HttpResponse> loadResponse(PathType type) throws IOException {

        when(response.getCode()).thenReturn(HTTP_OK);

        when(response.getMessage()).thenReturn("OK");

        URL url = Resources.getResource("json/" + type.name() + ".json");
        when(response.getBody()).thenReturn(new String(Resources.toByteArray(url)));

        return CompletableFuture.completedFuture(response);

    }

    @Test
    public void testGetPermissions() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<String> values = target.getPermissions().get().iterator();

        assertEquals(values.next(), "/v1/me/getpermissions");
        assertEquals(values.next(), "/v1/me/getbalance");
        assertEquals(values.next(), "/v1/me/getcollateral");
        assertEquals(values.next(), "/v1/me/getchildorders");
        assertEquals(values.next(), "/v1/me/getparentorders");
        assertEquals(values.next(), "/v1/me/getparentorder");
        assertEquals(values.next(), "/v1/me/getexecutions");
        assertEquals(values.next(), "/v1/me/getpositions");

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetBalances() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertFalse(MapUtils.isNotEmpty(request.getParameters()));

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Balance> values = target.getBalances().get().iterator();

        Balance value = values.next();
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getAmount(), new BigDecimal("1024078"));
        assertEquals(value.getAvailable(), new BigDecimal("508000"));

        value = values.next();
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getAmount(), new BigDecimal("10.24"));
        assertEquals(value.getAvailable(), new BigDecimal("4.12"));

        value = values.next();
        assertEquals(value.getCurrency(), "ETH");
        assertEquals(value.getAmount(), new BigDecimal("20.48"));
        assertEquals(value.getAvailable(), new BigDecimal("16.38"));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetCollateral() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertTrue(StringUtils.isBlank(request.getBody()));

            return loadResponse(request.getType());

        });

        Collateral value = target.getCollateral().get();

        assertEquals(value.getCollateral(), new BigDecimal("100000"));
        assertEquals(value.getOpenPositionPl(), new BigDecimal("-715"));
        assertEquals(value.getRequiredCollateral(), new BigDecimal("19857"));
        assertEquals(value.getKeepRate(), new BigDecimal("5.000"));

    }

    @Test
    public void testGetMargins() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            assertFalse(StringUtils.isNotEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Margin> values = target.getMargins().get().iterator();

        Margin value = values.next();
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getAmount(), new BigDecimal("10000"));

        value = values.next();
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getAmount(), new BigDecimal("1.23"));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetAddresses() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertFalse(MapUtils.isNotEmpty(request.getParameters()));

            assertFalse(StringUtils.isNotEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Address> values = target.getAddresses().get().iterator();

        Address value = values.next();
        assertEquals(value.getType(), "NORMAL");
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getAddress(), "3AYrDq8zhF82NJ2ZaLwBMPmaNziaKPaxa7");

        value = values.next();
        assertEquals(value.getType(), "NORMAL");
        assertEquals(value.getCurrency(), "ETH");
        assertEquals(value.getAddress(), "0x7fbB2CC24a3C0cd3789a44e9073381Ca6470853f");

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetCoinIns() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().size(), 2);
            assertEquals(request.getParameters().get("count"), "123");
            assertEquals(request.getParameters().get("before"), "456");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Pagination p = Pagination.builder().count(123L).before(456L).build();
        Iterator<CoinIn> values = target.getCoinIns(p).get().iterator();

        CoinIn value = values.next();
        assertEquals(value.getId(), (Long) 100L);
        assertEquals(value.getOrderId(), "CDP20151227-024141-055555");
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getAmount(), new BigDecimal("0.00002"));
        assertEquals(value.getAddress(), "1WriteySQufKZ2pVuM1oMhPrTtTVFq35j");
        assertEquals(value.getHash(), "9f92ee65a176bb9545f7becb8706c50d07d4cee5ffca34d8be3ef11d411405ae");
        assertEquals(value.getStatus(), "COMPLETED");
        assertEquals(value.getEventDate(), parse("2015-11-27T08:59:20.301", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetCoinOuts() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("after"), "123");

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        Pagination p = Pagination.builder().after(123L).build();
        Iterator<CoinOut> values = target.getCoinOuts(p).get().iterator();

        CoinOut value = values.next();
        assertEquals(value.getId(), (Long) 500L);
        assertEquals(value.getOrderId(), "CWD20151224-014040-077777");
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getAmount(), new BigDecimal("0.1234"));
        assertEquals(value.getAddress(), "1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa");
        assertEquals(value.getHash(), "724c07dfd4044abcb390b0412c3e707dd5c4f373f0a52b3bd295ce32b478c60a");
        assertEquals(value.getFee(), new BigDecimal("0.0005"));
        assertEquals(value.getAdditionalFee(), new BigDecimal("0.0001"));
        assertEquals(value.getStatus(), "COMPLETED");
        assertEquals(value.getEventDate(), parse("2015-12-24T01:40:40.397", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetBanks() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertFalse(MapUtils.isNotEmpty(request.getParameters()));

            assertTrue(StringUtils.isBlank(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Bank> values = target.getBanks().get().iterator();

        Bank value = values.next();
        assertEquals(value.getId(), (Long) 3402L);
        assertEquals(value.getVerified(), Boolean.TRUE);
        assertEquals(value.getName(), "三井住友銀行");
        assertEquals(value.getBranch(), "アオイ支店");
        assertEquals(value.getType(), "普通");
        assertEquals(value.getNumber(), "1111111");
        assertEquals(value.getAccount(), "ビットフライヤータロウ");

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetDeposits() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertNull(request.getParameters());

            assertTrue(StringUtils.isBlank(request.getBody()));

            return loadResponse(request.getType());

        });

        Iterator<Deposit> values = target.getDeposits(null).get().iterator();

        Deposit value = values.next();
        assertEquals(value.getId(), (Long) 300L);
        assertEquals(value.getOrderId(), "MDP20151014-101010-033333");
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getAmount(), new BigDecimal("10000"));
        assertEquals(value.getStatus(), DepositStatusType.COMPLETED);
        assertEquals(value.getEventDate(), parse("2015-10-14T10:10:10.001", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testWithdraw() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            Gson gson = module.getMock(Gson.class);

            Map<String, String> body = gson.fromJson(request.getBody(), new TypeToken<Map<String, String>>() {
            }.getType());
            assertEquals(body.get("currency_code"), "C");
            assertEquals(body.get("bank_account_id"), "1");
            assertEquals(body.get("amount"), "10");
            assertEquals(body.get("code"), "1234");
            assertEquals(body.size(), 4);

            return loadResponse(request.getType());

        });

        Withdraw w = Withdraw.builder().currency("C").bank(1L).amount(TEN).pin("1234").build();
        Withdraw.Response value = target.withdraw(w).get();

        assertEquals(value.getId(), "69476620-5056-4003-bcbe-42658a2b041b");
        assertEquals(value.getStatus(), (Integer) (-700));
        assertEquals(value.getMessage(), "This account has not yet been authenticated");
        assertNull(value.getData());

    }

    @Test
    public void testGetWithdrawals() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().size(), 1);
            assertEquals(request.getParameters().get("count"), "123");

            assertTrue(StringUtils.isBlank(request.getBody()));

            return loadResponse(request.getType());

        });

        Pagination p = Pagination.builder().count(123L).build();
        Iterator<Withdrawal> values = target.getWithdrawals(p).get().iterator();

        Withdrawal value = values.next();
        assertEquals(value.getId(), (Long) 700L);
        assertEquals(value.getOrderId(), "MWD20151020-090909-011111");
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getAmount(), new BigDecimal("12000"));
        assertEquals(value.getStatus(), WithdrawalStatusType.COMPLETED);
        assertEquals(value.getEventDate(), parse("2015-10-20T09:09:09.416", DTF));

        assertFalse(values.hasNext());

    }

}
