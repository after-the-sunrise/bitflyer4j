package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.TestModule;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpRequest;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient.HttpResponse;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Throttler;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType.*;
import static com.google.common.io.ByteStreams.toByteArray;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static java.net.HttpURLConnection.HTTP_ACCEPTED;
import static java.net.Proxy.Type.HTTP;
import static org.jboss.resteasy.test.TestPortProvider.getPort;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class HttpClientImplTest {

    private static UndertowJaxrsServer server;

    private static Gson gson;

    public static class TestApp extends Application {
        @Override
        public Set<Class<?>> getClasses() {
            return Sets.newHashSet(GetResource.class, PostResource.class);
        }
    }

    @Path("/v1/gethealth")
    public static class GetResource {
        @GET
        @Produces("application/json")
        public String get() {
            return "{\"status\": \"NORMAL\"}";
        }
    }

    @Path("/v1/me/{method}")
    public static class PostResource {
        @POST
        @Produces("application/json")
        public String post(@Context HttpServletRequest req, //
                           @Context HttpServletResponse res) throws IOException {

            Map<String, String> result = new HashMap<>();

            Enumeration<String> names = req.getHeaderNames();

            while (names.hasMoreElements()) {

                String name = names.nextElement();

                result.put("R:" + name, req.getHeader(name));

            }

            result.put("_", new String(toByteArray(req.getInputStream())));

            return gson.toJson(result);

        }

    }

    @BeforeClass
    public static void init() {

        server = new UndertowJaxrsServer().start();

        server.deploy(TestApp.class);

        gson = new GsonBuilder().create();

    }

    @AfterClass
    public static void terminate() {
        server.stop();
    }

    private HttpClientImpl target;

    private TestModule module;

    @BeforeMethod
    public void setUp() throws Exception {

        module = new TestModule();
        when(module.getEnvironment().getNow()).thenReturn(Instant.ofEpochMilli(1234567890987L));
        when(module.getEnvironment().getUrl()).thenReturn("http://localhost:" + getPort());
        when(module.getEnvironment().getAuthKey()).thenReturn("testkey");
        when(module.getEnvironment().getAuthSecret()).thenReturn("testsecret");
        when(module.getMock(ExecutorFactory.class).get(any(Class.class))).thenReturn(newDirectExecutorService());

        target = new HttpClientImpl(module.createInjector());

    }

    @Test
    public void testRequest_Get() throws Exception {

        HttpRequest request = HttpRequest.builder() //
                .type(HEALTH) //
                .parameters(null) //
                .body(null) //
                .build();

        HttpResponse response = target.request(request).get();

        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), "OK");
        assertEquals(response.getBody(), "{\"status\": \"NORMAL\"}");

    }

    @Test
    public void testRequest_Error() throws Exception {

        HttpRequest request = HttpRequest.builder() //
                .type(CHAT) //
                .parameters(null) //
                .body(null) //
                .build();

        CompletableFuture<HttpResponse> response = target.request(request);

        assertTrue(response.isCompletedExceptionally());

    }

    @Test
    public void testRequest_Post() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("foo", "bar");
        params.put("hoge", null);
        params.put(null, "hoge");
        params.put("zoo", "zoo");

        HttpRequest request = HttpRequest.builder() //
                .type(WITHDRAW) //
                .parameters(params) //
                .body("Lorem Ipsum") //
                .build();

        HttpResponse response = target.request(request).get();

        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), "OK");

        Map<String, String> result = gson.fromJson(response.getBody(), new TypeToken<Map<String, String>>() {
        }.getType());
        assertEquals(result.get("R:ACCESS-KEY"), "testkey");
        assertEquals(result.get("R:ACCESS-TIMESTAMP"), "1234567890");
        assertEquals(result.get("R:ACCESS-SIGN"), "043bde57184e77f196025f02941c90cdc7c56cdc0f58dda1f6b6ea45e615d921");
        assertEquals(result.get("_"), "Lorem Ipsum");

    }

    @Test
    public void testThrottle() throws Exception {

        target.throttle(HEALTH);

        verify(module.getMock(Throttler.class)).throttleAddress();

        verify(module.getMock(Throttler.class), never()).throttlePrivate();

    }

    @Test
    public void testThrottle_Signed() throws Exception {

        target.throttle(BALANCE);

        verify(module.getMock(Throttler.class)).throttleAddress();

        verify(module.getMock(Throttler.class)).throttlePrivate();

    }

    @Test
    public void testCreateConnection() throws Exception {

        when(module.getMock(Environment.class).getProxy()).thenReturn(null);

        HttpRequest request = HttpRequest.builder().type(HEALTH).build();

        HttpURLConnection conn = target.createConnection(request);

        try {

            conn.connect();

            assertFalse(conn.usingProxy());

        } finally {
            conn.disconnect();
        }

    }

    @Test
    public void testCreateConnection_Proxy() throws Exception {

        Proxy proxy = new Proxy(HTTP, new InetSocketAddress("localhost", getPort()));

        when(module.getMock(Environment.class).getProxy()).thenReturn(proxy);

        HttpRequest request = HttpRequest.builder().type(HEALTH).build();

        HttpURLConnection conn = target.createConnection(request);

        try {

            conn.connect();

            assertTrue(conn.usingProxy());

        } finally {
            conn.disconnect();
        }

    }


    @Test
    public void testCreateUrl() throws Exception {

        URL url = target.createUrl("/foo/bar", null);

        assertEquals(url.toString(), "http://localhost:8081/foo/bar");

    }

    @Test
    public void testCreateUrl_WithParameters() throws Exception {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("foo", "bar");
        params.put("hoge", null);
        params.put(null, "hoge");
        params.put("zoo", "zoo");

        URL url = target.createUrl("/foo/bar", params);

        assertEquals(url.toString(), "http://localhost:8081/foo/bar?foo=bar&zoo=zoo");

    }

    @Test
    public void testConfigure() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);

        HttpRequest request = HttpRequest.builder().type(HEALTH).build();

        conn = target.configure(conn, request);

        // Method
        verify(conn).setRequestMethod(request.getType().getMethod().name());

        // Timeout
        verify(conn, never()).setConnectTimeout(anyInt());
        verify(conn, never()).setReadTimeout(anyInt());

        // Request Property
        verify(conn, never()).setRequestProperty(anyString(), anyString());

        // Body
        verify(conn, never()).getOutputStream();
        verify(conn).connect();

    }

    @Test
    public void testComputeHash() throws Exception {

        String hash = target.computeHash("HmacSHA256", "foo");

        assertEquals(hash, "dc25fe75c43656c2a96a63801e089aec84d8fa4fba77ae7a24a52950b9779af9");

    }

    @Test(expectedExceptions = IOException.class)
    public void testComputeHash_InvalidAlgo() throws Exception {

        target.computeHash("MyHashAlgo", "foo");

    }

    @Test
    public void testReceive() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);

        when(conn.getInputStream()).thenReturn(new ByteArrayInputStream("hoge".getBytes()));
        when(conn.getResponseCode()).thenReturn(HTTP_ACCEPTED);
        when(conn.getResponseMessage()).thenReturn("ACCEPTED");

        HttpResponse response = target.receive(conn);

        assertEquals(response.getCode(), HTTP_ACCEPTED);
        assertEquals(response.getMessage(), "ACCEPTED");
        assertEquals(response.getBody(), "hoge");

    }

    @Test(expectedExceptions = IOException.class)
    public void testReceive_Failure() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);

        when(conn.getInputStream()).thenThrow(new IOException("test"));

        target.receive(conn);

    }

}