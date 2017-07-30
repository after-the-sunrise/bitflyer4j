package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.PubNubFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.PUBNUB_KEY;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class PubNubFactoryImpl implements PubNubFactory {

    private final AtomicReference<PubNub> reference = new AtomicReference<>();

    private final Configuration conf;

    @Inject
    public PubNubFactoryImpl(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public PubNub get() {

        synchronized (reference) {

            PubNub pn = reference.get();

            if (pn == null) {

                pn = createInstance();

                reference.set(pn);

                log.debug("Created instance : {}", pn.getInstanceId());

            }

            return pn;

        }

    }

    @Override
    public void close() throws IOException {

        synchronized (reference) {

            PubNub pn = reference.getAndSet(null);

            if (pn == null) {
                return;
            }

            pn.destroy();

            log.debug("Destroyed instance : {}", pn.getInstanceId());

        }

    }

    @VisibleForTesting
    PubNub createInstance() {

        String key = PUBNUB_KEY.apply(conf);

        PNConfiguration pnc = new PNConfiguration();

        pnc.setSubscribeKey(key);

        return new PubNub(pnc);

    }

}
