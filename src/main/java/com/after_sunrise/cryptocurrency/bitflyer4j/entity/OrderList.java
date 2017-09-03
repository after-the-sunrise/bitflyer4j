package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface OrderList {

    Long getId();

    String getOrderId();

    String getProduct();

    SideType getSide();

    ConditionType getCondition();

    BigDecimal getPrice();

    BigDecimal getAveragePrice();

    BigDecimal getSize();

    StateType getState();

    ZonedDateTime getExpireDate();

    ZonedDateTime getOrderDate();

    String getAcceptanceId();

    BigDecimal getOutstandingSize();

    BigDecimal getCancelSize();

    BigDecimal getExecutedSize();

    BigDecimal getTotalCommission();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity implements Pagination {

        @SerializedName("product_code")
        private final String product;

        @SerializedName("child_order_state")
        private final StateType state;

        @SerializedName("child_order_id")
        private final String orderId;

        @SerializedName("child_order_acceptance_id")
        private final String acceptanceId;

        @SerializedName("parent_order_id")
        private final String parentId;

        @SerializedName("count")
        private final Integer count;

        @SerializedName("before")
        private final Long before;

        @SerializedName("after")
        private final Long after;

    }

}
