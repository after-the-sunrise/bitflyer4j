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
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TradePosition extends Entity {

    @SerializedName("product_code")
    private final String product;

    public interface Response {

        String getProduct();

        SideType getSide();

        BigDecimal getPrice();

        BigDecimal getSize();

        BigDecimal getCommission();

        BigDecimal getSwapPoint();

        BigDecimal getRequiredCollateral();

        ZonedDateTime getOpenDate();

        BigDecimal getLeverage();

        BigDecimal getProfitAndLoss();

    }

}
