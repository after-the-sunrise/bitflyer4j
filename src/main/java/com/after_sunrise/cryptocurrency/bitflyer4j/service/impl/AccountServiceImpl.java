package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
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
public class AccountServiceImpl extends HttpService implements AccountService {

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

        return request(req, TYPE_STRINGS);

    }

    @Override
    public CompletableFuture<List<Balance>> getBalances() {

        HttpRequest req = HttpRequest.builder().type(PathType.BALANCE).build();

        return request(req, TYPE_BALANCES);

    }

    @Override
    public CompletableFuture<Collateral> getCollateral() {

        HttpRequest req = HttpRequest.builder().type(PathType.COLLATERAL).build();

        return request(req, CollateralImpl.class);

    }

    @Override
    public CompletableFuture<List<Margin>> getMargins() {

        HttpRequest req = HttpRequest.builder().type(PathType.MARGIN).build();

        return request(req, TYPE_MARGINS);

    }

    @Override
    public CompletableFuture<List<Address>> getAddresses() {

        HttpRequest req = HttpRequest.builder().type(PathType.ADDRESS).build();

        return request(req, TYPE_ADDRESSES);

    }

    @Override
    public CompletableFuture<List<CoinIn>> getCoinIns(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.COIN_IN).parameters(params).build();

        return request(req, TYPE_COIN_INS);

    }

    @Override
    public CompletableFuture<List<CoinOut>> getCoinOuts(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.COIN_OUT).parameters(params).build();

        return request(req, TYPE_COIN_OUTS);

    }

    @Override
    public CompletableFuture<List<Bank>> getBanks() {

        HttpRequest req = HttpRequest.builder().type(PathType.BANK).build();

        return request(req, TYPE_BANKS);

    }

    @Override
    public CompletableFuture<List<Deposit>> getDeposits(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.DEPOSIT).parameters(params).build();

        return request(req, TYPE_DEPOSITS);

    }

    @Override
    public CompletableFuture<Withdraw.Response> withdraw(Withdraw request) {

        String body = gson.toJson(request);

        HttpRequest req = HttpRequest.builder().type(PathType.WITHDRAW).body(body).build();

        return request(req, WithdrawResponse.class);

    }

    @Override
    public CompletableFuture<List<Withdrawal>> getWithdrawals(Pagination pagination) {

        Map<String, String> params = prepareParameter(pagination);

        HttpRequest req = HttpRequest.builder().type(PathType.WITHDRAWAL).parameters(params).build();

        return request(req, TYPE_WITHDRAWALS);

    }

}
