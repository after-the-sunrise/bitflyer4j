package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdraw;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class WithdrawResponse extends Entity implements Withdraw.Response {

    @SerializedName("message_id")
    private final String id;

    @SerializedName("status")
    private final Integer status;

    @SerializedName("error_message")
    private final String message;

    @SerializedName("data")
    private final String data;

}
