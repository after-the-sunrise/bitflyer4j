package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.inject.Provider;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public class GsonProvider implements Provider<Gson> {

    private static final DateTimeFormatter DTF = ISO_LOCAL_DATE_TIME.withZone(ZoneId.of("GMT"));

    @Override
    public Gson get() {

        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (j, t, c) -> {

            if (j.isJsonNull()) {
                return null;
            }

            String value = j.getAsString();

            try {

                return ZonedDateTime.parse(value, DTF);

            } catch (DateTimeParseException e) {

                String msg = "Invalid date/time : " + value;

                throw new JsonParseException(msg, e);

            }

        });

        return builder.create();

    }

}
