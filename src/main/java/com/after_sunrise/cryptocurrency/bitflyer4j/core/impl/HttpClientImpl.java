package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.KeyType.*;
import static java.lang.Integer.parseInt;
import static java.net.Proxy.Type.HTTP;
import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class HttpClientImpl implements HttpClient {

    private static final String METHOD_GET = "GET";

    private static final String METHOD_POST = "POST";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String endpoint;

    private final String proxyHost;

    private final String proxyPort;

    private final String timeout;

    private final ExecutorService executor;

    @Inject
    public HttpClientImpl(Injector injector) {

        Configuration conf = injector.getInstance(Configuration.class);

        endpoint = HTTP_URL.apply(conf);

        proxyHost = HTTP_PROXY_HOST.apply(conf);

        proxyPort = HTTP_PROXY_PORT.apply(conf);

        timeout = HTTP_TIMEOUT.apply(conf);

        executor = injector.getInstance(ExecutorFactory.class).get(getClass());

    }

    @Override
    public CompletableFuture<HttpResponse> request(HttpRequest request) {

        return CompletableFuture.completedFuture(request).thenComposeAsync(req -> {

            CompletableFuture<HttpResponse> f = new CompletableFuture<>();

            try {

                log.debug("SEND : {}", req);

                HttpURLConnection connection = create(req);

                try {

                    HttpResponse response = parse(connection);

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
    HttpURLConnection create(HttpRequest request) throws IOException {

        URL url = createUrl(request.getType().getPath(), request.getParameters());

        Proxy proxy = createProxy();

        HttpURLConnection conn;

        if (proxy == null) {
            conn = (HttpURLConnection) url.openConnection();
        } else {
            conn = (HttpURLConnection) url.openConnection(proxy);
        }

        try {

            if (timeout != null) {

                conn.setConnectTimeout(parseInt(timeout));

                conn.setReadTimeout(parseInt(timeout));

            }

            if (request.getType().isSign()) {
                // TODO : Sign credentials
            }

            conn.setRequestMethod(request.getType().getMethod().get());

            conn.connect();

            if (request.getBody() != null) {

                InputStream in = new ByteArrayInputStream(request.getBody().getBytes());

                ByteStreams.copy(in, conn.getOutputStream());

            }

        } catch (IOException e) {

            conn.disconnect();

            throw e;

        }

        return conn;

    }


    @VisibleForTesting
    URL createUrl(String path, Map<String, String> parameters) throws MalformedURLException {

        StringBuilder sb = new StringBuilder(endpoint);

        sb.append(path);

        if (parameters != null) {

            int length = sb.length();

            parameters.entrySet().forEach(entry -> {
                sb.append(sb.length() == length ? '?' : '&');
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue());
            });

        }

        return new URL(sb.toString());

    }

    @VisibleForTesting
    Proxy createProxy() {

        if (proxyHost == null || proxyPort == null) {
            return null;
        }

        SocketAddress sa = new InetSocketAddress(proxyHost, parseInt(proxyPort));

        return new Proxy(HTTP, sa);

    }

    private static class HttpResponseImpl implements HttpResponse {

        private final int code;

        private final String message;

        private final String body;

        private HttpResponseImpl(int code, String message, String body) {
            this.code = code;
            this.message = message;
            this.body = body;
        }

        @Override
        public String toString() {
            return reflectionToString(this, SHORT_PREFIX_STYLE);
        }

        @Override
        public int getCode() {
            return code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public String getBody() {
            return body;
        }

    }

    @VisibleForTesting
    HttpResponse parse(HttpURLConnection connection) throws IOException {

        try (InputStream in = connection.getInputStream()) {

            int code = connection.getResponseCode();

            String message = connection.getResponseMessage();

            byte[] bytes = ByteStreams.toByteArray(in);

            String body = new String(bytes, StandardCharsets.UTF_8);

            return new HttpResponseImpl(code, message, body);

        }

    }

}
