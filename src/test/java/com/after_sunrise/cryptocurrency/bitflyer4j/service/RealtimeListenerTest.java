package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeListener.RealtimeListenerAdapter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author takanori.takase
 * @version 0.0.3
 */
public class RealtimeListenerTest {

    private RealtimeListenerAdapter target;

    @BeforeMethod
    public void setUp() throws Exception {
        target = new RealtimeListenerAdapter();
    }

    @Test
    public void testOnBoards() throws Exception {
        target.onBoards(null);
    }

    @Test
    public void testOnTicks() throws Exception {
        target.onTicks(null);
    }

    @Test
    public void testOnExecutions() throws Exception {
        target.onExecutions(null);
    }

}