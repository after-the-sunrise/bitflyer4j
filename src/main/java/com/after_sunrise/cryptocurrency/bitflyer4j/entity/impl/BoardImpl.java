package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class BoardImpl extends Entity implements Board {

    @Getter
    @AllArgsConstructor
    public static class QuoteImpl extends Entity implements Quote {

        @SerializedName("price")
        private final BigDecimal price;

        @SerializedName("size")
        private final BigDecimal size;

    }

    @SerializedName("mid_price")
    private final BigDecimal mid;

    @SerializedName("asks")
    private final List<QuoteImpl> ask;

    @SerializedName("bids")
    private final List<QuoteImpl> bid;

    @Override
    public List<Quote> getAsk() {
        return ask == null ? emptyList() : unmodifiableList(ask);
    }

    @Override
    public List<Quote> getBid() {
        return bid == null ? emptyList() : unmodifiableList(bid);
    }

}
