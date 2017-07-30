package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.TradeExecution;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.0
 */
@Getter
@AllArgsConstructor
public class TradeExecutionResponse extends Entity implements TradeExecution.Response {

    @SerializedName("id")
    private final Long id;

    @SerializedName("child_order_id")
    private final String orderId;

    @SerializedName("side")
    private final SideType side;

    @SerializedName("price")
    private final BigDecimal price;

    @SerializedName("size")
    private final BigDecimal size;

    @SerializedName("commission")
    private final BigDecimal commission;

    @SerializedName("exec_date")
    private final ZonedDateTime execDate;

    @SerializedName("child_order_acceptance_id")
    private final String acceptanceId;

}
