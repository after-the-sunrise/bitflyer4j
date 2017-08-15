package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeService;
import com.pubnub.api.PubNub;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jImplTest {

    private Bitflyer4jImpl target;

    private TestModule module;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        when(module.getEnvironment().getVersion()).thenReturn(getClass().getSimpleName());

        when(module.getEnvironment().getSite()).thenReturn(getClass().getTypeName());

        target = new Bitflyer4jImpl(module.createInjector());

    }

    @AfterMethod
    public void tearDown() throws Exception {

        target.close();

        verify(module.getMock(ExecutorFactory.class)).shutdown();

        verify(module.getMock(PubNub.class)).destroy();

    }

    @Test
    public void testGetVersion() throws Exception {

        assertEquals(target.getVersion(), getClass().getSimpleName());

    }

    @Test
    public void testGetSite() throws Exception {

        assertEquals(target.getSite(), getClass().getTypeName());

    }

    @Test
    public void testGetMarketService() throws Exception {

        MarketService service = target.getMarketService();

        assertSame(service, module.getMock(MarketService.class));

    }

    @Test
    public void testGetAccountService() throws Exception {

        AccountService service = target.getAccountService();

        assertSame(service, module.getMock(AccountService.class));

    }

    @Test
    public void testGetOrderService() throws Exception {

        OrderService service = target.getOrderService();

        assertSame(service, module.getMock(OrderService.class));

    }

    @Test
    public void testGetRealtimeService() throws Exception {

        RealtimeService service = target.getRealtimeService();

        assertSame(service, module.getMock(RealtimeService.class));

    }

}
