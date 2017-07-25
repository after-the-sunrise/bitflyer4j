package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Address;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class AddressImpl extends AbstractEntity<String, Address> implements Address {

    @SerializedName("address")
    @VisibleForTesting
    String key;

    @SerializedName("currency_code")
    @VisibleForTesting
    String currency;

    @SerializedName("type")
    @VisibleForTesting
    String type;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public String getType() {
        return type;
    }

}
