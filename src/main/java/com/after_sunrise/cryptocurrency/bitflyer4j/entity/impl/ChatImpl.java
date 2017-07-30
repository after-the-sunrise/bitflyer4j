package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Chat;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class ChatImpl extends Entity implements Chat {

    @SerializedName("nickname")
    private final String name;

    @SerializedName("message")
    private final String message;

    @SerializedName("date")
    private final ZonedDateTime timestamp;

}
