package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.concurrent.atomic.AtomicReference;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.lang.Integer.parseInt;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class EnvironmentImpl implements Environment {

    private final AtomicReference<Proxy[]> proxy = new AtomicReference<>();

    private final Configuration conf;

    @Inject
    public EnvironmentImpl(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public long getTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public Proxy getProxy() {

        synchronized (proxy) {

            Proxy[] p = proxy.get();

            if (p == null) {

                p = new Proxy[]{createProxy()};

                proxy.set(p);

            }

            return p[0];

        }

    }

    private Proxy createProxy() {

        String type = HTTP_PROXY_TYPE.apply(conf);

        if (StringUtils.isEmpty(type)) {

            log.debug("Skipping HTTP proxy.");

            return null;

        }

        Type t = Type.valueOf(type);

        Proxy p;

        if (t == Type.DIRECT) {

            p = Proxy.NO_PROXY;

        } else {

            String host = HTTP_PROXY_HOST.apply(conf);

            String port = HTTP_PROXY_PORT.apply(conf);

            p = new Proxy(t, new InetSocketAddress(host, parseInt(port)));

        }

        log.debug("Created HTTP proxy : {}", p);

        return p;

    }

}
