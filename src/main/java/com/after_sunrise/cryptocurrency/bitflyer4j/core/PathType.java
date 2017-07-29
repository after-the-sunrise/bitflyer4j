package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.GET;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.POST;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public enum PathType {

    MARKET("/v1/markets", false),

    BOARD("/v1/board", false),

    TICKER("/v1/ticker", false),

    EXECUTION("/v1/executions", false),

    HEALTH("/v1/gethealth", false),

    CHAT("/v1/getchats", false),

    PERMISSION("/v1/me/getpermissions"),

    BALANCE("/v1/me/getbalance"),

    COLLATERAL("/v1/me/getcollateral"),

    MARGIN("/v1/me/getcollateralaccounts"),

    ADDRESS("/v1/me/getaddresses"),

    COIN_IN("/v1/me/getcoinins"),

    COIN_OUT("/v1/me/getcoinouts"),

    BANK("/v1/me/getbankaccounts"),

    DEPOSIT("/v1/me/getdeposits"),

    WITHDRAW("/v1/me/withdraw", POST),

    WITHDRAWAL("/v1/me/getwithdrawals"),

    ORDER_SEND("/v1/me/sendchildorder", POST),

    ORDER_CANCEL("/v1/me/cancelchildorder", POST),

    PARENT_SEND("/v1/me/sendparentorder", POST),

    PARENT_CANCEL("/v1/me/cancelparentorder", POST),

    PRODUCT_CANCEL("/v1/me/cancelallchildorders", POST),

    ORDER_LIST("/v1/me/getchildorders"),

    PARENT_LIST("/v1/me/getparentorders"),

    PARENT_DETAIL("/v1/me/getparentorder"),

    TRADE_EXECUTION("/v1/me/getexecutions"),

    TRADE_COLLATERAL("/v1/me/getmycollateralhistory"),

    TRADE_COMMISSION("/v1/me/getexecutions");

    private final String path;

    private final boolean sign;

    private final MethodType method;

    PathType(String path) {
        this(path, true);
    }

    PathType(String path, boolean sign) {
        this(path, sign, GET);
    }

    PathType(String path, MethodType method) {
        this(path, true, method);
    }

    PathType(String path, boolean sign, MethodType method) {
        this.path = path;
        this.sign = sign;
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public boolean isSign() {
        return sign;
    }

    public MethodType getMethod() {
        return method;
    }

}
