package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.Pagination;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Injector;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class BaseService {

    private static final String PAGE_COUNT = "count";

    private static final String PAGE_BEFORE = "before";

    private static final String PAGE_AFTER = "after";

    static final String PRODUCT_CODE = "product_code";

    static final String FROM_DATE = "from_date";

    static final Type TYPE_STRINGS = new TypeToken<List<String>>() {
    }.getType();

    final Logger log = LoggerFactory.getLogger(getClass());

    final HttpClient client;

    final Gson gson;

    BaseService(Injector injector) {

        client = injector.getInstance(HttpClient.class);

        gson = injector.getInstance(Gson.class);

        log.debug("Initialized.");

    }

    private String str(Object o) {
        return o == null ? null : o.toString();
    }

    Map<String, String> prepareParameter(Pagination pagination) {

        if (pagination == null) {
            return null;
        }

        Map<String, String> map = prepareParameter(PAGE_COUNT, str(pagination.getCount()));

        map = prepareParameter(map, PAGE_BEFORE, str(pagination.getBefore()));

        map = prepareParameter(map, PAGE_AFTER, str(pagination.getAfter()));

        return map;

    }

    Map<String, String> prepareParameter(String key, Object value) {
        return prepareParameter(null, key, value);
    }

    Map<String, String> prepareParameter(Map<String, String> base, String key, Object value) {

        if (StringUtils.isEmpty(key)) {
            return base;
        }

        String v = str(value);

        if (StringUtils.isEmpty(v)) {
            return base;
        }

        if (base == null) {
            return singletonMap(key, v);
        }

        Map<String, String> result = new HashMap<>(base);

        result.put(key, v);

        return result;

    }

}
