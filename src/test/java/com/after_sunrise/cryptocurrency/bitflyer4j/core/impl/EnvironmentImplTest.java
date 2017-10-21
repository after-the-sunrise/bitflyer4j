package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.pubnub.api.enums.PNReconnectionPolicy;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.MapConfiguration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.lang.Long.parseLong;
import static java.net.Proxy.Type.DIRECT;
import static java.net.Proxy.Type.SOCKS;
import static java.time.Duration.ofMillis;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class EnvironmentImplTest {

    private EnvironmentImpl target;

    private AbstractConfiguration conf;

    @BeforeMethod
    public void setUp() throws Exception {

        conf = new MapConfiguration(new HashMap<>());

        target = new EnvironmentImpl(conf, conf);

    }

    @Test
    public void testGetNow() throws Exception {

        Instant t1 = Instant.now();
        Thread.sleep(5L);

        Instant t2 = target.getNow();
        Thread.sleep(5L);

        Instant t3 = Instant.now();
        Assert.assertTrue(t1.isBefore(t2));
        Assert.assertTrue(t2.isBefore(t3));

    }

    @Test
    public void testGetVersion() {

        assertEquals(target.getVersion(), VERSION.getDefaultValue());

        conf.setProperty(VERSION.getKey(), "");
        assertNull(target.getVersion());

        conf.setProperty(VERSION.getKey(), "testversion");
        assertEquals(target.getVersion(), "testversion");

    }

    @Test
    public void testGetSite() {

        assertEquals(target.getSite(), SITE.getDefaultValue());

        conf.setProperty(SITE.getKey(), "");
        assertNull(target.getSite());

        conf.setProperty(SITE.getKey(), "testsite");
        assertEquals(target.getSite(), "testsite");

    }

    @Test
    public void testGetUrl() {

        assertEquals(target.getUrl(), HTTP_URL.getDefaultValue());

        conf.setProperty(HTTP_URL.getKey(), "");
        assertNull(target.getUrl());

        conf.setProperty(HTTP_URL.getKey(), "http://localhost:65535");
        assertEquals(target.getUrl(), "http://localhost:65535");

    }

    @Test
    public void testGetProxy() {

        assertNull(target.getProxy());

        conf.setProperty(HTTP_PROXY_PORT.getKey(), "");
        conf.setProperty(HTTP_PROXY_HOST.getKey(), "");
        conf.setProperty(HTTP_PROXY_TYPE.getKey(), "");
        assertNull(target.getProxy());

        conf.setProperty(HTTP_PROXY_TYPE.getKey(), DIRECT.name());
        assertEquals(target.getProxy(), Proxy.NO_PROXY);

        conf.setProperty(HTTP_PROXY_PORT.getKey(), "65535");
        conf.setProperty(HTTP_PROXY_HOST.getKey(), "localhost");
        conf.setProperty(HTTP_PROXY_TYPE.getKey(), SOCKS.name());
        Proxy proxy = target.getProxy();
        assertEquals(proxy.type(), SOCKS);
        assertEquals(((InetSocketAddress) proxy.address()).getHostName(), "localhost");
        assertEquals(((InetSocketAddress) proxy.address()).getPort(), 65535);

    }

    @Test
    public void testGetTimeout() {

        assertEquals(Duration.ofMinutes(5), target.getTimeout());

        conf.setProperty(HTTP_TIMEOUT.getKey(), "");
        assertNull(target.getTimeout());

        conf.setProperty(HTTP_TIMEOUT.getKey(), "1234");
        assertEquals(target.getTimeout(), ofMillis(1234));

    }

    @Test
    public void testGetAuthKey() {

        assertNull(target.getAuthKey());

        conf.setProperty(AUTH_KEY.getKey(), "");
        assertNull(target.getAuthKey());

        conf.setProperty(AUTH_KEY.getKey(), "testkey");
        assertEquals(target.getAuthKey(), "testkey");

    }

    @Test
    public void testGetAuthSecret() {

        assertNull(target.getAuthSecret());

        conf.setProperty(AUTH_SECRET.getKey(), "");
        assertNull(target.getAuthSecret());

        conf.setProperty(AUTH_SECRET.getKey(), "testsecret");
        assertEquals(target.getAuthSecret(), "testsecret");

    }

    @Test
    public void testGetHttpThreads() {

        Integer threads = Integer.parseInt(HTTP_THREADS.getDefaultValue());
        assertEquals(target.getHttpThreads(), threads);

        conf.setProperty(HTTP_THREADS.getKey(), "");
        assertNull(target.getHttpThreads());

        conf.setProperty(HTTP_THREADS.getKey(), "123");
        assertEquals(target.getHttpThreads(), Integer.valueOf(123));

    }

    @Test
    public void testGetHttpLimitInterval() {

        Duration duration = ofMillis(parseLong(HTTP_LIMIT_INTERVAL.getDefaultValue()));
        assertEquals(target.getHttpLimitInterval(), duration);

        conf.setProperty(HTTP_LIMIT_INTERVAL.getKey(), "");
        assertNull(target.getHttpLimitInterval());

        conf.setProperty(HTTP_LIMIT_INTERVAL.getKey(), "1234");
        assertEquals(target.getHttpLimitInterval(), ofMillis(1234));

    }

    @Test
    public void testGetHttpLimitAddress() {

        Integer limit = Integer.parseInt(HTTP_LIMIT_CRITERIA_ADDRESS.getDefaultValue());
        assertEquals(target.getHttpLimitAddress(), limit);

        conf.setProperty(HTTP_LIMIT_CRITERIA_ADDRESS.getKey(), "");
        assertNull(target.getHttpLimitAddress());

        conf.setProperty(HTTP_LIMIT_CRITERIA_ADDRESS.getKey(), "123");
        assertEquals(target.getHttpLimitAddress(), Integer.valueOf(123));

    }

    @Test
    public void testGetHttpLimitPrivate() {

        Integer limit = Integer.parseInt(HTTP_LIMIT_CRITERIA_PRIVATE.getDefaultValue());
        assertEquals(target.getHttpLimitPrivate(), limit);

        conf.setProperty(HTTP_LIMIT_CRITERIA_PRIVATE.getKey(), "");
        assertNull(target.getHttpLimitPrivate());

        conf.setProperty(HTTP_LIMIT_CRITERIA_PRIVATE.getKey(), "123");
        assertEquals(target.getHttpLimitPrivate(), Integer.valueOf(123));

    }

    @Test
    public void testGetHttpLimitDormant() {

        Integer limit = Integer.parseInt(HTTP_LIMIT_CRITERIA_DORMANT.getDefaultValue());
        assertEquals(target.getHttpLimitDormant(), limit);

        conf.setProperty(HTTP_LIMIT_CRITERIA_DORMANT.getKey(), "");
        assertNull(target.getHttpLimitDormant());

        conf.setProperty(HTTP_LIMIT_CRITERIA_DORMANT.getKey(), "123");
        assertEquals(target.getHttpLimitDormant(), Integer.valueOf(123));

    }

    @Test
    public void testGetPubNubKey() {

        assertEquals(target.getPubNubKey(), PUBNUB_KEY.getDefaultValue());

        conf.setProperty(PUBNUB_KEY.getKey(), "");
        assertNull(target.getPubNubKey());

        conf.setProperty(PUBNUB_KEY.getKey(), "testkey");
        assertEquals(target.getPubNubKey(), "testkey");

    }

    @Test
    public void testGetPubNubReconnect() {

        String value = PUBNUB_RECONNECT.getDefaultValue();
        assertEquals(target.getPubNubReconnect(), PNReconnectionPolicy.valueOf(value));

        conf.setProperty(PUBNUB_RECONNECT.getKey(), "");
        assertNull(target.getPubNubReconnect());

        conf.setProperty(PUBNUB_RECONNECT.getKey(), PNReconnectionPolicy.NONE.name());
        assertEquals(target.getPubNubReconnect(), PNReconnectionPolicy.NONE);

    }

    @Test
    public void testGetPubNubSecure() {

        assertEquals(target.getPubNubSecure(), Boolean.valueOf(PUBNUB_SECURE.getDefaultValue()));

        conf.setProperty(PUBNUB_SECURE.getKey(), "");
        assertNull(target.getPubNubSecure());

        conf.setProperty(PUBNUB_SECURE.getKey(), "false");
        assertEquals(target.getPubNubSecure(), Boolean.FALSE);

    }

}