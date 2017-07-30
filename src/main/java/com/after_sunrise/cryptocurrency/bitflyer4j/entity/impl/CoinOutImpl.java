package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.CoinOut;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class CoinOutImpl extends Entity implements CoinOut {

    @SerializedName("id")
    private final Long id;

    @SerializedName("order_id")
    private final String orderId;

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("amount")
    private final BigDecimal amount;

    @SerializedName("address")
    private final String address;

    @SerializedName("tx_hash")
    private final String hash;

    @SerializedName("fee")
    private final BigDecimal fee;

    @SerializedName("additional_fee")
    private final BigDecimal additionalFee;

    @SerializedName("status")
    private final String status;

    @SerializedName("event_date")
    private final ZonedDateTime eventDate;

}
