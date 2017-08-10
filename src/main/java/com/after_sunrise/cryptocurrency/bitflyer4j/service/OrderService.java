package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface OrderService {

    CompletableFuture<OrderCreate.Response> sendOrder(OrderCreate request);

    CompletableFuture<OrderCancel.Response> cancelOrder(OrderCancel request);

    CompletableFuture<ParentCreate.Response> sendParent(ParentCreate request);

    CompletableFuture<ParentCancel.Response> cancelParent(ParentCancel request);

    CompletableFuture<List<OrderList.Response>> listOrders(OrderList request, Pagination pagination);

    CompletableFuture<List<ParentList.Response>> listParents(ParentList request, Pagination pagination);

    CompletableFuture<ParentDetail.Response> getParent(ParentDetail request);

    CompletableFuture<ProductCancel.Response> cancelProduct(ProductCancel request);

    CompletableFuture<List<TradeExecution.Response>> listExecutions(TradeExecution request, Pagination pagination);

    CompletableFuture<List<TradePosition.Response>> listPositions(TradePosition request, Pagination pagination);

    CompletableFuture<List<TradeCollateral.Response>> listCollaterals(TradeCollateral request, Pagination pagination);

    CompletableFuture<TradeCommission.Response> getCommission(TradeCommission request);

}
