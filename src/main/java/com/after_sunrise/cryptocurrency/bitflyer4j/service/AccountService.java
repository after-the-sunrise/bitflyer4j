package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface AccountService {

    CompletableFuture<List<String>> getPermissions();

    CompletableFuture<List<Balance>> getBalances();

    CompletableFuture<Collateral> getCollateral();

    CompletableFuture<List<Margin>> getMargins();

    CompletableFuture<List<Address>> getAddresses();

    CompletableFuture<List<CoinIn>> getCoinIns(Pagination pagination);

    CompletableFuture<List<CoinOut>> getCoinOuts(Pagination pagination);

    CompletableFuture<List<Bank>> getBanks();

    CompletableFuture<List<Deposit>> getDeposits(Pagination pagination);

    CompletableFuture<Withdraw.Response> withdraw(Withdraw request);

    CompletableFuture<List<Withdrawal>> getWithdrawals(Pagination pagination);

}
