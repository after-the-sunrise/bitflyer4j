package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.annotations.Test;

import java.util.HashMap;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.VERSION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class KeyTypeTest {

    @Test
    public void test() throws ConfigurationException {

        // Null input.  (= Default value.)
        assertEquals(VERSION.fetch(null), VERSION.getDefaultValue());

        // Not found. (= Default value.)
        Configuration conf = new MapConfiguration(new HashMap<>());
        assertEquals(VERSION.fetch(conf), VERSION.getDefaultValue());

        // Retrieved from properties. (Empty)
        conf.setProperty(VERSION.getKey(), "");
        assertNull(VERSION.fetch(conf));

        // Retrieved from properties. (Configured)
        conf.setProperty(VERSION.getKey(), "test");
        assertEquals(VERSION.fetch(conf), "test");

    }

    @Test
    public void testFetch_Unconfigured() throws ConfigurationException {

        Configuration c = new MapConfiguration(new HashMap<>());

        for (KeyType type : KeyType.values()) {

            type.fetch(null);

            type.fetch(c);

        }

    }

    @Test
    public void testFetch_Empty() throws ConfigurationException {

        Configuration c = new MapConfiguration(new HashMap<>());

        for (KeyType type : KeyType.values()) {
            c.setProperty(type.getKey(), "");
        }

        for (KeyType type : KeyType.values()) {
            assertNull(type.fetch(c));
        }

    }

}