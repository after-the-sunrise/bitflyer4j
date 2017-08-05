package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.Loggers.HttpLogger;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class HttpClientImpl implements HttpClient {

    private static final long TIME_PRECISION = 1000L;

    static final String ALGORITHM = "HmacSHA256";

    static final String ACCESS_KEY = "ACCESS-KEY";

    static final String ACCESS_TIME = "ACCESS-TIMESTAMP";

    static final String ACCESS_SIGN = "ACCESS-SIGN";

    private final Logger clientLog = LoggerFactory.getLogger(HttpLogger.class);

    private final Environment environment;

    private final Throttler throttler;

    private final ExecutorService executor;

    @Inject
    public HttpClientImpl(Injector injector) {

        environment = injector.getInstance(Environment.class);

        throttler = injector.getInstance(Throttler.class);

        executor = injector.getInstance(ExecutorFactory.class).get(getClass());

    }

    @Override
    public CompletableFuture<HttpResponse> request(HttpRequest request) {

        return CompletableFuture.completedFuture(request).thenComposeAsync(req -> {

            throttle(req.getType());

            CompletableFuture<HttpResponse> f = new CompletableFuture<>();

            try {

                HttpURLConnection connection = createConnection(req);

                try {

                    connection = configure(connection, req);

                    HttpResponse response = receive(connection);

                    f.complete(response);

                } finally {
                    connection.disconnect();
                }

            } catch (IOException e) {

                clientLog.trace("FAIL : {}", e.getMessage());

                f.completeExceptionally(e);

            }

            return f;

        }, executor);

    }

    @VisibleForTesting
    void throttle(PathType type) {

        throttler.throttleAddress();

        if (!type.isSign()) {
            return;
        }

        throttler.throttlePrivate();

    }

    @VisibleForTesting
    HttpURLConnection createConnection(HttpRequest request) throws IOException {

        log.trace("Creating connection : {}", request);

        URL url = createUrl(request.getType().getPath(), request.getParameters());

        Proxy proxy = environment.getProxy();

        HttpURLConnection conn;

        if (proxy == null) {
            conn = (HttpURLConnection) url.openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection(proxy);
        }

        log.trace("Created connection : [{}] [{}]", url, proxy);

        return conn;

    }

    @VisibleForTesting
    URL createUrl(String path, Map<String, String> parameters) throws MalformedURLException {

        String endpoint = environment.getUrl();

        StringBuilder sb = new StringBuilder(endpoint);

        sb.append(path);

        if (MapUtils.isNotEmpty(parameters)) {

            int length = sb.length();

            parameters.entrySet().stream() //
                    .filter(entry -> StringUtils.isNotEmpty(entry.getKey())) //
                    .filter(entry -> StringUtils.isNotEmpty(entry.getValue())) //
                    .forEach(entry -> {
                        sb.append(sb.length() == length ? '?' : '&');
                        sb.append(entry.getKey());
                        sb.append('=');
                        sb.append(entry.getValue());
                    });

        }

        return new URL(sb.toString());

    }

    @VisibleForTesting
    HttpURLConnection configure(HttpURLConnection conn, HttpRequest request) throws IOException {

        {

            MethodType method = request.getType().getMethod();

            conn.setRequestMethod(method.get());

            log.trace("Configured method : {}", method);

        }

        Duration timeout = environment.getTimeout();

        if (timeout != null) {

            conn.setConnectTimeout((int) timeout.toMillis());

            conn.setReadTimeout((int) timeout.toMillis());

            log.trace("Configured timeouts : {} ms", timeout);

        }

        String body = request.getBody();

        if (request.getType().isSign()) {

            long now = environment.getNow().toEpochMilli();

            String ts = String.valueOf(now / TIME_PRECISION);

            String base = ts //
                    + conn.getRequestMethod() //
                    + conn.getURL().getFile() //
                    + StringUtils.trimToEmpty(body);

            String sign = computeHash(ALGORITHM, base);

            String authKey = environment.getAuthKey();

            conn.addRequestProperty(ACCESS_KEY, authKey);
            conn.addRequestProperty(ACCESS_TIME, ts);
            conn.addRequestProperty(ACCESS_SIGN, sign);

            log.trace("Configured signature : key=[{}] time=[{}] sign=[{}]", authKey, ts, sign);

        }

        Map<String, List<String>> properties = conn.getRequestProperties();

        if (StringUtils.isNotEmpty(body)) {

            conn.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());

            ByteStreams.copy(new ByteArrayInputStream(body.getBytes()), out);

            out.flush();

            out.close();

            log.trace("Configured body : [{}]", body);

            clientLog.trace("SEND : [{}] [{}] [{}]", conn.getURL(), properties, body);

        } else {

            conn.connect();

            log.trace("Configured connect.");

            clientLog.trace("SEND : [{}] [{}]", conn.getURL(), properties);

        }

        return conn;

    }

    @VisibleForTesting
    String computeHash(String algorithm, String base) throws IOException {

        try {

            Mac mac = Mac.getInstance(algorithm);

            String authSecret = environment.getAuthSecret();

            mac.init(new SecretKeySpec(authSecret.getBytes(), ALGORITHM));

            byte[] hash = mac.doFinal(base.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }

            log.trace("Computed hash : [{}] -> [{}]", base, sb);

            return sb.toString();

        } catch (GeneralSecurityException e) {

            throw new IOException("Failed to compute hash.", e);

        }

    }

    @Value
    private static class HttpResponseImpl implements HttpResponse {

        private final int code;

        private final String message;

        private final String body;

    }

    @VisibleForTesting
    HttpResponse receive(HttpURLConnection connection) throws IOException {

        int code = connection.getResponseCode();

        String message = connection.getResponseMessage();

        try (InputStream in = connection.getInputStream()) {

            byte[] bytes = ByteStreams.toByteArray(in);

            String body = new String(bytes, StandardCharsets.UTF_8);

            clientLog.trace("RECV : [{} {}] [{}]", code, message, body);

            log.trace("Received : Headers=[{}] Body=[{}]", connection.getHeaderFields(), body);

            return new HttpResponseImpl(code, message, body);

        } catch (IOException e) {

            throw new IOException(String.format("%s %s", code, message), e);

        }

    }

}
