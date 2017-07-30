package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Address;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class AddressImpl extends Entity implements Address {

    @SerializedName("type")
    private final String type;

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("address")
    private final String address;

}
