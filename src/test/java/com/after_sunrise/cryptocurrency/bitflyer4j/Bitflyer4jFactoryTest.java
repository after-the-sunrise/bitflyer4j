package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConfigurationType;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Properties;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static com.google.common.io.Resources.getResource;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyer4jFactoryTest {

    private Bitflyer4jFactory target;

    @BeforeMethod
    public void setUp() throws Exception {
        target = new Bitflyer4jFactory();
    }

    @Test
    public void testCreateInstance() throws Exception {

        Bitflyer4j api = target.createInstance();

        assertNotNull(api);

        api.close();

    }

    @Test
    public void testCreateConfiguration() throws Exception {

        Properties properties = new Properties();

        Configuration conf = target.createConfiguration(properties);

        for (Object name : Collections.list(System.getProperties().propertyNames())) {

            // All system properties must exist.
            assertTrue(conf.containsKey(name.toString()), "Missing system : " + name);

        }

        Configuration v = new Configurations().properties(getResource(ConfigurationType.VERSION.getPath()));
        Configuration s = new Configurations().properties(getResource(ConfigurationType.SITE.getPath()));

        // 1st should be version.
        assertEquals(VERSION.fetch(conf), v.getString(VERSION.getKey()));
        assertNotEquals(VERSION.fetch(conf), s.getString(VERSION.getKey()));
        assertEquals(s.getString(VERSION.getKey()), "test");

        // 2nd should be system. (Should not be overridden.)
        String system = "java.version";
        assertEquals(conf.getString(system), System.getProperty(system));
        assertNotEquals(conf.getString(system), s.getString(system));
        assertEquals(s.getString(system), "test");

        // 3rd should be site.
        assertEquals(SITE.fetch(conf), s.getString(SITE.getKey()));
        assertEquals(s.getString(SITE.getKey()), "test");

        // Last should be default.
        assertEquals(HTTP_URL.fetch(conf), HTTP_URL.getDefaultValue());

    }

}
