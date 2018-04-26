package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.common.io.Resources;
import org.apache.commons.configuration2.Configuration;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.SITE;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.VERSION;
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
                    assertNotNull(conf.getString(VERSION.getKey()));
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
                    assertEquals(conf.getString(VERSION.getKey()), "test");
                    assertEquals(conf.getString(SITE.getKey()), "test");
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
        assertEquals(conf.getString(VERSION.getKey()), "test");

        // Classpath does not exist. (And should not load from file path.)
        assertFalse(ConfigurationType.get("build.gradle", null).isPresent());

        // File exists.
        Path path = Paths.get(Resources.getResource(site).toURI()).getParent();
        conf = ConfigurationType.get(site, path.toAbsolutePath().toString()).get();
        assertEquals(conf.getString(VERSION.getKey()), "test");

        // File does not exist. (And should not load from classpath.)
        assertFalse(ConfigurationType.get(site, "build").isPresent());

    }

}
