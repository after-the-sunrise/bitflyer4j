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
    public CompletableFuture<OrderCreate.Response> sendOrder(OrderCreate request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_SEND).body(body).build();

        return request(req, OrderCreateResponse.class);

    }

    @Override
    public CompletableFuture<OrderCancel.Response> cancelOrder(OrderCancel request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_CANCEL).body(body).build();

        return request(req, OrderCancelResponse.class);

    }

    @Override
    public CompletableFuture<ParentCreate.Response> sendParent(ParentCreate request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_SEND).body(body).build();

        return request(req, ParentCreateResponse.class);

    }

    @Override
    public CompletableFuture<ParentCancel.Response> cancelParent(ParentCancel request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_CANCEL).body(body).build();

        return request(req, ParentCancelResponse.class);

    }

    @Override
    public CompletableFuture<List<OrderList.Response>> listOrders(OrderList request, Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        params = prepareParameter(params, request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_LIST).parameters(params).build();

        return request(req, TYPE_ORDERS);

    }

    @Override
    public CompletableFuture<List<ParentList.Response>> listParents(ParentList request, Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        params = prepareParameter(params, request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_LIST).parameters(params).build();

        return request(req, TYPE_PARENTS);

    }

    @Override
    public CompletableFuture<ParentDetail.Response> getParent(ParentDetail request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PARENT_DETAIL).parameters(params).build();

        return request(req, ParentDetailResponse.class);

    }

    @Override
    public CompletableFuture<ProductCancel.Response> cancelProduct(ProductCancel request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.PRODUCT_CANCEL).body(body).build();

        return request(req, ProductCancelResponse.class);

    }

    @Override
    public CompletableFuture<List<TradeExecution.Response>> listExecutions(TradeExecution request, Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        params = prepareParameter(params, request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_EXECUTION).parameters(params).build();

        return request(req, TYPE_EXECS);

    }

    @Override
    public CompletableFuture<List<TradePosition.Response>> listPositions(TradePosition request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_POSITION).parameters(params).build();

        return request(req, TYPE_POSITIONS);

    }

    @Override
    public CompletableFuture<List<TradeCollateral.Response>> listCollaterals(TradeCollateral request, Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        params = prepareParameter(params, request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_COLLATERAL).parameters(params).build();

        return request(req, TYPE_COLLATERALS);

    }

    @Override
    public CompletableFuture<TradeCommission.Response> getCommission(TradeCommission request) {

        Map<String, String> params = prepareParameter(request);

        HttpRequest req = HttpRequest.builder().type(PathType.TRADE_COMMISSION).parameters(params).build();

        return request(req, TradeCommissionResponse.class);

    }

}
