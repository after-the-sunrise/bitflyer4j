package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.pubnub.api.PubNub;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
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

        target = new PubNubProvider(module.createInjector());

    }

    @Test
    public void testGet() throws Exception {

        PubNub pn = target.get();

        Assert.assertNotNull(pn);

        pn.destroy();

    }

    @Test
    public void testGet_Configured() throws Exception {

        module.setProperty(PUBNUB_KEY, "testkey");
        module.setProperty(PUBNUB_SECURE, "true");
        module.setProperty(PUBNUB_RECONNECT, LINEAR.name());
        module.setProperty(HTTP_TIMEOUT, "1234");
        when(module.getMock(Environment.class).getProxy()).thenReturn(NO_PROXY);

        PubNub pn = target.get();

        assertEquals(pn.getConfiguration().getSubscribeKey(), "testkey");
        assertEquals(pn.getConfiguration().isSecure(), true);
        assertEquals(pn.getConfiguration().getProxy(), NO_PROXY);
        assertEquals(pn.getConfiguration().getConnectTimeout(), 1);
        assertEquals(pn.getConfiguration().getReconnectionPolicy(), LINEAR);

        pn.destroy();

    }

}