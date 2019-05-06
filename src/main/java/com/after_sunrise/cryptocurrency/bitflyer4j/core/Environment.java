package com.after_sunrise.cryptocurrency.bitflyer4j.core;

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

    String getSite();

    String getUrl();

    Proxy getProxy();

    Duration getTimeout();

    String getAuthKey();

    String getAuthSecret();

    Integer getHttpThreads();

    Duration getHttpLimitInterval();

    Integer getHttpLimitAddress();

    Integer getHttpLimitPrivate();

    String getSocketEndpoint();

}
