package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ExecutionImpl extends AbstractEntity<Long, Execution> implements Execution {

    @SerializedName("id")
    @VisibleForTesting
    Long key;

    @SerializedName("exec_date")
    @VisibleForTesting
    ZonedDateTime timestamp;

    @SerializedName("price")
    @VisibleForTesting
    BigDecimal price;

    @SerializedName("size")
    @VisibleForTesting
    BigDecimal size;

    @SerializedName("side")
    @VisibleForTesting
    String side;

    @SerializedName("buy_child_order_acceptance_id")
    @VisibleForTesting
    String buyOrderId;

    @SerializedName("sell_child_order_acceptance_id")
    @VisibleForTesting
    String sellOrderId;

    @Override
    public Long getKey() {
        return key;
    }

    @Override
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public BigDecimal getSize() {
        return size;
    }

    @Override
    public String getSide() {
        return side;
    }

    @Override
    public String getBuyOrderId() {
        return buyOrderId;
    }

    @Override
    public String getSellOrderId() {
        return sellOrderId;
    }

}