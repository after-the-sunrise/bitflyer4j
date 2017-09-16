package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.enums.PNReconnectionPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.Proxy;
import java.time.Duration;
import java.util.Objects;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class PubNubProvider implements Provider<PubNub> {

    private final Environment environment;

    @Inject
    public PubNubProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public PubNub get() {

        PNConfiguration pnc = new PNConfiguration();

        String key = environment.getPubNubKey();

        if (StringUtils.isNotEmpty(key)) {

            pnc.setSubscribeKey(key);

            log.debug("Configured subscribe key : {}", key);

        }

        Boolean secure = environment.getPubNubSecure();

        if (Objects.equals(Boolean.TRUE, secure)) {

            pnc.setSecure(secure);

            log.debug("Configured secure : {}", secure);

        }

        Proxy proxy = environment.getProxy();

        if (proxy != null) {

            pnc.setProxy(proxy);

            log.debug("Configured proxy : {}", proxy);

        }

        Duration timeout = environment.getTimeout();

        if (timeout != null) {

            long sec = timeout.getSeconds();

            pnc.setConnectTimeout((int) sec);

            log.debug("Configured timeout : {} sec", sec);

        }

        PNReconnectionPolicy reconnect = environment.getPubNubReconnect();

        if (reconnect != null) {

            pnc.setReconnectionPolicy(reconnect);

            log.debug("Configured reconnect : {}", reconnect);

        }

        PubNub pubNub = new PubNub(pnc);

        log.debug("Created instance : {}", pubNub.getInstanceId());

        return pubNub;

    }

}
