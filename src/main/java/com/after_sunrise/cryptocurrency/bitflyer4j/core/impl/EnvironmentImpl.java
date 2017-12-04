package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType;
import com.pubnub.api.enums.PNReconnectionPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.event.EventSource;

import javax.inject.Inject;
import java.net.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.configuration2.event.ConfigurationEvent.ANY;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class EnvironmentImpl implements Environment, EventListener<ConfigurationEvent> {

    private final AtomicReference<Map<KeyType, Object>> cache;

    private final Configuration conf;

    private final EventSource source;

    @Inject
    public EnvironmentImpl(Configuration conf, EventSource source) {

        this.cache = new AtomicReference<>(emptyMap());

        this.conf = conf;

        this.source = source;

        this.source.addEventListener(ANY, this);

        this.onEvent(null);

    }

    @Override
    public void onEvent(ConfigurationEvent event) {

        log.debug("Refreshing cache : {}", event);

        Map<KeyType, Object> map = new HashMap<>();

        Arrays.stream(KeyType.values()).forEach(t -> map.put(t, t.fetch(conf)));

        cache.set(unmodifiableMap(map));

    }

    @Override
    public Instant getNow() {
        return Instant.now();
    }

    @Override
    public String getVersion() {
        return String.class.cast(cache.get().get(VERSION));
    }

    @Override
    public String getSite() {
        return String.class.cast(cache.get().get(SITE));
    }

    @Override
    public String getUrl() {
        return String.class.cast(cache.get().get(HTTP_URL));
    }

    @Override
    public Proxy getProxy() {
        return Proxy.class.cast(cache.get().get(HTTP_PROXY_TYPE));
    }

    @Override
    public Duration getTimeout() {
        return Duration.class.cast(cache.get().get(HTTP_TIMEOUT));
    }

    @Override
    public String getAuthKey() {
        return String.class.cast(cache.get().get(AUTH_KEY));
    }

    @Override
    public String getAuthSecret() {
        return String.class.cast(cache.get().get(AUTH_SECRET));
    }

    @Override
    public Integer getHttpThreads() {
        return Integer.class.cast(cache.get().get(HTTP_THREADS));
    }

    @Override
    public Duration getHttpLimitInterval() {
        return Duration.class.cast(cache.get().get(HTTP_LIMIT_INTERVAL));
    }

    @Override
    public Integer getHttpLimitAddress() {
        return Integer.class.cast(cache.get().get(HTTP_LIMIT_CRITERIA_ADDRESS));
    }

    @Override
    public Integer getHttpLimitPrivate() {
        return Integer.class.cast(cache.get().get(HTTP_LIMIT_CRITERIA_PRIVATE));
    }

    @Override
    public Integer getHttpLimitDormant() {
        return Integer.class.cast(cache.get().get(HTTP_LIMIT_CRITERIA_DORMANT));
    }

    @Override
    public String getPubNubKey() {
        return String.class.cast(cache.get().get(PUBNUB_KEY));
    }

    @Override
    public PNReconnectionPolicy getPubNubReconnect() {
        return PNReconnectionPolicy.class.cast(cache.get().get(PUBNUB_RECONNECT));
    }

    @Override
    public Boolean getPubNubSecure() {
        return Boolean.class.cast(cache.get().get(PUBNUB_SECURE));
    }

}
