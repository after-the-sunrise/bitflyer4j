package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.Value;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.lang.Integer.parseInt;
import static java.net.Proxy.Type.HTTP;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class HttpClientImpl implements HttpClient {

    private static final long TIME_PRECISION = 1000L;

    private static final String ALGORITHM = "HmacSHA256";

    private static final String ACCESS_KEY = "ACCESS-KEY";

    private static final String ACCESS_TIME = "ACCESS-TIMESTAMP";

    private static final String ACCESS_SIGN = "ACCESS-SIGN";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Configuration conf;

    private final ExecutorService executor;

    @Inject
    public HttpClientImpl(Injector injector) {

        conf = injector.getInstance(Configuration.class);

        executor = injector.getInstance(ExecutorFactory.class).get(getClass());

    }

    @Override
    public CompletableFuture<HttpResponse> request(HttpRequest request) {

        return CompletableFuture.completedFuture(request).thenComposeAsync(req -> {

            CompletableFuture<HttpResponse> f = new CompletableFuture<>();

            try {

                log.debug("SEND : {}", req);

                HttpURLConnection connection = createConnection(req);

                try {

                    connection = configure(connection, req);

                    HttpResponse response = receive(connection);

                    log.debug("RECV : {}", response);

                    f.complete(response);

                } finally {
                    connection.disconnect();
                }

            } catch (IOException e) {

                log.debug("FAIL : {}", e.getMessage());

                f.completeExceptionally(e);

            }

            return f;

        }, executor);

    }

    @VisibleForTesting
    HttpURLConnection createConnection(HttpRequest request) throws IOException {

        URL url = createUrl(request.getType().getPath(), request.getParameters());

        String proxyHost = HTTP_PROXY_HOST.apply(conf);

        String proxyPort = HTTP_PROXY_PORT.apply(conf);

        HttpURLConnection conn;

        if (StringUtils.isNotEmpty(proxyHost) && StringUtils.isNotEmpty(proxyPort)) {

            SocketAddress sa = new InetSocketAddress(proxyHost, parseInt(proxyPort));

            conn = (HttpURLConnection) url.openConnection(new Proxy(HTTP, sa));

            log.trace("Created connection : URL=[{}], Proxy=[{}]", url, sa);

        } else {

            conn = (HttpURLConnection) url.openConnection();

            log.trace("Created connection : URL=[{}]", url);

        }

        return conn;

    }

    @VisibleForTesting
    URL createUrl(String path, Map<String, String> parameters) throws MalformedURLException {

        String endpoint = HTTP_URL.apply(conf);

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
    HttpURLConnection configure(HttpURLConnection connection, HttpRequest request) throws IOException {

        connection.setRequestMethod(request.getType().getMethod().get());

        String timeout = HTTP_TIMEOUT.apply(conf);

        if (timeout != null) {

            int millis = Integer.parseInt(timeout);

            connection.setConnectTimeout(millis);

            connection.setReadTimeout(millis);

            log.trace("Configured timeout : {}", millis);

        }

        if (request.getType().isSign()) {

            String ts = computeTimestamp();

            String base = ts //
                    + request.getType().getMethod().get() //
                    + request.getType().getPath() //
                    + StringUtils.trimToEmpty(request.getBody());

            String sign = computeHash(base);

            String authKey = AUTH_KEY.apply(conf);

            connection.addRequestProperty(ACCESS_KEY, authKey);
            connection.addRequestProperty(ACCESS_TIME, ts);
            connection.addRequestProperty(ACCESS_SIGN, sign);

            log.trace("Configured signature : key=[{}], base=[{}], sign=[{}]", authKey, base, sign);

        }

        if (StringUtils.isNotEmpty(request.getBody())) {

            connection.setDoOutput(true);

            InputStream in = new ByteArrayInputStream(request.getBody().getBytes());

            OutputStream out = new BufferedOutputStream(connection.getOutputStream());

            ByteStreams.copy(in, out);

            out.flush();

            log.trace("Configured body : {}", request.getBody());

        } else {

            connection.connect();

        }

        return connection;

    }

    @VisibleForTesting
    String computeTimestamp() {
        return String.valueOf(System.currentTimeMillis() / TIME_PRECISION);
    }

    @VisibleForTesting
    String computeHash(String base) throws IOException {

        byte[] hash;

        try {

            Mac mac = Mac.getInstance(ALGORITHM);

            String authSecret = AUTH_SECRET.apply(conf);

            mac.init(new SecretKeySpec(authSecret.getBytes(), ALGORITHM));

            hash = mac.doFinal(base.getBytes());

        } catch (GeneralSecurityException e) {

            throw new IOException(e);

        }

        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();

    }

    @Value
    private static class HttpResponseImpl implements HttpResponse {

        private final int code;

        private final String message;

        private final String body;

    }

    @VisibleForTesting
    HttpResponse receive(HttpURLConnection connection) throws IOException {

        try (InputStream in = connection.getInputStream()) {

            int code = connection.getResponseCode();

            String message = connection.getResponseMessage();

            byte[] bytes = ByteStreams.toByteArray(in);

            String body = new String(bytes, StandardCharsets.UTF_8);

            return new HttpResponseImpl(code, message, body);

        }

    }

}
