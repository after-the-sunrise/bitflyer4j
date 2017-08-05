package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.pubnub.api.enums.PNReconnectionPolicy;

import java.net.Proxy;
import java.time.Duration;
import java.time.Instant;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Environment {

    Instant getNow();

    String getVersion();

    String getUrl();

    Proxy getProxy();

    Duration getTimeout();

    String getAuthKey();

    String getAuthSecret();

    Duration getHttpLimitInterval();

    Integer getHttpLimitAddress();

    Integer getHttpLimitPrivate();

    Integer getHttpLimitDormant();

    String getPubNubKey();

    PNReconnectionPolicy getPubNubReconnect();

    Boolean getPubNubSecure();

}
