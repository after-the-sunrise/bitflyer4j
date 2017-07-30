package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
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
public class AccountServiceImpl extends BaseService implements AccountService {

    private static final Type TYPE_STRINGS = new TypeToken<List<String>>() {
    }.getType();

    private static final Type TYPE_BALANCES = new TypeToken<List<BalanceImpl>>() {
    }.getType();

    private static final Type TYPE_MARGINS = new TypeToken<List<MarginImpl>>() {
    }.getType();

    private static final Type TYPE_ADDRESSES = new TypeToken<List<AddressImpl>>() {
    }.getType();

    private static final Type TYPE_COIN_INS = new TypeToken<List<CoinInImpl>>() {
    }.getType();

    private static final Type TYPE_COIN_OUTS = new TypeToken<List<CoinOutImpl>>() {
    }.getType();

    private static final Type TYPE_BANKS = new TypeToken<List<BankImpl>>() {
    }.getType();

    private static final Type TYPE_DEPOSITS = new TypeToken<List<DepositImpl>>() {
    }.getType();

    private static final Type TYPE_WITHDRAWALS = new TypeToken<List<WithdrawalImpl>>() {
    }.getType();

    @Inject
    public AccountServiceImpl(Injector injector) {
        super(injector);
    }

    @Override
    public CompletableFuture<List<String>> getPermissions() {

        HttpRequest req = HttpRequest.builder().type(PathType.PERMISSION).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_STRINGS));

    }


    @Override
    public CompletableFuture<List<Balance>> getBalances() {

        HttpRequest req = HttpRequest.builder().type(PathType.BALANCE).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_BALANCES));

    }

    @Override
    public CompletableFuture<Collateral> getCollateral() {

        HttpRequest req = HttpRequest.builder().type(PathType.COLLATERAL).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), CollateralImpl.class));

    }

    @Override
    public CompletableFuture<List<Margin>> getMargins() {

        HttpRequest req = HttpRequest.builder().type(PathType.MARGIN).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_MARGINS));

    }

    @Override
    public CompletableFuture<List<Address>> getAddresses() {

        HttpRequest req = HttpRequest.builder().type(PathType.ADDRESS).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_ADDRESSES));

    }

    @Override
    public CompletableFuture<List<CoinIn>> getCoinIns(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.COIN_IN).parameters(params).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_COIN_INS));

    }

    @Override
    public CompletableFuture<List<CoinOut>> getCoinOuts(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.COIN_OUT).parameters(params).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_COIN_OUTS));

    }

    @Override
    public CompletableFuture<List<Bank>> getBanks() {

        HttpRequest req = HttpRequest.builder().type(PathType.BANK).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_BANKS));

    }

    @Override
    public CompletableFuture<List<Deposit>> getDeposits(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.DEPOSIT).parameters(params).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_DEPOSITS));

    }

    @Override
    public CompletableFuture<Withdraw.Response> withdraw(Withdraw request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.WITHDRAW).body(body).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), Withdraw.Response.class));

    }

    @Override
    public CompletableFuture<List<Withdrawal>> getWithdrawals(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.WITHDRAWAL).parameters(params).build();

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_WITHDRAWALS));

    }

}
