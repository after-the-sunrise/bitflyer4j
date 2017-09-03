package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
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
public interface ParentList {

    Long getId();

    String getParentId();

    String getProduct();

    SideType getSide();

    ParentType getType();

    BigDecimal getPrice();

    BigDecimal getAveragePrice();

    BigDecimal getSize();

    StateType getState();

    ZonedDateTime getExpireDate();

    ZonedDateTime getParentDate();

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

        @SerializedName("parent_order_state")
        private final StateType state;

        @SerializedName("count")
        private final Integer count;

        @SerializedName("before")
        private final Long before;

        @SerializedName("after")
        private final Long after;

    }

}
