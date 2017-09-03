package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
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
public interface TradeExecution {

    Long getId();

    String getOrderId();

    SideType getSide();

    BigDecimal getPrice();

    BigDecimal getSize();

    BigDecimal getCommission();

    ZonedDateTime getExecDate();

    String getAcceptanceId();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity implements Pagination {

        @SerializedName("product_code")
        private final String product;

        @SerializedName("child_order_id")
        private final String childOrderId;

        @SerializedName("child_order_acceptance_id")
        private final String childOrderAcceptanceId;

        @SerializedName("count")
        private final Integer count;

        @SerializedName("before")
        private final Long before;

        @SerializedName("after")
        private final Long after;

    }

}
