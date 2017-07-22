package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Market;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class MarketImpl extends AbstractEntity implements Market {

    @SerializedName("product_code")
    @VisibleForTesting
    String key;

    @SerializedName("alias")
    @VisibleForTesting
    String alias;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getAlias() {
        return alias;
    }

}
