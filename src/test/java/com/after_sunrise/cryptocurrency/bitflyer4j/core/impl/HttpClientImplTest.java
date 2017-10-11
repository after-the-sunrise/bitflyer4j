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
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType.*;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.HttpClientImpl.*;
import static com.google.common.io.ByteStreams.toByteArray;
import static com.google.common.util.concurrent.MoreExecutors.newDirectExecutorService;
import static java.net.HttpURLConnection.*;
import static java.net.Proxy.Type.HTTP;
import static java.util.Collections.singletonMap;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.jboss.resteasy.test.TestPortProvider.getPort;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class HttpClientImplTest {

    private static final Type TYPE_MAP = new TypeToken<Map<String, String>>() {
    }.getType();

    private static final Set<String> IGNORE_HEADERS = Sets.newHashSet("Host", "Connection", "Content-Length");

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
        public String get(@Context HttpServletRequest req) {

            Map<String, String> result = new HashMap<>();

            Enumeration<String> names = req.getHeaderNames();

            while (names.hasMoreElements()) {

                String name = names.nextElement();

                if (IGNORE_HEADERS.contains(name)) {
                    continue;
                }

                result.put("R:" + name, req.getHeader(name));

            }

            result.put("status", "NORMAL");

            return gson.toJson(result);

        }
    }

    @Path("/v1/me/{method}")
    public static class PostResource {
        @POST
        @Produces("application/json")
        public Object post(@Context HttpServletRequest req, //
                           @Context HttpServletResponse res, //
                           @PathParam("method") String method) throws IOException {

            Map<String, String> result = new HashMap<>();

            Enumeration<String> names = req.getHeaderNames();

            while (names.hasMoreElements()) {

                String name = names.nextElement();

                if (IGNORE_HEADERS.contains(name)) {
                    continue;
                }

                result.put("R:" + name, req.getHeader(name));

            }

            Response.ResponseBuilder builder;

            if ("cancelallchildorders".equals(method)) {

                builder = Response.status(BAD_REQUEST);

                result.put("status", "-500");

            } else {

                builder = Response.status(OK);

                result.put("_", new String(toByteArray(req.getInputStream())));

            }

            return builder.entity(gson.toJson(result)).build();

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
        when(module.getEnvironment().getVersion()).thenReturn("0.0.0");
        when(module.getEnvironment().getNow()).thenReturn(Instant.ofEpochMilli(1234567890987L));
        when(module.getEnvironment().getUrl()).thenReturn("http://localhost:" + getPort());
        when(module.getEnvironment().getAuthKey()).thenReturn("testkey");
        when(module.getEnvironment().getAuthSecret()).thenReturn("testsecret");
        when(module.getMock(ExecutorFactory.class).get(any(Class.class), anyInt())).thenReturn(newDirectExecutorService());

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

        Map<String, String> result = gson.fromJson(response.getBody(), TYPE_MAP);
        assertEquals(result.remove("R:User-Agent").split("/")[0], "bitflyer4j");
        assertEquals(result.remove("R:Accept"), "application/json");
        assertEquals(result.remove("R:Accept-Charset"), "UTF-8");
        assertEquals(result.remove("status"), "NORMAL");
        assertTrue(result.isEmpty(), result.toString());
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

        Map<String, String> result = gson.fromJson(response.getBody(), TYPE_MAP);
        assertEquals(result.remove("R:User-Agent"), "bitflyer4j/0.0.0");
        assertEquals(result.remove("R:Accept"), "application/json");
        assertEquals(result.remove("R:Accept-Charset"), "UTF-8");
        assertEquals(result.remove("R:Content-Type"), "application/json");
        assertEquals(result.remove("R:ACCESS-KEY"), "testkey");
        assertEquals(result.remove("R:ACCESS-TIMESTAMP"), "1234567890");
        assertEquals(result.remove("R:ACCESS-SIGN"), "043bde57184e77f196025f02941c90cdc7c56cdc0f58dda1f6b6ea45e615d921");
        assertEquals(result.remove("_"), "Lorem Ipsum");
        assertTrue(result.isEmpty(), result.toString());

    }

    @Test
    public void testRequest_Post_EmptyBody() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("foo", "bar");
        params.put("hoge", null);
        params.put(null, "hoge");
        params.put("zoo", "zoo");

        HttpRequest request = HttpRequest.builder() //
                .type(WITHDRAW) //
                .parameters(params) //
                .body(null) //
                .build();

        HttpResponse response = target.request(request).get();

        assertEquals(response.getCode(), 200);
        assertEquals(response.getMessage(), "OK");

        Map<String, String> result = gson.fromJson(response.getBody(), TYPE_MAP);
        assertEquals(result.remove("R:User-Agent"), "bitflyer4j/0.0.0");
        assertEquals(result.remove("R:Accept"), "application/json");
        assertEquals(result.remove("R:Accept-Charset"), "UTF-8");
        assertEquals(result.remove("R:ACCESS-KEY"), "testkey");
        assertEquals(result.remove("R:ACCESS-TIMESTAMP"), "1234567890");
        assertEquals(result.remove("R:ACCESS-SIGN"), "f9a2636bc8f948bf996c01f824964bb8f882246353486bf4cb55a1583e07360e");
        assertEquals(result.remove("_"), "");
        assertTrue(result.isEmpty(), result.toString());

    }

    @Test
    public void testRequest_Post_Reject() throws Exception {

        HttpRequest request = HttpRequest.builder() //
                .type(PRODUCT_CANCEL) //
                .parameters(singletonMap("product_code", "JPY")) //
                .body(null) //
                .build();

        HttpResponse response = target.request(request).get();

        assertEquals(response.getCode(), HTTP_BAD_REQUEST);
        assertEquals(response.getMessage(), "Bad Request");

        Map<String, String> result = gson.fromJson(response.getBody(), TYPE_MAP);
        assertEquals(result.remove("R:User-Agent"), "bitflyer4j/0.0.0");
        assertEquals(result.remove("R:Accept"), "application/json");
        assertEquals(result.remove("R:Accept-Charset"), "UTF-8");
        assertEquals(result.remove("R:ACCESS-KEY"), "testkey");
        assertEquals(result.remove("R:ACCESS-TIMESTAMP"), "1234567890");
        assertEquals(result.remove("R:ACCESS-SIGN"), "75201cac930faeb2beec3928cef697504951e6b02323ea60ca8bf2b8e0806c85");
        assertEquals(result.remove("status"), "-500");
        assertTrue(result.isEmpty(), result.toString());

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
        params.put("z?o", "z&o");

        URL url = target.createUrl("/foo/bar", params);

        assertEquals(url.toString(), "http://localhost:8081/foo/bar?foo=bar&z%3Fo=z%26o");

    }

    @Test
    public void testConfigure_GET() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(module.getEnvironment().getTimeout()).thenReturn(null);

        HttpRequest request = HttpRequest.builder().type(HEALTH).build();
        conn = target.configure(conn, request);

        // Method
        verify(conn).setRequestMethod(request.getType().getMethod().name());

        // Timeout
        verify(conn, never()).setConnectTimeout(anyInt());
        verify(conn, never()).setReadTimeout(anyInt());

        // Request Property
        verify(conn).setRequestProperty("Accept", "application/json");
        verify(conn).setRequestProperty("Accept-Charset", "UTF-8");
        verify(conn).setRequestProperty("User-Agent", "bitflyer4j/0.0.0");
        verify(conn, times(3)).setRequestProperty(anyString(), anyString());

        // Body
        verify(conn, never()).getOutputStream();
        verify(conn).connect();

    }

    @Test
    public void testConfigure_POST() throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getRequestMethod()).thenReturn("HEAD");
        when(conn.getURL()).thenReturn(new URL("http://localhost:" + getPort() + "/foo/bar?a=b"));
        when(conn.getOutputStream()).thenReturn(out);
        when(module.getEnvironment().getTimeout()).thenReturn(Duration.ofMillis(9876));
        when(module.getEnvironment().getAuthKey()).thenReturn("testkey");
        when(module.getEnvironment().getAuthSecret()).thenReturn("testsecret");

        HttpRequest request = HttpRequest.builder().type(BALANCE).body("test").build();
        conn = target.configure(conn, request);

        // Method
        verify(conn).setRequestMethod(request.getType().getMethod().name());

        // Timeout
        verify(conn).setConnectTimeout(9876);
        verify(conn).setReadTimeout(9876);

        // Request Property
        verify(conn).setRequestProperty("Accept", "application/json");
        verify(conn).setRequestProperty("Accept-Charset", "UTF-8");
        verify(conn).setRequestProperty("User-Agent", "bitflyer4j/0.0.0");
        verify(conn).setRequestProperty("Content-Type", "application/json");
        verify(conn).setRequestProperty(ACCESS_KEY, "testkey");
        verify(conn).setRequestProperty(ACCESS_TIME, "1234567890");
        verify(conn).setRequestProperty(ACCESS_SIGN, "6e29ed1d31e7957c2f7277cc3d6dadab1f51e31759f392cb97e568fd42155f5f");
        verify(conn, times(7)).setRequestProperty(anyString(), anyString());

        // Body
        verify(conn, times(1)).getOutputStream();
        verify(conn, never()).connect();
        assertEquals(new String(out.toByteArray()), request.getBody());

    }

    @Test
    public void testConfigure_POST_EmptyBody() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getRequestMethod()).thenReturn("HEAD");
        when(conn.getURL()).thenReturn(new URL("http://localhost:" + getPort() + "/foo/bar?a=b"));
        when(module.getEnvironment().getTimeout()).thenReturn(Duration.ofMillis(9876));
        when(module.getEnvironment().getAuthKey()).thenReturn("testkey");
        when(module.getEnvironment().getAuthSecret()).thenReturn("testsecret");

        HttpRequest request = HttpRequest.builder().type(BALANCE).body(null).build();
        conn = target.configure(conn, request);

        // Method
        verify(conn).setRequestMethod(request.getType().getMethod().name());

        // Timeout
        verify(conn).setConnectTimeout(9876);
        verify(conn).setReadTimeout(9876);

        // Request Property
        verify(conn).setRequestProperty("Accept", "application/json");
        verify(conn).setRequestProperty("Accept-Charset", "UTF-8");
        verify(conn).setRequestProperty("User-Agent", "bitflyer4j/0.0.0");
        verify(conn).setRequestProperty(ACCESS_KEY, "testkey");
        verify(conn).setRequestProperty(ACCESS_TIME, "1234567890");
        verify(conn).setRequestProperty(ACCESS_SIGN, "3b6ad01b1d09a3e9430c931830961463df6ac3ca2cb47a842f4838e58ff83165");
        verify(conn, times(6)).setRequestProperty(anyString(), anyString());

        // Body
        verify(conn, times(1)).connect();

    }

    @Test
    public void testComputeHash() throws Exception {

        String hash = target.computeHash(ALGORITHM, "foo");

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
    public void testReceive_ReceiveFailure() throws Exception {

        HttpURLConnection conn = mock(HttpURLConnection.class);

        when(conn.getInputStream()).thenThrow(new IOException("test"));

        target.receive(conn);

    }

    @Test(expectedExceptions = IOException.class)
    public void testReceive_InputFailure() throws Exception {

        InputStream in = mock(InputStream.class);
        when(in.read(any(byte[].class))).thenThrow(new IOException("test"));

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getInputStream()).thenReturn(in);

        target.receive(conn);

    }

    @Test(expectedExceptions = IOException.class)
    public void testReceive_CloseFailure() throws Exception {

        InputStream in = mock(InputStream.class);
        when(in.read(any(byte[].class))).thenThrow(new IOException("testread"));
        doThrow(new IOException("testclose")).when(in).close();

        HttpURLConnection conn = mock(HttpURLConnection.class);
        when(conn.getInputStream()).thenReturn(in);

        target.receive(conn);

    }

    @Test
    public void testHttpResponse() {

        HttpResponseImpl i0 = new HttpResponseImpl(HTTP_OK, null, null);
        assertEquals(i0.toString(), "HttpClientImpl.HttpResponseImpl(code=200, message=null, body=null)");
        assertEquals(i0.hashCode(), i0.hashCode());
        assertEquals(i0.hashCode(), new HttpResponseImpl(HTTP_OK, null, null).hashCode());
        assertTrue(i0.equals(i0));
        assertTrue(i0.equals(new HttpResponseImpl(HTTP_OK, null, null)));
        assertFalse(i0.equals(null));
        assertFalse(i0.equals(""));

        HttpResponseImpl i1 = new HttpResponseImpl(HTTP_OK, "m", "b");
        assertEquals(i1.toString(), "HttpClientImpl.HttpResponseImpl(code=200, message=m, body=b)");
        assertEquals(i1.hashCode(), i1.hashCode());
        assertEquals(i1.hashCode(), new HttpResponseImpl(HTTP_OK, "m", "b").hashCode());
        assertTrue(i1.equals(i1));
        assertTrue(i1.equals(new HttpResponseImpl(HTTP_OK, "m", "b")));
        assertFalse(i1.equals(null));
        assertFalse(i1.equals(""));

        assertFalse(i0.equals(i1));
        assertFalse(i1.equals(i0));

    }

    @Test
    public void testCloseQuietly() throws IOException {

        target.closeQuietly(null);

        InputStream in = mock(InputStream.class);
        target.closeQuietly(in);
        verify(in, times(1)).close();

        doThrow(new IOException("test")).when(in).close();
        target.closeQuietly(in);
        verify(in, times(2)).close();

    }

}
