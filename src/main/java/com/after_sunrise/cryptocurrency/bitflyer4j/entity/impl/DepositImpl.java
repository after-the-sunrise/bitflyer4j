package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.DepositStatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Deposit;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class DepositImpl extends Entity implements Deposit {

    @SerializedName("id")
    @VisibleForTesting
    Long id;

    @SerializedName("order_id")
    @VisibleForTesting
    String orderId;

    @SerializedName("currency_code")
    @VisibleForTesting
    String currency;

    @SerializedName("amount")
    @VisibleForTesting
    BigDecimal amount;

    @SerializedName("status")
    @VisibleForTesting
    DepositStatusType status;

    @SerializedName("event_date")
    @VisibleForTesting
    ZonedDateTime eventDate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public String getCurrency() {
        return currency;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public DepositStatusType getStatus() {
        return status;
    }

    @Override
    public ZonedDateTime getEventDate() {
        return eventDate;
    }

}
