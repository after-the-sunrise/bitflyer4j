package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.DataException;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
class HttpService {

    static final Type TYPE_MAP = new TypeToken<Map<String, String>>() {
    }.getType();

    static final String EMPTY = "{}";

    final Logger log = LoggerFactory.getLogger(getClass());

    final HttpClient client;

    final Gson gson;

    HttpService(Injector injector) {

        client = injector.getInstance(HttpClient.class);

        gson = injector.getInstance(Gson.class);

        log.debug("Initialized.");

    }

    Map<String, String> prepareParameter(Object o) {

        if (o == null) {
            return null;
        }

        String json = gson.toJson(o);

        Map<String, String> map = gson.fromJson(json, TYPE_MAP);

        if (MapUtils.isEmpty(map)) {
            return null;
        }

        return map;

    }

    <T> CompletableFuture<T> request(HttpClient.HttpRequest request, Class<? extends T> clazz) {
        return request(request, (Type) clazz);
    }

    <T> CompletableFuture<T> request(HttpClient.HttpRequest request, Type type) {

        CompletableFuture<HttpClient.HttpResponse> future = client.request(request);

        return future.thenApply(response -> {

            int code = response.getCode();

            String text = response.getMessage();

            String body = StringUtils.defaultIfEmpty(response.getBody(), EMPTY);

            try {

                if (code != HttpURLConnection.HTTP_OK) {

                    Map<String, String> details = gson.fromJson(body, TYPE_MAP);

                    throw new RejectException(code, text, details);

                }

                return gson.fromJson(body, type);

            } catch (RejectException e) {

                throw e;

            } catch (RuntimeException e) {

                throw new DataException(code, text, body, e);

            }

        });

    }

}
