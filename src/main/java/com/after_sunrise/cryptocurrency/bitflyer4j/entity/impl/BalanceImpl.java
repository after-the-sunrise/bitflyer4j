package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Balance;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class BalanceImpl extends Entity implements Balance {

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("amount")
    private final BigDecimal amount;

    @SerializedName("available")
    private final BigDecimal available;

}
