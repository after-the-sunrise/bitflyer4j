package com.after_sunrise.cryptocurrency.bitflyer4j;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static com.google.common.io.Resources.getResource;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyerj4FactoryTest {

    private Bitflyerj4Factory target;

    @BeforeMethod
    public void setUp() throws Exception {
        target = new Bitflyerj4Factory();
    }

    @Test
    public void testCreateInstance() throws Exception {

        Bitflyer4j api = target.createInstance();

        assertNotNull(api);

        api.close();

    }

    @Test
    public void testCreateConfiguration() throws Exception {

        Configuration conf = target.createConfiguration();

        for (Object name : Collections.list(System.getProperties().propertyNames())) {

            // All system properties must exist.
            assertTrue(conf.containsKey(name.toString()), "Missing system : " + name);

        }

        Configuration v = new PropertiesConfiguration(getResource("bitflyer4j-version.properties"));
        Configuration s = new PropertiesConfiguration(getResource("bitflyer4j-site.properties"));

        // 1st should be version.
        assertEquals(VERSION.apply(conf), v.getString(VERSION.get()));
        assertNotEquals(VERSION.apply(conf), s.getString(VERSION.get()));
        assertEquals(s.getString(VERSION.get()), "test");

        // 2nd should be system. (Should not be overridden.)
        String system = "java.version";
        assertEquals(conf.getString(system), System.getProperty(system));
        assertNotEquals(conf.getString(system), s.getString(system));
        assertEquals(s.getString(system), "test");

        // 3rd should be site.
        assertEquals(AUTH_KEY.apply(conf), s.getString(AUTH_KEY.get()));
        assertEquals(s.getString(AUTH_KEY.get()), "test");

        // Last should be default.
        assertEquals(HTTP_URL_BASE.apply(conf), HTTP_URL_BASE.getDefault());

    }

}