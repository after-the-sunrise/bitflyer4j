package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Chat;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ChatImpl extends AbstractEntity<String, Chat> implements Chat {

    @SerializedName("date")
    @VisibleForTesting
    ZonedDateTime timestamp;

    @SerializedName("nickname")
    @VisibleForTesting
    String name;

    @SerializedName("message")
    @VisibleForTesting
    String message;

    @Override
    public String getKey() {
        return toString();
    }

    @Override
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
