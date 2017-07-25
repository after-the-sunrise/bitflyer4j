package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Collateral;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class CollateralImpl extends AbstractEntity<ZonedDateTime, Collateral> implements Collateral {

    @VisibleForTesting
    ZonedDateTime key = ZonedDateTime.now();

    @SerializedName("collateral")
    @VisibleForTesting
    BigDecimal collateral;

    @SerializedName("open_position_pnl")
    @VisibleForTesting
    BigDecimal openPositionPl;

    @SerializedName("require_collateral")
    @VisibleForTesting
    BigDecimal requiredCollateral;

    @SerializedName("keep_rate")
    @VisibleForTesting
    BigDecimal keepRate;

    @Override
    public ZonedDateTime getKey() {
        return key;
    }

    @Override
    public BigDecimal getCollateral() {
        return collateral;
    }

    @Override
    public BigDecimal getOpenPositionPl() {
        return openPositionPl;
    }

    @Override
    public BigDecimal getRequiredCollateral() {
        return requiredCollateral;
    }

    @Override
    public BigDecimal getKeepRate() {
        return keepRate;
    }

}
