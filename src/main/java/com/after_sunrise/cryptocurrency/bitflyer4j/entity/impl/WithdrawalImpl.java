package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Withdrawal;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class WithdrawalImpl extends Entity implements Withdrawal {

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
    String status;

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
    public String getStatus() {
        return status;
    }

    @Override
    public ZonedDateTime getEventDate() {
        return eventDate;
    }

}
