package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.TradePosition;
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
public class TradePositionResponse extends Entity implements TradePosition {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("side")
    private final SideType side;

    @SerializedName("price")
    private final BigDecimal price;

    @SerializedName("size")
    private final BigDecimal size;

    @SerializedName("commission")
    private final BigDecimal commission;

    @SerializedName("swap_point_accumulate")
    private final BigDecimal swapPoint;

    @SerializedName("require_collateral")
    private final BigDecimal requiredCollateral;

    @SerializedName("open_date")
    private final ZonedDateTime openDate;

    @SerializedName("leverage")
    private final BigDecimal leverage;

    @SerializedName("pnl")
    private final BigDecimal profitAndLoss;

    @SerializedName("sfd")
    private final BigDecimal sfd;

}
