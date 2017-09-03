package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Board {

    BigDecimal getMid();

    List<Quote> getAsk();

    List<Quote> getBid();

    interface Quote {

        BigDecimal getPrice();

        BigDecimal getSize();

    }

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity {

        @SerializedName("product_code")
        private final String product;

    }

}
