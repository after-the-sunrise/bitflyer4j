package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.TimestampJsonAdapter;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TickImpl extends AbstractEntity implements Tick {

    @SerializedName("tick_id")
    @VisibleForTesting
    String key;

    @JsonAdapter(TimestampJsonAdapter.class)
    @SerializedName("timestamp")
    @VisibleForTesting
    Long timestamp;

    @SerializedName("product_code")
    @VisibleForTesting
    String product;

    @SerializedName("best_ask")
    @VisibleForTesting
    BigDecimal bestAskPrice;

    @SerializedName("best_ask_size")
    @VisibleForTesting
    BigDecimal bestAskSize;

    @SerializedName("best_bid")
    @VisibleForTesting
    BigDecimal bestBidPrice;

    @SerializedName("best_bid_size")
    @VisibleForTesting
    BigDecimal bestBidSize;

    @SerializedName("total_ask_depth")
    @VisibleForTesting
    BigDecimal totalAskDepth;

    @SerializedName("total_bid_depth")
    @VisibleForTesting
    BigDecimal totalBidDepth;

    @SerializedName("ltp")
    @VisibleForTesting
    BigDecimal tradePrice;

    @SerializedName("volume")
    @VisibleForTesting
    BigDecimal tradeVolume;

    @SerializedName("volume_by_product")
    @VisibleForTesting
    BigDecimal productVolume;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public BigDecimal getBestAskPrice() {
        return bestAskPrice;
    }

    @Override
    public BigDecimal getBestAskSize() {
        return bestAskSize;
    }

    @Override
    public BigDecimal getBestBidPrice() {
        return bestBidPrice;
    }

    @Override
    public BigDecimal getBestBidSize() {
        return bestBidSize;
    }

    @Override
    public BigDecimal getTotalAskDepth() {
        return totalAskDepth;
    }

    @Override
    public BigDecimal getTotalBidDepth() {
        return totalBidDepth;
    }

    @Override
    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    @Override
    public BigDecimal getTradeVolume() {
        return tradeVolume;
    }

    @Override
    public BigDecimal getProductVolume() {
        return productVolume;
    }

}
