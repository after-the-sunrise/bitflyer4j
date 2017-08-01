package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.enums.PNReconnectionPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.net.Proxy;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class PubNubProvider implements Provider<PubNub> {

    private final Configuration conf;

    private final Environment environment;

    @Inject
    public PubNubProvider(Injector injector) {

        this.conf = injector.getInstance(Configuration.class);

        this.environment = injector.getInstance(Environment.class);

    }

    @Override
    public PubNub get() {

        PNConfiguration pnc = new PNConfiguration();

        String key = PUBNUB_KEY.apply(conf);

        if (StringUtils.isNotEmpty(key)) {

            pnc.setSubscribeKey(key);

            log.debug("Configured subscribe key : {}", key);

        }

        String secure = PUBNUB_SECURE.apply(conf);

        if (StringUtils.isNotEmpty(secure)) {

            pnc.setSecure(Boolean.valueOf(secure));

            log.debug("Configured secure : {}", Boolean.valueOf(secure));

        }

        Proxy proxy = environment.getProxy();

        if (proxy != null) {

            pnc.setProxy(proxy);

            log.debug("Configured proxy : {}", proxy);

        }

        String timeout = HTTP_TIMEOUT.apply(conf);

        if (StringUtils.isNotEmpty(timeout)) {

            long sec = MILLISECONDS.toSeconds(Integer.parseInt(timeout));

            pnc.setConnectTimeout((int) sec);

            log.debug("Configured timeout : {} sec", sec);

        }

        String reconnect = PUBNUB_RECONNECT.apply(conf);

        if (StringUtils.isNotEmpty(reconnect)) {

            pnc.setReconnectionPolicy(PNReconnectionPolicy.valueOf(reconnect));

            log.debug("Configured reconnect : {}", reconnect);

        }

        PubNub pubNub = new PubNub(pnc);

        log.debug("Created instance : {}", pubNub.getInstanceId());

        return pubNub;

    }

}
