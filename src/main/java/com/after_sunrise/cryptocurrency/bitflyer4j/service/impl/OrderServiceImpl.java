package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.OrderList;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.OrderListResponse;
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
public class OrderServiceImpl extends BaseService implements OrderService {

    private static final Type TYPE_ORDERS = new TypeToken<List<OrderListResponse>>() {
    }.getType();

    @Inject
    public OrderServiceImpl(Injector injector) {
        super(injector);
    }

    @Override
    public CompletableFuture<List<OrderList.Response>> listOrders(OrderList request, Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        params = prepareParameter(params, request);

        HttpRequest req = HttpRequest.builder().type(PathType.ORDER_LIST).parameters(params).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_ORDERS));

    }

}
