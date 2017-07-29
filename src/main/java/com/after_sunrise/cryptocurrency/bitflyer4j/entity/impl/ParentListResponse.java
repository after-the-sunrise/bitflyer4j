package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.ParentList;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ParentListResponse extends Entity implements ParentList.Response {

    @SerializedName("id")
    @VisibleForTesting
    Long id;

    @SerializedName("parent_order_id")
    @VisibleForTesting
    String orderId;

    @SerializedName("product_code")
    @VisibleForTesting
    String product;

    @SerializedName("side")
    @VisibleForTesting
    SideType side;

    @SerializedName("parent_order_type")
    @VisibleForTesting
    ParentType type;

    @SerializedName("price")
    @VisibleForTesting
    BigDecimal price;

    @SerializedName("average_price")
    @VisibleForTesting
    BigDecimal averagePrice;

    @SerializedName("size")
    @VisibleForTesting
    BigDecimal size;

    @SerializedName("parent_order_state")
    @VisibleForTesting
    StateType state;

    @SerializedName("expire_date")
    @VisibleForTesting
    ZonedDateTime expireDate;

    @SerializedName("parent_order_date")
    @VisibleForTesting
    ZonedDateTime orderDate;

    @SerializedName("parent_order_acceptance_id")
    @VisibleForTesting
    String acceptanceId;

    @SerializedName("outstanding_size")
    @VisibleForTesting
    BigDecimal outstandingSize;

    @SerializedName("cancel_size")
    @VisibleForTesting
    BigDecimal cancelSize;

    @SerializedName("executed_size")
    @VisibleForTesting
    BigDecimal executedSize;

    @SerializedName("total_commission")
    @VisibleForTesting
    BigDecimal totalCommission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return orderId;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public SideType getSide() {
        return side;
    }

    @Override
    public ParentType getType() {
        return type;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    @Override
    public BigDecimal getSize() {
        return size;
    }

    @Override
    public StateType getState() {
        return state;
    }

    @Override
    public ZonedDateTime getExpireDate() {
        return expireDate;
    }

    @Override
    public ZonedDateTime getParentDate() {
        return orderDate;
    }

    @Override
    public String getAcceptanceId() {
        return acceptanceId;
    }

    @Override
    public BigDecimal getOutstandingSize() {
        return outstandingSize;
    }

    @Override
    public BigDecimal getCancelSize() {
        return cancelSize;
    }

    @Override
    public BigDecimal getExecutedSize() {
        return executedSize;
    }

    @Override
    public BigDecimal getTotalCommission() {
        return totalCommission;
    }

}
