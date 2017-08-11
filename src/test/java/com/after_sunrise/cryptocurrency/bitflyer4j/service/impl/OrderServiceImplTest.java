package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URL;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
public class OrderServiceImplTest {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private static final Type MAP = new TypeToken<Map<String, String>>() {
    }.getType();

    private static final Type OBJ = new TypeToken<Map<String, Map<String, String>>>() {
    }.getType();

    private static class ParentTest {

        @SerializedName("id")
        String id;

        @SerializedName("parent_order_id")
        String parent_order_id;

        @SerializedName("parent_order_acceptance_id")
        String parent_order_acceptance_id;

        @SerializedName("order_method")
        String order_method;

        @SerializedName("minute_to_expire")
        String minute_to_expire;

        @SerializedName("time_in_force")
        String time_in_force;

        @SerializedName("parameters")
        List<Map<String, String>> parameters;

    }

    private OrderServiceImpl target;

    private TestModule module;

    private Gson gson;

    private HttpClient client;

    private HttpResponse response;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        module.putMock(Gson.class, new GsonProvider().get());

        gson = module.getMock(Gson.class);

        client = module.getMock(HttpClient.class);

        response = module.getMock(HttpResponse.class);

        target = new OrderServiceImpl(module.createInjector());

    }

    private CompletableFuture<HttpResponse> loadResponse(PathType type) throws IOException {

        URL url = Resources.getResource("json/" + type.name() + ".json");
        when(response.getBody()).thenReturn(new String(Resources.toByteArray(url)));

        when(response.getCode()).thenReturn(HTTP_OK);
        when(response.getMessage()).thenReturn("OK");

        return CompletableFuture.completedFuture(response);

    }

    @Test
    public void testSendOrder() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            Map<String, String> body = gson.fromJson(request.getBody(), MAP);
            assertEquals(body.get("product_code"), "BTC_JPY");
            assertEquals(body.get("child_order_type"), "LIMIT");
            assertEquals(body.get("side"), "BUY");
            assertEquals(body.get("price"), "30000");
            assertEquals(body.get("size"), "0.1");
            assertEquals(body.get("minute_to_expire"), "10000");
            assertEquals(body.get("time_in_force"), "GTC");
            assertEquals(body.size(), 7);

            return loadResponse(request.getType());

        });

        OrderCreate v = OrderCreate.builder().product("BTC_JPY").type(ConditionType.LIMIT) //
                .side(SideType.BUY).price(new BigDecimal("30000")) //
                .size(new BigDecimal("0.1")).expiry(10000) //
                .timeInForce(TimeInForceType.GTC).build();
        OrderCreate.Response value = target.sendOrder(v).get();

        assertEquals(value.getAcceptanceId(), "JRF20150707-050237-639234");

    }

    @Test
    public void testCancelOrder() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            Map<String, String> body = gson.fromJson(request.getBody(), MAP);
            assertEquals(body.get("product_code"), "BTC_JPY");
            assertEquals(body.get("child_order_id"), "JOR20150707-055555-022222");
            assertEquals(body.get("child_order_acceptance_id"), "JRF20150707-033333-099999");
            assertEquals(body.size(), 3);

            return loadResponse(request.getType());

        });

        OrderCancel v = OrderCancel.builder().product("BTC_JPY") //
                .orderId("JOR20150707-055555-022222") //
                .acceptanceId("JRF20150707-033333-099999")
                .build();
        OrderCancel.Response value = target.cancelOrder(v).get();

        assertNotNull(value);

    }

    @Test
    public void testSendParent() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            ParentTest body = gson.fromJson(request.getBody(), ParentTest.class);
            assertEquals(body.id, null);
            assertEquals(body.parent_order_id, null);
            assertEquals(body.parent_order_acceptance_id, null);
            assertEquals(body.order_method, "IFDOCO");
            assertEquals(body.minute_to_expire, "10000");
            assertEquals(body.time_in_force, "GTC");
            assertEquals(body.parameters.get(0).get("product_code"), "BTC_JPY");
            assertEquals(body.parameters.get(0).get("condition_type"), "LIMIT");
            assertEquals(body.parameters.get(0).get("side"), "BUY");
            assertEquals(body.parameters.get(0).get("price"), "30000");
            assertEquals(body.parameters.get(0).get("size"), "0.1");
            assertEquals(body.parameters.get(1).get("product_code"), "BTC_JPY");
            assertEquals(body.parameters.get(1).get("condition_type"), "LIMIT");
            assertEquals(body.parameters.get(1).get("side"), "SELL");
            assertEquals(body.parameters.get(1).get("price"), "32000");
            assertEquals(body.parameters.get(1).get("size"), "0.1");
            assertEquals(body.parameters.get(2).get("product_code"), "BTC_JPY");
            assertEquals(body.parameters.get(2).get("condition_type"), "STOP_LIMIT");
            assertEquals(body.parameters.get(2).get("side"), "SELL");
            assertEquals(body.parameters.get(2).get("price"), "28800");
            assertEquals(body.parameters.get(2).get("trigger_price"), "29000");
            assertEquals(body.parameters.get(2).get("size"), "0.1");

            return loadResponse(request.getType());

        });

        ParentCreate.Parameter p1 = ParentCreate.Parameter.builder().product("BTC_JPY") //
                .condition(ConditionType.LIMIT) //
                .side(SideType.BUY).price(new BigDecimal("30000")) //
                .size(new BigDecimal("0.1")).build();

        ParentCreate.Parameter p2 = ParentCreate.Parameter.builder().product("BTC_JPY") //
                .condition(ConditionType.LIMIT) //
                .side(SideType.SELL).price(new BigDecimal("32000")) //
                .size(new BigDecimal("0.1")).build();

        ParentCreate.Parameter p3 = ParentCreate.Parameter.builder().product("BTC_JPY") //
                .condition(ConditionType.STOP_LIMIT) //
                .side(SideType.SELL).price(new BigDecimal("28800")) //
                .triggerPrice(new BigDecimal("29000")) //
                .size(new BigDecimal("0.1")).build();

        ParentCreate v = ParentCreate.builder().type(ParentType.IFDOCO) //
                .expiry(10000).timeInForce(TimeInForceType.GTC) //
                .parameters(Arrays.asList(p1, p2, p3)).build();
        ParentCreate.Response value = target.sendParent(v).get();

        assertEquals(value.getAcceptanceId(), "JRF20150707-050237-639234");

    }

    @Test
    public void testCancelParent() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            Map<String, String> body = gson.fromJson(request.getBody(), MAP);
            assertEquals(body.get("product_code"), "BTC_JPY");
            assertEquals(body.get("parent_order_id"), "JCO20150925-055555-022222");
            assertEquals(body.get("parent_order_acceptance_id"), "JRF20150925-033333-099999");
            assertEquals(body.size(), 3);

            return loadResponse(request.getType());

        });

        ParentCancel v = ParentCancel.builder().product("BTC_JPY") //
                .orderId("JCO20150925-055555-022222") //
                .acceptanceId("JRF20150925-033333-099999")
                .build();
        ParentCancel.Response value = target.cancelParent(v).get();

        assertNotNull(value);

    }

    @Test
    public void testListOrders() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            Map<String, String> params = request.getParameters();
            assertEquals(params.get("product_code"), "P");
            assertEquals(params.get("child_order_state"), "ACTIVE");
            assertEquals(params.get("child_order_id"), "OI");
            assertEquals(params.get("child_order_acceptance_id"), "AI");
            assertEquals(params.get("parent_order_id"), "PI");
            assertEquals(params.get("count"), "123");
            assertEquals(params.size(), 6);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        OrderList v = OrderList.builder().product("P").state(StateType.ACTIVE) //
                .orderId("OI").acceptanceId("AI").parentId("PI").build();
        Pagination p = Pagination.builder().count(123L).build();
        Iterator<OrderList.Response> values = target.listOrders(v, p).get().iterator();

        OrderList.Response value = values.next();
        assertEquals(value.getId(), (Long) 138398L);
        assertEquals(value.getOrderId(), "JOR20150707-084555-022523");
        assertEquals(value.getProduct(), "BTC_JPY");
        assertEquals(value.getSide(), SideType.BUY);
        assertEquals(value.getCondition(), ConditionType.LIMIT);
        assertEquals(value.getPrice(), new BigDecimal("30000"));
        assertEquals(value.getAveragePrice(), new BigDecimal("30000"));
        assertEquals(value.getSize(), new BigDecimal("0.1"));
        assertEquals(value.getState(), StateType.COMPLETED);
        assertEquals(value.getExpireDate(), parse("2015-07-14T07:25:52", DTF));
        assertEquals(value.getOrderDate(), parse("2015-07-07T08:45:53", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-084552-031927");
        assertEquals(value.getOutstandingSize(), new BigDecimal("0"));
        assertEquals(value.getCancelSize(), new BigDecimal("0"));
        assertEquals(value.getExecutedSize(), new BigDecimal("0.1"));
        assertEquals(value.getTotalCommission(), new BigDecimal("0"));

        value = values.next();
        assertEquals(value.getId(), (Long) 138397L);
        assertEquals(value.getOrderId(), "JOR20150707-084549-022519");
        assertEquals(value.getProduct(), "BTC_JPY");
        assertEquals(value.getSide(), SideType.SELL);
        assertEquals(value.getCondition(), ConditionType.LIMIT);
        assertEquals(value.getPrice(), new BigDecimal("30000"));
        assertEquals(value.getAveragePrice(), new BigDecimal("0"));
        assertEquals(value.getSize(), new BigDecimal("0.1"));
        assertEquals(value.getState(), StateType.CANCELED);
        assertEquals(value.getExpireDate(), parse("2015-07-14T07:25:47", DTF));
        assertEquals(value.getOrderDate(), parse("2015-07-07T08:45:47", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-084547-396699");
        assertEquals(value.getOutstandingSize(), new BigDecimal("0"));
        assertEquals(value.getCancelSize(), new BigDecimal("0.1"));
        assertEquals(value.getExecutedSize(), new BigDecimal("0"));
        assertEquals(value.getTotalCommission(), new BigDecimal("0"));

        assertFalse(values.hasNext());

    }

    @Test
    public void testListParents() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            Map<String, String> params = request.getParameters();
            assertEquals(params.get("product_code"), "P");
            assertEquals(params.get("parent_order_state"), "ACTIVE");
            assertEquals(params.get("count"), "123");
            assertEquals(params.size(), 3);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        ParentList v = ParentList.builder().product("P").state(StateType.ACTIVE).build();
        Pagination p = Pagination.builder().count(123L).build();
        Iterator<ParentList.Response> values = target.listParents(v, p).get().iterator();

        ParentList.Response value = values.next();
        assertEquals(value.getId(), (Long) 138398L);
        assertEquals(value.getParentId(), "JCO20150707-084555-022523");
        assertEquals(value.getProduct(), "BTC_JPY");
        assertEquals(value.getSide(), SideType.BUY);
        assertEquals(value.getType(), ParentType.STOP);
        assertEquals(value.getPrice(), new BigDecimal("30000"));
        assertEquals(value.getAveragePrice(), new BigDecimal("30000"));
        assertEquals(value.getSize(), new BigDecimal("0.1"));
        assertEquals(value.getState(), StateType.COMPLETED);
        assertEquals(value.getExpireDate(), parse("2015-07-14T07:25:52", DTF));
        assertEquals(value.getParentDate(), parse("2015-07-07T08:45:53", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-084552-031927");
        assertEquals(value.getOutstandingSize(), new BigDecimal("0"));
        assertEquals(value.getCancelSize(), new BigDecimal("0"));
        assertEquals(value.getExecutedSize(), new BigDecimal("0.1"));
        assertEquals(value.getTotalCommission(), new BigDecimal("0"));

        value = values.next();
        assertEquals(value.getId(), (Long) 138397L);
        assertEquals(value.getParentId(), "JCO20150707-084549-022519");
        assertEquals(value.getProduct(), "BTC_JPY");
        assertEquals(value.getSide(), SideType.SELL);
        assertEquals(value.getType(), ParentType.IFD);
        assertEquals(value.getPrice(), new BigDecimal("30000"));
        assertEquals(value.getAveragePrice(), new BigDecimal("0"));
        assertEquals(value.getSize(), new BigDecimal("0.1"));
        assertEquals(value.getState(), StateType.CANCELED);
        assertEquals(value.getExpireDate(), parse("2015-07-14T07:25:47", DTF));
        assertEquals(value.getParentDate(), parse("2015-07-07T08:45:47", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-084547-396699");
        assertEquals(value.getOutstandingSize(), new BigDecimal("0"));
        assertEquals(value.getCancelSize(), new BigDecimal("0.1"));
        assertEquals(value.getExecutedSize(), new BigDecimal("0"));
        assertEquals(value.getTotalCommission(), new BigDecimal("0"));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetParent() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().get("parent_order_id"), "PI");
            assertEquals(request.getParameters().get("parent_order_acceptance_id"), "AI");
            assertEquals(request.getParameters().size(), 2);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        ParentDetail v = ParentDetail.builder().parentId("PI").acceptanceId("AI").build();
        ParentDetail.Response value = target.getParent(v).get();

        assertEquals(value.getId(), (Long) 4242L);
        assertEquals(value.getParentId(), "JCO20150925-046876-036161");
        assertEquals(value.getAcceptanceId(), "JRF20150925-060559-396699");
        assertEquals(value.getType(), ParentType.IFDOCO);
        assertEquals(value.getExpiry(), (Integer) 10000);
        assertEquals(value.getParameters().get(0).getProduct(), "BTC_JPY");
        assertEquals(value.getParameters().get(0).getCondition(), ConditionType.LIMIT);
        assertEquals(value.getParameters().get(0).getSide(), SideType.BUY);
        assertEquals(value.getParameters().get(0).getPrice(), new BigDecimal("30000"));
        assertEquals(value.getParameters().get(0).getSize(), new BigDecimal("0.1"));
        assertEquals(value.getParameters().get(0).getTriggerPrice(), new BigDecimal("0"));
        assertEquals(value.getParameters().get(0).getOffset(), new BigDecimal("0"));
        assertEquals(value.getParameters().get(1).getProduct(), "BTC_JPY");
        assertEquals(value.getParameters().get(1).getCondition(), ConditionType.LIMIT);
        assertEquals(value.getParameters().get(1).getSide(), SideType.SELL);
        assertEquals(value.getParameters().get(1).getPrice(), new BigDecimal("32000"));
        assertEquals(value.getParameters().get(1).getSize(), new BigDecimal("0.1"));
        assertEquals(value.getParameters().get(1).getTriggerPrice(), new BigDecimal("0"));
        assertEquals(value.getParameters().get(1).getOffset(), new BigDecimal("0"));
        assertEquals(value.getParameters().get(2).getProduct(), "BTC_JPY");
        assertEquals(value.getParameters().get(2).getCondition(), ConditionType.STOP_LIMIT);
        assertEquals(value.getParameters().get(2).getSide(), SideType.SELL);
        assertEquals(value.getParameters().get(2).getPrice(), new BigDecimal("28800"));
        assertEquals(value.getParameters().get(2).getSize(), new BigDecimal("0.1"));
        assertEquals(value.getParameters().get(2).getTriggerPrice(), new BigDecimal("29000"));
        assertEquals(value.getParameters().get(2).getOffset(), new BigDecimal("0"));
        assertEquals(value.getParameters().size(), 3);

    }

    @Test
    public void testCancelProduct() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertTrue(MapUtils.isEmpty(request.getParameters()));

            Map<String, String> body = gson.fromJson(request.getBody(), MAP);
            assertEquals(body.get("product_code"), "BTC_JPY");
            assertEquals(body.size(), 1);

            return loadResponse(request.getType());

        });

        ProductCancel v = ProductCancel.builder().product("BTC_JPY").build();

        ProductCancel.Response value = target.cancelProduct(v).get();

        assertNotNull(value);

    }

    @Test
    public void testListExecutions() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().get("product_code"), "BTC_JPY");
            assertEquals(request.getParameters().get("child_order_id"), "OI");
            assertEquals(request.getParameters().get("child_order_acceptance_id"), "AI");
            assertEquals(request.getParameters().get("count"), "123");
            assertEquals(request.getParameters().size(), 4);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        TradeExecution v = TradeExecution.builder().product("BTC_JPY") //
                .child_order_id("OI").child_order_acceptance_id("AI").build();
        Pagination p = Pagination.builder().count(123L).build();
        Iterator<TradeExecution.Response> values = target.listExecutions(v, p).get().iterator();

        TradeExecution.Response value = values.next();
        assertEquals(value.getId(), (Long) 37233L);
        assertEquals(value.getOrderId(), "JOR20150707-060559-021935");
        assertEquals(value.getSide(), SideType.BUY);
        assertEquals(value.getPrice(), new BigDecimal("33470"));
        assertEquals(value.getSize(), new BigDecimal("0.01"));
        assertEquals(value.getCommission(), new BigDecimal("0"));
        assertEquals(value.getExecDate(), parse("2015-07-07T09:57:40.397", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-060559-396699");

        value = values.next();
        assertEquals(value.getId(), (Long) 37232L);
        assertEquals(value.getOrderId(), "JOR20150707-060426-021925");
        assertEquals(value.getSide(), SideType.BUY);
        assertEquals(value.getPrice(), new BigDecimal("33470"));
        assertEquals(value.getSize(), new BigDecimal("0.01"));
        assertEquals(value.getCommission(), new BigDecimal("0"));
        assertEquals(value.getExecDate(), parse("2015-07-07T09:57:40.397", DTF));
        assertEquals(value.getAcceptanceId(), "JRF20150707-060559-396699");

        assertFalse(values.hasNext());

    }

    @Test
    public void testListPositions() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().get("product_code"), "FX_BTC_JPY");
            assertEquals(request.getParameters().size(), 1);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        TradePosition v = TradePosition.builder().product("FX_BTC_JPY").build();
        Iterator<TradePosition.Response> values = target.listPositions(v).get().iterator();

        TradePosition.Response value = values.next();
        assertEquals(value.getProduct(), "FX_BTC_JPY");
        assertEquals(value.getSide(), SideType.BUY);
        assertEquals(value.getPrice(), new BigDecimal("36000"));
        assertEquals(value.getSize(), new BigDecimal("10"));
        assertEquals(value.getCommission(), new BigDecimal("0"));
        assertEquals(value.getSwapPoint(), new BigDecimal("-35"));
        assertEquals(value.getRequiredCollateral(), new BigDecimal("120000"));
        assertEquals(value.getOpenDate(), parse("2015-11-03T10:04:45.011", DTF));
        assertEquals(value.getLeverage(), new BigDecimal("3"));
        assertEquals(value.getProfitAndLoss(), new BigDecimal("965"));

        assertFalse(values.hasNext());

    }

    @Test
    public void testListCollaterals() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().get("count"), "123");
            assertEquals(request.getParameters().size(), 1);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        TradeCollateral v = TradeCollateral.builder().build();
        Pagination p = Pagination.builder().count(123L).build();
        Iterator<TradeCollateral.Response> values = target.listCollaterals(v, p).get().iterator();

        TradeCollateral.Response value = values.next();
        assertEquals(value.getId(), (Long) 4995L);
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getChange(), new BigDecimal("-6"));
        assertEquals(value.getAmount(), new BigDecimal("-6"));
        assertEquals(value.getReasonCode(), "CLEARING_COLL");
        assertEquals(value.getDate(), parse("2017-05-18T02:37:41.327", DTF));

        value = values.next();
        assertEquals(value.getId(), (Long) 4994L);
        assertEquals(value.getCurrency(), "JPY");
        assertEquals(value.getChange(), new BigDecimal("2083698"));
        assertEquals(value.getAmount(), new BigDecimal("0"));
        assertEquals(value.getReasonCode(), "EXCHANGE_COLL");
        assertEquals(value.getDate(), parse("2017-04-28T03:05:07.493", DTF));

        value = values.next();
        assertEquals(value.getId(), (Long) 4993L);
        assertEquals(value.getCurrency(), "BTC");
        assertEquals(value.getChange(), new BigDecimal("-14.69001618"));
        assertEquals(value.getAmount(), new BigDecimal("34.97163164"));
        assertEquals(value.getReasonCode(), "EXCHANGE_COLL");
        assertEquals(value.getDate(), parse("2017-04-28T03:05:07.493", DTF));

        assertFalse(values.hasNext());

    }

    @Test
    public void testGetCommission() throws Exception {

        when(client.request(any(HttpClient.HttpRequest.class))).thenAnswer(invocation -> {

            HttpClient.HttpRequest request = invocation.getArgumentAt(0, HttpClient.HttpRequest.class);

            assertEquals(request.getParameters().get("product_code"), "ETH_JPY");
            assertEquals(request.getParameters().size(), 1);

            assertTrue(StringUtils.isEmpty(request.getBody()));

            return loadResponse(request.getType());

        });

        TradeCommission v = TradeCommission.builder().product("ETH_JPY").build();
        TradeCommission.Response value = target.getCommission(v).get();

        assertEquals(value.getRate(), new BigDecimal("0.001"));

    }

}