package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TimestampJsonAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

    private static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss.sss";

    private static final TimeZone ZONE = TimeZone.getTimeZone("GMT");

    private static final ThreadLocal<DateFormat> LOCAL = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {

            DateFormat df = new SimpleDateFormat(FORMAT);

            df.setTimeZone(ZONE);

            return df;

        }
    };

    @Override
    public JsonElement serialize(Long s, Type t, JsonSerializationContext c) {

        if (s == null) {
            return JsonNull.INSTANCE;
        }

        String value = LOCAL.get().format(new Date(s));

        return new JsonPrimitive(value);

    }


    @Override
    public Long deserialize(JsonElement j, Type t, JsonDeserializationContext c) throws JsonParseException {

        if (j.isJsonNull()) {
            return null;
        }

        String value = j.getAsString();

        try {

            return LOCAL.get().parse(value).getTime();

        } catch (ParseException e) {

            String msg = "Invalid timestamp : " + value;

            throw new JsonParseException(msg, e);

        }

    }

}
