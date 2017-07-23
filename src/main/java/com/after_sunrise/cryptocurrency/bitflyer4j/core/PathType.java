package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.GET;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.POST;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public enum PathType {

    MARKET("/v1/markets"),

    BOARD("/v1/board"),

    TICKER("/v1/ticker"),

    EXECUTION("/v1/execution"),

    HEALTH("/v1/gethealth"),

    CHAT("/v1/getchats"),

    PERMISSION("/v1/me/getpermissions", true),

    BALANCE("/v1/me/getbalance", true),

    MARGIN_STATUS("/v1/me/getcollateral", true),

    MARGIN_POSITION("/v1/me/getcollateralaccounts", true),

    ORDER_SEND("/v1/me/sendchildorder", true, POST),

    ORDER_CANCEL("/v1/me/cancelchildorder", true, POST);

    private String path;

    private boolean sign;

    private MethodType method;

    PathType(String path) {
        this(path, false);
    }

    PathType(String path, boolean sign) {
        this(path, sign, GET);
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
