package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest.HttpRequestBuilder;
import org.testng.annotations.Test;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType.HEALTH;
import static java.util.Collections.singletonMap;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class HttpClientTest {

    @Test
    public void testBuilder() throws Exception {

        HttpRequestBuilder b = HttpRequest.builder();
        assertEquals(b.toString(), "HttpClient.HttpRequest.HttpRequestBuilder(type=null, parameters=null, body=null)");

        HttpRequest p0 = b.build();

        assertNull(p0.getType());
        assertNull(p0.getParameters());
        assertNull(p0.getBody());

        assertTrue(p0.equals(p0));
        assertTrue(p0.equals(b.build()));
        assertFalse(p0.equals(null));
        assertFalse(p0.equals(new Object()));
        assertFalse(p0.equals(new String()));

        assertEquals(b.build().hashCode(), p0.hashCode());
        assertEquals(b.build().toString(), p0.toString());

        HttpRequest p1 = b.type(HEALTH).parameters(singletonMap("foo", "bar")).body("hoge").build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.type(HEALTH).parameters(null).body(null).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.type(null).parameters(singletonMap("foo", "bar")).body(null).build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

        p1 = b.type(null).parameters(null).body("hoge").build();
        assertFalse(p0.equals(p1));
        assertFalse(p1.equals(p0));
        assertEquals(b.build().hashCode(), p1.hashCode());

    }

}