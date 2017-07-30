package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.TradeCollateral;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.0
 */
@Getter
@AllArgsConstructor
public class TradeCollateralResponse extends Entity implements TradeCollateral.Response {

    @SerializedName("id")
    private final Long id;

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("change")
    private final BigDecimal change;

    @SerializedName("amount")
    private final BigDecimal amount;

    @SerializedName("reason_code")
    private final String reasonCode;

    @SerializedName("date")
    private final ZonedDateTime date;

}
