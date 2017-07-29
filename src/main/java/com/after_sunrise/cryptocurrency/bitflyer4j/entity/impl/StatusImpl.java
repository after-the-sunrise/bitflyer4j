package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

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
    String status;

    @Override
    public String getStatus() {
        return status;
    }

}
