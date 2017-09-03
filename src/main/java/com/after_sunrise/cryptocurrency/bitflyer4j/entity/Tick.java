package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

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
public interface Tick {

    String getProduct();

    ZonedDateTime getTimestamp();

    Long getId();

    BigDecimal getBestAskPrice();

    BigDecimal getBestAskSize();

    BigDecimal getBestBidPrice();

    BigDecimal getBestBidSize();

    BigDecimal getTotalAskDepth();

    BigDecimal getTotalBidDepth();

    BigDecimal getTradePrice();

    BigDecimal getTradeVolume();

    BigDecimal getProductVolume();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity {

        @SerializedName("product_code")
        private final String product;

    }

}
