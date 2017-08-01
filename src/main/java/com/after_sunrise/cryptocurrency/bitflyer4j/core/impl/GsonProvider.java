package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
@Slf4j
public class GsonProvider implements Provider<Gson> {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    private final Map<Type, Map<String, Enum<?>>> enums = new ConcurrentHashMap<>();

    @Override
    public Gson get() {

        GsonBuilder builder = new GsonBuilder();

        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getAnnotation(SerializedName.class) == null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });

        builder.registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (j, t, c) -> {

            if (j.isJsonNull()) {
                return null;
            }

            // cf : "2017-01-23T12:34:56.789"
            String value = j.getAsString();

            // Handle "Z" at the end : "2017-01-23T12:34:56.7890123Z"
            String trimmed = StringUtils.removeEnd(value, "Z");

            try {

                return ZonedDateTime.parse(trimmed, DTF);

            } catch (DateTimeParseException e) {

                String msg = "Invalid date/time : " + value;

                throw new JsonParseException(msg, e);

            }

        });

        builder.registerTypeHierarchyAdapter(Enum.class, (JsonDeserializer<Enum<?>>) (j, t, c) -> {

            if (j.isJsonNull()) {
                return null;
            }

            String value = j.getAsString();

            value = StringUtils.replaceChars(value, ' ', '_');

            value = value.toUpperCase();

            Map<String, Enum<?>> cache = enums.computeIfAbsent(t, type -> {

                Map<String, Enum<?>> map = new LinkedHashMap<>();

                for (Object o : ((Class<?>) type).getEnumConstants()) {

                    Enum<?> element = (Enum<?>) o;

                    map.put(element.name(), element);

                }

                log.trace("Cached constants : {}", type);

                return map;

            });

            Enum<?> element = cache.get(value);

            if (element == null) {

                String name = ((Class<?>) t).getSimpleName();

                String raw = j.getAsString();

                log.warn("Skipping deserialization for enum {} : {}", name, raw);

            }

            return (Enum<?>) element;

        });

        Gson gson = builder.create();

        log.debug("Created Gson instance.");

        return gson;

    }

}
