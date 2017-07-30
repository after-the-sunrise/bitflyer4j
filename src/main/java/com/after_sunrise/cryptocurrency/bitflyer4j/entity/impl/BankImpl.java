package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Bank;
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
public class BankImpl extends Entity implements Bank {

    @SerializedName("id")
    private final Long id;

    @SerializedName("is_verified")
    private final Boolean verified;

    @SerializedName("bank_name")
    private final String name;

    @SerializedName("branch_name")
    private final String branch;

    @SerializedName("account_type")
    private final String type;

    @SerializedName("account_number")
    private final String number;

    @SerializedName("account_name")
    private final String account;

}
