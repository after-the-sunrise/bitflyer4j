package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.ZonedDateTime;

import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public class GsonProviderTest {

    private static enum TestType {
        HOGE, FOO_BAR;
    }

    private GsonProvider target;

    private Gson gson;

    @BeforeMethod
    public void setUp() throws Exception {

        target = new GsonProvider();

        gson = target.get();

    }

    @Test
    public void testGet_ZonedDateTime() throws Exception {

        ZonedDateTime result = gson.fromJson("null", ZonedDateTime.class);
        assertNull(result);

        result = gson.fromJson("\"2017-04-14T12:34:56.78\"", ZonedDateTime.class);
        assertEquals(result.getZone().getId(), "GMT");
        assertEquals(result.getYear(), 2017);
        assertEquals(result.getMonthValue(), 4);
        assertEquals(result.getDayOfMonth(), 14);
        assertEquals(result.getHour(), 12);
        assertEquals(result.getMinute(), 34);
        assertEquals(result.getSecond(), 56);
        assertEquals(result.getNano(), 780 * 1000L * 1000L);

        try {
            gson.fromJson("\"foo\"", ZonedDateTime.class);
            fail();
        } catch (JsonParseException e) {
            // Success
        }

    }

    @Test
    public void testGet_Enum() throws Exception {

        assertNull(gson.fromJson("null", TestType.class));

        assertEquals(gson.fromJson("\"HOGE\"", TestType.class), TestType.HOGE);

        assertEquals(gson.fromJson("\"hoge\"", TestType.class), TestType.HOGE);

        assertEquals(gson.fromJson("\"FOO BAR\"", TestType.class), TestType.FOO_BAR);

        assertNull(gson.fromJson("\"INVALID\"", TestType.class));

    }

}