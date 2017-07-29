package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.StatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Status;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class StatusImpl extends Entity implements Status {

    @SerializedName("status")
    @VisibleForTesting
    StatusType status;

    @Override
    public StatusType getStatus() {
        return status;
    }

}
