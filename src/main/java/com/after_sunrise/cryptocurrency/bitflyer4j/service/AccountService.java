package com.after_sunrise.cryptocurrency.bitflyer4j.service;

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

    CompletableFuture<List<CoinIn>> getCoinIns(CoinIn.Request request);

    CompletableFuture<List<CoinOut>> getCoinOuts(CoinOut.Request request);

    CompletableFuture<List<Bank>> getBanks();

    CompletableFuture<List<Deposit>> getDeposits(Deposit.Request request);

    CompletableFuture<Withdraw> withdraw(Withdraw.Request request);

    CompletableFuture<List<Withdrawal>> getWithdrawals(Withdrawal.Request request);

}
