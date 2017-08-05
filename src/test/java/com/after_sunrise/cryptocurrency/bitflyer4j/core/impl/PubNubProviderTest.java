package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.pubnub.api.PubNub;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static com.pubnub.api.enums.PNReconnectionPolicy.LINEAR;
import static java.net.Proxy.NO_PROXY;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class PubNubProviderTest {

    private PubNubProvider target;

    private TestModule module;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        target = new PubNubProvider(module.getEnvironment());

    }

    @Test
    public void testGet() throws Exception {

        PubNub pn = target.get();

        Assert.assertNotNull(pn);

        pn.destroy();

    }

    @Test
    public void testGet_Configured() throws Exception {

        when(module.getEnvironment().getTimeout()).thenReturn(Duration.ofMillis(1234));
        when(module.getEnvironment().getProxy()).thenReturn(NO_PROXY);
        when(module.getEnvironment().getPubNubKey()).thenReturn("testkey");
        when(module.getEnvironment().getPubNubSecure()).thenReturn(Boolean.TRUE);
        when(module.getEnvironment().getPubNubReconnect()).thenReturn(LINEAR);

        PubNub pn = target.get();

        assertEquals(pn.getConfiguration().getSubscribeKey(), "testkey");
        assertEquals(pn.getConfiguration().isSecure(), true);
        assertEquals(pn.getConfiguration().getProxy(), NO_PROXY);
        assertEquals(pn.getConfiguration().getConnectTimeout(), 1);
        assertEquals(pn.getConfiguration().getReconnectionPolicy(), LINEAR);

        pn.destroy();

    }

}