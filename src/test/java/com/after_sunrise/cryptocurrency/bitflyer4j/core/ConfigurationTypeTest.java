package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import org.apache.commons.configuration.Configuration;
import org.testng.annotations.Test;

import java.util.*;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ConfigurationTypeTest {

    @Test
    public void test() {

        for (ConfigurationType type : ConfigurationType.values()) {

            Configuration conf = type.get().orElse(null);

            Map<String, String> map = new HashMap<>();

            if (conf != null) {
                for (Iterator<String> itr = conf.getKeys(); itr.hasNext(); ) {
                    String key = itr.next();
                    map.put(key, conf.getString(key));
                }
            }

            switch (type) {
                case VERSION:
                    assertNotNull(VERSION.apply(conf));
                    assertEquals(map.size(), 1);
                    break;
                case SYSTEM:
                    List<?> system = Collections.list(System.getProperties().propertyNames());
                    system.forEach(o -> assertTrue(conf.containsKey(o.toString()), "Missing : " + o));
                    assertEquals(map.size(), system.size());
                    break;
                case HOME:
                    // Do nothing. (Dependent on the test machine.)
                    break;
                case SITE:
                    assertEquals(VERSION.apply(conf), "test");
                    assertEquals(AUTH_KEY.apply(conf), "test");
                    assertEquals(AUTH_SECRET.apply(conf), "test");
                    break;
                default:
                    fail("Unknown type : " + type);

            }

        }

    }

    @Test
    public void testGet() throws Exception {

        String site = "bitflyer4j-site.properties";

        // Classpath exists
        Configuration conf = ConfigurationType.get(site, null).get();
        assertEquals(conf.getString(VERSION.get()), "test");

        // Classpath does not exist. (And should not load from file path.)
        assertFalse(ConfigurationType.get("build.gradle", null).isPresent());

        // File exists.
        conf = ConfigurationType.get(site, "src/test/resources").get();
        assertEquals(conf.getString(VERSION.get()), "test");

        // File does not exist. (And should not load from classpath.)
        assertFalse(ConfigurationType.get(site, "build").isPresent());

    }

}