package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Address;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class AddressImpl extends Entity implements Address {

    @SerializedName("type")
    @VisibleForTesting
    String type;

    @SerializedName("currency_code")
    @VisibleForTesting
    String currency;

    @SerializedName("address")
    @VisibleForTesting
    String address;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public String getAddress() {
        return address;
    }

}
