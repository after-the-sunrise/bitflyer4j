package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Chat;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ChatImpl extends Entity implements Chat {

    @SerializedName("nickname")
    @VisibleForTesting
    String name;

    @SerializedName("message")
    @VisibleForTesting
    String message;

    @SerializedName("date")
    @VisibleForTesting
    ZonedDateTime timestamp;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

}
