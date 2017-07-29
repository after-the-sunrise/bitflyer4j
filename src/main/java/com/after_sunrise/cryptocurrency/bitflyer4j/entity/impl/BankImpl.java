package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Bank;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class BankImpl extends Entity implements Bank {

    @SerializedName("id")
    @VisibleForTesting
    Long id;

    @SerializedName("is_verified")
    @VisibleForTesting
    Boolean verified;

    @SerializedName("bank_name")
    @VisibleForTesting
    String name;

    @SerializedName("branch_name")
    @VisibleForTesting
    String branch;

    @SerializedName("account_type")
    @VisibleForTesting
    String type;

    @SerializedName("account_number")
    @VisibleForTesting
    String number;

    @SerializedName("account_name")
    @VisibleForTesting
    String account;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Boolean isVerified() {
        return verified;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBranch() {
        return branch;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public String getAccount() {
        return account;
    }

}
