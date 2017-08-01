package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.common.io.Resources;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.testng.annotations.Test;

import java.net.URL;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.ConfigurationType.SITE;
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

        URL url = Resources.getResource(SITE.getPath());

        Configuration conf = new Configurations().properties(url);

        // Null input.  (= Default value.)
        assertEquals(VERSION.apply(null), VERSION.getDefaultValue());

        // Retrieved from properties.
        assertEquals(VERSION.apply(conf), "test");

        // Not found. (= Default value.)
        assertEquals(PUBNUB_KEY.apply(conf), PUBNUB_KEY.getDefaultValue());

    }


}