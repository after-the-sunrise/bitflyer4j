package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.TradeCommission;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.0
 */
@Getter
@AllArgsConstructor
public class TradeCommissionResponse extends Entity implements TradeCommission {

    @SerializedName("commission_rate")
    private final BigDecimal rate;

}
