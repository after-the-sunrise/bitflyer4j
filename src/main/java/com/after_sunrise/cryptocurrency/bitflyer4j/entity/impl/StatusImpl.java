package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Status;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class StatusImpl extends AbstractEntity<ZonedDateTime, Status> implements Status {

    @VisibleForTesting
    ZonedDateTime key = ZonedDateTime.now();

    @SerializedName("status")
    @VisibleForTesting
    String status;

    @Override
    public ZonedDateTime getKey() {
        return key;
    }

    @Override
    public String getStatus() {
        return status;
    }

}
