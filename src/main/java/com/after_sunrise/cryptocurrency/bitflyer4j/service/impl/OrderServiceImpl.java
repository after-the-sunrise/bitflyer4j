package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class OrderServiceImpl extends HttpService implements OrderService {

    private static final Type TYPE_ORDERS = new TypeToken<List<OrderListResponse>>() {
    }.getType();

    private static final Type TYPE_PARENTS = new TypeToken<List<ParentListResponse>>() {
    }.getType();

    private static final Type TYPE_EXECS = new TypeToken<List<TradeExecutionResponse>>() {
    }.getType();

    private static final Type TYPE_POSITIONS = new TypeToken<List<TradePositionResponse>>() {
    }.getType();

    private static final Type TYPE_COLLATERALS = new TypeToken<List<TradeCollateralResponse>>() {
    }.getType();

    @Inject
    public OrderServiceImpl(Injector injector) {
        super(injector);
    }

    @Override
    public CompletableFuture<OrderCreate> sendOrder(OrderCreate.Request request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_SEND).body(body).build();

        return request(req, OrderCreateResponse.class);

    }

    @Override
    public CompletableFuture<OrderCancel> cancelOrder(OrderCancel.Request request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_CANCEL).body(body).build();

        return request(req, OrderCancelResponse.class);

    }

    @Override
    public CompletableFuture<ParentCreate> sendParent(ParentCreate.Request request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_SEND).body(body).build();

        return request(req, ParentCreateResponse.class);

    }

    @Override
    public CompletableFuture<ParentCancel> cancelParent(ParentCancel.Request request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_CANCEL).body(body).build();

        return request(req, ParentCancelResponse.class);

    }

    @Override
    public CompletableFuture<List<OrderList>> listOrders(OrderList.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_LIST).parameters(params).build();

        return request(req, TYPE_ORDERS);

    }

    @Override
    public CompletableFuture<List<ParentList>> listParents(ParentList.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_LIST).parameters(params).build();

        return request(req, TYPE_PARENTS);

    }

    @Override
    public CompletableFuture<ParentDetail> getParent(ParentDetail.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_DETAIL).parameters(params).build();

        return request(req, ParentDetailResponse.class);

    }

    @Override
    public CompletableFuture<ProductCancel> cancelProduct(ProductCancel.Request request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PRODUCT_CANCEL).body(body).build();

        return request(req, ProductCancelResponse.class);

    }

    @Override
    public CompletableFuture<List<TradeExecution>> listExecutions(TradeExecution.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_EXECUTION).parameters(params).build();

        return request(req, TYPE_EXECS);

    }

    @Override
    public CompletableFuture<List<TradePosition>> listPositions(TradePosition.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_POSITION).parameters(params).build();

        return request(req, TYPE_POSITIONS);

    }

    @Override
    public CompletableFuture<List<TradeCollateral>> listCollaterals(TradeCollateral.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_COLLATERAL).parameters(params).build();

        return request(req, TYPE_COLLATERALS);

    }

    @Override
    public CompletableFuture<TradeCommission> getCommission(TradeCommission.Request request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_COMMISSION).parameters(params).build();

        return request(req, TradeCommissionResponse.class);

    }

}
