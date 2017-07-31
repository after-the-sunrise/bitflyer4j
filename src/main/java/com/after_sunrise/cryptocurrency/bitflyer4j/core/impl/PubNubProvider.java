package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.net.Proxy;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.HTTP_TIMEOUT;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.PUBNUB_KEY;
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

        String timeout = HTTP_TIMEOUT.apply(conf);

        if (timeout != null) {

            long sec = MILLISECONDS.toSeconds(Integer.parseInt(timeout));

            pnc.setConnectTimeout((int) sec);

            log.debug("Configured timeout : {} sec", sec);

        }

        Proxy proxy = environment.getProxy();

        if (proxy != null) {

            pnc.setProxy(proxy);

            log.debug("Configured proxy : {}", proxy);

        }

        PubNub pubNub = new PubNub(pnc);

        log.debug("Created instance : {}", pubNub.getInstanceId());

        return pubNub;

    }

}
