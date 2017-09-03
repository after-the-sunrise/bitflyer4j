package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface OrderService {

    CompletableFuture<OrderCreate> sendOrder(OrderCreate.Request request);

    CompletableFuture<OrderCancel> cancelOrder(OrderCancel.Request request);

    CompletableFuture<ParentCreate> sendParent(ParentCreate.Request request);

    CompletableFuture<ParentCancel> cancelParent(ParentCancel.Request request);

    CompletableFuture<List<OrderList>> listOrders(OrderList.Request request);

    CompletableFuture<List<ParentList>> listParents(ParentList.Request request);

    CompletableFuture<ParentDetail> getParent(ParentDetail.Request request);

    CompletableFuture<ProductCancel> cancelProduct(ProductCancel.Request request);

    CompletableFuture<List<TradeExecution>> listExecutions(TradeExecution.Request request);

    CompletableFuture<List<TradePosition>> listPositions(TradePosition.Request request);

    CompletableFuture<List<TradeCollateral>> listCollaterals(TradeCollateral.Request request);

    CompletableFuture<TradeCommission> getCommission(TradeCommission.Request request);

}
