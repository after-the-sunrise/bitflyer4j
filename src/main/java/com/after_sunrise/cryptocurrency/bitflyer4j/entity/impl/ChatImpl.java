package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.TimestampJsonAdapter;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Chat;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ChatImpl implements Chat {

    @JsonAdapter(TimestampJsonAdapter.class)
    @SerializedName("date")
    @VisibleForTesting
    Long timestamp;

    @SerializedName("nickname")
    @VisibleForTesting
    String name;

    @SerializedName("message")
    @VisibleForTesting
    String message;

    @Override
    public Long getTimestamp() {
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
