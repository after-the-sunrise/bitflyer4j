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
public interface Execution {

    Long getId();

    SideType getSide();

    BigDecimal getPrice();

    BigDecimal getSize();

    ZonedDateTime getTimestamp();

    String getBuyOrderId();

    String getSellOrderId();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity implements Pagination {

        @SerializedName("product_code")
        private final String product;

        @SerializedName("count")
        private final Integer count;

        @SerializedName("before")
        private final Long before;

        @SerializedName("after")
        private final Long after;

    }

}
