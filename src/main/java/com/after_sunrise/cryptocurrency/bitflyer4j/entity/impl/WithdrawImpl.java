package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdraw;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class WithdrawImpl extends AbstractEntity<String, Withdraw> implements Withdraw {

    @SerializedName("message_id")
    @VisibleForTesting
    String key;

    @SerializedName("status")
    @VisibleForTesting
    String status;

    @SerializedName("error_message")
    @VisibleForTesting
    String message;

    @SerializedName("data")
    @VisibleForTesting
    String data;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getStatus() {
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
