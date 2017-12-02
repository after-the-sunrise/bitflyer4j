package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.BoardStatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.BoardStatus;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author takanori.takase
 * @version 0.1.6
 */
@Getter
@AllArgsConstructor
public class BoardStatusImpl extends Entity implements BoardStatus {

    @SerializedName("health")
    private final StatusType health;

    @SerializedName("state")
    private final BoardStatusType state;

    @SerializedName("data")
    private final Map<String, String> data;

}
