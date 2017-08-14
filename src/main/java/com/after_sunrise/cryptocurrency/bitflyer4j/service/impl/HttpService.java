package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.RejectException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonMap;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
class HttpService {

    static final Type TYPE_MAP = new TypeToken<Map<String, String>>() {
    }.getType();

    static final String PRODUCT_CODE = "product_code";

    static final String FROM_DATE = "from_date";

    static final String EMPTY = "{}";

    final Logger log = LoggerFactory.getLogger(getClass());

    final HttpClient client;

    final Gson gson;

    HttpService(Injector injector) {

        client = injector.getInstance(HttpClient.class);

        gson = injector.getInstance(Gson.class);

        log.debug("Initialized.");

    }

    Map<String, String> prepareParameter(String key, Object value) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        String v = Objects.toString(value, null);

        if (StringUtils.isEmpty(v)) {
            return null;
        }

        return singletonMap(key, v);

    }

    Map<String, String> prepareParameter(Object o) {
        return prepareParameter((Map<String, String>) null, o);
    }

    Map<String, String> prepareParameter(Map<String, String> base, Object o) {

        if (o == null) {
            return base;
        }

        String json = gson.toJson(o);

        Map<String, String> map = gson.fromJson(json, TYPE_MAP);

        if (MapUtils.isEmpty(map)) {
            return base;
        }

        if (MapUtils.isEmpty(base)) {
            return map;
        }

        Map<String, String> merged = new HashMap<>();

        merged.putAll(base);

        merged.putAll(map);

        return merged;

    }

    <T> CompletableFuture<T> request(HttpClient.HttpRequest request, Class<? extends T> clazz) {
        return request(request, (Type) clazz);
    }

    <T> CompletableFuture<T> request(HttpClient.HttpRequest request, Type type) {

        CompletableFuture<HttpClient.HttpResponse> future = client.request(request);

        return future.thenApply(response -> {

            String body = StringUtils.defaultIfEmpty(response.getBody(), EMPTY);

            if (response.getCode() != HttpURLConnection.HTTP_OK) {

                Map<String, String> details = gson.fromJson(body, TYPE_MAP);

                throw new RejectException(response.getCode(), response.getMessage(), details);

            }

            return gson.fromJson(body, type);

        });

    }

}
