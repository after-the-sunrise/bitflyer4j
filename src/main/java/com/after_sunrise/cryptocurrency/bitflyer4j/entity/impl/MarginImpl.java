package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Margin;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarginImpl extends AbstractEntity<String, Margin> implements Margin {

    @SerializedName("currency_code")
    @VisibleForTesting
    String key;

    @SerializedName("status")
    @VisibleForTesting
    BigDecimal amount;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

}
