package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.singletonMap;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class BaseService {

    static final Type TYPE_MAP = new TypeToken<Map<String, String>>() {
    }.getType();

    static final String PRODUCT_CODE = "product_code";

    static final String FROM_DATE = "from_date";

    final Logger log = LoggerFactory.getLogger(getClass());

    final HttpClient client;

    final Gson gson;

    BaseService(Injector injector) {

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

}
