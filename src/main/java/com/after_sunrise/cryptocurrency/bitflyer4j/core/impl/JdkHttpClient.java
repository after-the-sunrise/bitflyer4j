package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class JdkHttpClient implements HttpClient {

    private static final int CODE_OK = 200;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final ExecutorService executor;

    @Inject
    public JdkHttpClient(Injector injector) {
        executor = injector.getInstance(ExecutorService.class);
    }

    @Override
    public CompletableFuture<String> get(String... paths) {
        return compose(paths).thenComposeAsync(this::get, executor);
    }

    @VisibleForTesting
    CompletableFuture<URL> compose(String... paths) {
        return CompletableFuture.completedFuture(paths).thenComposeAsync(p -> {

            CompletableFuture<URL> f = new CompletableFuture<>();

            try {
                f.complete(new URL(StringUtils.join(p)));
            } catch (MalformedURLException e) {
                f.completeExceptionally(e);
            }

            return f;

        }, executor);
    }

    @VisibleForTesting
    CompletableFuture<String> get(URL url) {

        log.debug("SEND : {}", url);

        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try (InputStream input = conn.getInputStream(); //
                 InputStreamReader reader = new InputStreamReader(input); //
                 BufferedReader br = new BufferedReader(reader)) {

                int code = conn.getResponseCode();

                if (CODE_OK != code) {

                    String msg = code + " : " + conn.getResponseMessage();

                    log.debug("RECV : ", msg);

                    throw new IOException(msg);

                }

                StringBuilder sb = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                log.debug("RECV : {}", sb);

                return CompletableFuture.completedFuture(sb.toString());

            } finally {
                conn.disconnect();
            }


        } catch (IOException e) {

            CompletableFuture<String> f = new CompletableFuture<>();

            f.completeExceptionally(e);

            return f;

        }

    }

}
