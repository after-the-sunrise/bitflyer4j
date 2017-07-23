package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class BoardImpl implements Board {

    static class QuoteImpl implements Quote {

        @VisibleForTesting
        BigDecimal price;

        @VisibleForTesting
        BigDecimal size;

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public BigDecimal getSize() {
            return size;
        }

    }

    @SerializedName("mid_price")
    @VisibleForTesting
    BigDecimal mid;

    @SerializedName("asks")
    @VisibleForTesting
    List<QuoteImpl> ask;

    @SerializedName("bids")
    @VisibleForTesting
    List<QuoteImpl> bid;

    @Override
    public BigDecimal getMid() {
        return mid;
    }

    @Override
    public List<? extends Quote> getAsk() {
        return ask == null ? emptyList() : unmodifiableList(ask);
    }

    @Override
    public List<? extends Quote> getBid() {
        return bid == null ? emptyList() : unmodifiableList(bid);
    }

}
