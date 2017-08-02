package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import org.apache.commons.configuration2.Configuration;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.net.Proxy.Type.DIRECT;
import static java.net.Proxy.Type.HTTP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class EnvironmentImplTest {

    private EnvironmentImpl target;

    private TestModule module;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        target = new EnvironmentImpl(module.getMock(Configuration.class));

    }

    @Test
    public void testGetTimeMillis() throws Exception {

        long t1 = System.currentTimeMillis();

        Thread.sleep(5L);

        long t2 = target.getTimeMillis();

        Thread.sleep(5L);

        long t3 = System.currentTimeMillis();

        Assert.assertTrue(t1 < t2);

        Assert.assertTrue(t2 < t3);

    }

    @Test
    public void testGetProxy_Null() throws Exception {
        Assert.assertNull(target.getProxy());
        Assert.assertNull(target.getProxy());
        Assert.assertNull(target.getProxy());
    }

    @Test
    public void testGetProxy_Direct() throws Exception {

        module.setProperty(HTTP_PROXY_TYPE, DIRECT.name());

        assertSame(target.getProxy(), Proxy.NO_PROXY);
        assertSame(target.getProxy(), Proxy.NO_PROXY);
        assertSame(target.getProxy(), Proxy.NO_PROXY);

    }

    @Test
    public void testGetProxy_Http() throws Exception {

        module.setProperty(HTTP_PROXY_TYPE, HTTP.name());
        module.setProperty(HTTP_PROXY_HOST, "localhost");
        module.setProperty(HTTP_PROXY_PORT, "65535");

        Proxy p = target.getProxy();
        assertEquals(((InetSocketAddress) p.address()).getHostName(), "localhost");
        assertEquals(((InetSocketAddress) p.address()).getPort(), 65535);

        assertSame(target.getProxy(), p);
        assertSame(target.getProxy(), p);
        assertSame(target.getProxy(), p);

    }

}