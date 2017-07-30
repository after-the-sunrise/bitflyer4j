package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
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
public class ExecutionImpl extends Entity implements Execution {

    @SerializedName("id")
    private final Long id;

    @SerializedName("side")
    private final SideType side;

    @SerializedName("price")
    private final BigDecimal price;

    @SerializedName("size")
    private final BigDecimal size;

    @SerializedName("exec_date")
    private final ZonedDateTime timestamp;

    @SerializedName("buy_child_order_acceptance_id")
    private final String buyOrderId;

    @SerializedName("sell_child_order_acceptance_id")
    private final String sellOrderId;

}
