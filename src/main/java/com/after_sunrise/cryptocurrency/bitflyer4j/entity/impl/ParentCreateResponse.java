package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.ParentCreate;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ParentCreateResponse extends Entity implements ParentCreate.Response {

    @SerializedName("parent_order_acceptance_id")
    @VisibleForTesting
    String acceptanceId;

    @Override
    public String getAcceptanceId() {
        return acceptanceId;
    }

}
