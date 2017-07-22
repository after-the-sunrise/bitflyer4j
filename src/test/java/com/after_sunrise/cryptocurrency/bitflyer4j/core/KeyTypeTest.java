package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.common.io.Resources;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.Test;

import java.net.URL;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.PUBNUB_KEY;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.VERSION;
import static org.testng.Assert.assertEquals;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class KeyTypeTest {

    @Test
    public void test() throws ConfigurationException {

        URL url = Resources.getResource("bitflyer4j-site.properties");

        Configuration conf = new PropertiesConfiguration(url);

        // Null input.  (= Default value.)
        assertEquals(VERSION.apply(null), VERSION.getDefault());

        // Retrieved from properties.
        assertEquals(VERSION.apply(conf), "test");

        // Not found. (= Default value.)
        assertEquals(PUBNUB_KEY.apply(conf), PUBNUB_KEY.getDefault());

    }


}