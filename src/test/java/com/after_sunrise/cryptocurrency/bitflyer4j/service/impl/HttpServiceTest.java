package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.GsonProvider;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.DataException;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.RejectException;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.google.common.io.Resources.getResource;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.6
 */
public class HttpServiceTest {

    private static class TestEntity {

        @SerializedName("foo")
        String foo;

        @SerializedName("bar")
        String bar;

    }

    private HttpService target;

    private TestModule module;


    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();

        module.putMock(Gson.class, new GsonProvider().get());

        target = new HttpService(module.createInjector());

    }

    @Test
    public void testPrepareParameter_Object() throws Exception {

        TestEntity entity = null;
        Map<String, String> result = target.prepareParameter(entity);
        assertNull(result);

        entity = new TestEntity();
        result = target.prepareParameter(entity);
        assertNull(result);

        entity.foo = "";
        entity.bar = null;
        result = target.prepareParameter(entity);
        assertEquals(result.remove("foo"), "");
        assertTrue(result.isEmpty());

        entity.foo = null;
        entity.bar = "";
        result = target.prepareParameter(entity);
        assertEquals(result.remove("bar"), "");
        assertTrue(result.isEmpty());

        entity.foo = "f";
        entity.bar = null;
        result = target.prepareParameter(entity);
        assertEquals(result.remove("foo"), "f");
        assertTrue(result.isEmpty());

        entity.foo = null;
        entity.bar = "b";
        result = target.prepareParameter(entity);
        assertEquals(result.remove("bar"), "b");
        assertTrue(result.isEmpty());

        entity.foo = "f";
        entity.bar = "b";
        result = target.prepareParameter(entity);
        assertEquals(result.remove("foo"), "f");
        assertEquals(result.remove("bar"), "b");
        assertTrue(result.isEmpty());

    }

    @Test
    public void testRequest() throws Exception {

        HttpClient client = module.getMock(HttpClient.class);

        when(client.request(any(HttpRequest.class))).thenAnswer(i -> {
            HttpResponse response = mock(HttpResponse.class);
            when(response.getCode()).thenReturn(HttpURLConnection.HTTP_OK);
            when(response.getMessage()).thenReturn("OK");
            return CompletableFuture.completedFuture(response);
        });

        TestEntity entity = target.request(HttpRequest.builder().build(), TestEntity.class).get();
        assertNotNull(entity);

    }

    @Test
    public void testRequest_Reject_Json() throws Exception {

        HttpClient client = module.getMock(HttpClient.class);

        when(client.request(any(HttpRequest.class))).thenAnswer(i -> {
            String path = "json/REJECT.json";
            HttpResponse response = mock(HttpResponse.class);
            when(response.getCode()).thenReturn(HTTP_BAD_REQUEST);
            when(response.getMessage()).thenReturn("Bad Request");
            when(response.getBody()).thenReturn(Resources.toString(getResource(path), UTF_8));
            return CompletableFuture.completedFuture(response);
        });

        try {

            target.request(HttpRequest.builder().build(), TestEntity.class).get();

            fail();

        } catch (Exception e) {

            RejectException re = (RejectException) e.getCause();
            assertEquals(re.getMessage(), "400 Bad Request");
            assertEquals(re.getCode(), HTTP_BAD_REQUEST);
            assertEquals(re.getText(), "Bad Request");
            assertEquals(re.getDetails().size(), 3, re.getDetails().toString());
            assertEquals(re.getDetails().get("status"), "-500");
            assertEquals(re.getDetails().get("error_message"), "Permission denied");
            assertEquals(re.getDetails().get("data"), null);
            assertEquals(re.getCause(), null);

        }

    }

    @Test
    public void testRequest_Reject_Html() throws Exception {

        HttpClient client = module.getMock(HttpClient.class);

        when(client.request(any(HttpRequest.class))).thenAnswer(i -> {
            HttpResponse response = mock(HttpResponse.class);
            when(response.getCode()).thenReturn(HTTP_BAD_REQUEST);
            when(response.getMessage()).thenReturn("Bad Request");
            when(response.getBody()).thenReturn("<html>some html message</html>");
            return CompletableFuture.completedFuture(response);
        });

        try {

            target.request(HttpRequest.builder().build(), TestEntity.class).get();

            fail();

        } catch (Exception e) {

            DataException re = (DataException) e.getCause();
            assertEquals(re.getMessage(), "400 Bad Request");
            assertEquals(re.getCode(), HTTP_BAD_REQUEST);
            assertEquals(re.getText(), "Bad Request");
            assertEquals(re.getData(), "<html>some html message</html>");
            assertEquals(re.getCause().getMessage(), "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 1 path $");

        }

    }

}
