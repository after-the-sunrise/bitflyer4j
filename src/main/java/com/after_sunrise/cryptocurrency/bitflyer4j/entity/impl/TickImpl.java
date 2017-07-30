package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class TickImpl extends Entity implements Tick {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("timestamp")
    private final ZonedDateTime timestamp;

    @SerializedName("tick_id")
    private final Long id;

    @SerializedName("best_ask")
    private final BigDecimal bestAskPrice;

    @SerializedName("best_ask_size")
    private final BigDecimal bestAskSize;

    @SerializedName("best_bid")
    private final BigDecimal bestBidPrice;

    @SerializedName("best_bid_size")
    private final BigDecimal bestBidSize;

    @SerializedName("total_ask_depth")
    private final BigDecimal totalAskDepth;

    @SerializedName("total_bid_depth")
    private final BigDecimal totalBidDepth;

    @SerializedName("ltp")
    private final BigDecimal tradePrice;

    @SerializedName("volume")
    private final BigDecimal tradeVolume;

    @SerializedName("volume_by_product")
    private final BigDecimal productVolume;

}
