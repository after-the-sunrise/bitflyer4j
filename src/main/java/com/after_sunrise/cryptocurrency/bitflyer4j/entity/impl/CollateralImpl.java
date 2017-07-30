package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Collateral;
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
public class CollateralImpl extends Entity implements Collateral {

    @SerializedName("collateral")
    private final BigDecimal collateral;

    @SerializedName("open_position_pnl")
    private final BigDecimal openPositionPl;

    @SerializedName("require_collateral")
    private final BigDecimal requiredCollateral;

    @SerializedName("keep_rate")
    private final BigDecimal keepRate;

}
