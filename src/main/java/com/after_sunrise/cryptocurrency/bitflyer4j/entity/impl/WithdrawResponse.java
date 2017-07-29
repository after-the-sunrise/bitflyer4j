package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdraw;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class WithdrawResponse extends Entity implements Withdraw.Response {

    @SerializedName("message_id")
    @VisibleForTesting
    String id;

    @SerializedName("status")
    @VisibleForTesting
    Integer status;

    @SerializedName("error_message")
    @VisibleForTesting
    String message;

    @SerializedName("data")
    @VisibleForTesting
    String data;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getData() {
        return data;
    }

}
