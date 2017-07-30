package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.ParentList;
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
public class ParentListResponse extends Entity implements ParentList.Response {

    @SerializedName("id")
    private final Long id;

    @SerializedName("parent_order_id")
    private final String parentId;

    @SerializedName("product_code")
    private final String product;

    @SerializedName("side")
    private final SideType side;

    @SerializedName("parent_order_type")
    private final ParentType type;

    @SerializedName("price")
    private final BigDecimal price;

    @SerializedName("average_price")
    private final BigDecimal averagePrice;

    @SerializedName("size")
    private final BigDecimal size;

    @SerializedName("parent_order_state")
    private final StateType state;

    @SerializedName("expire_date")
    private final ZonedDateTime expireDate;

    @SerializedName("parent_order_date")
    private final ZonedDateTime parentDate;

    @SerializedName("parent_order_acceptance_id")
    private final String acceptanceId;

    @SerializedName("outstanding_size")
    private final BigDecimal outstandingSize;

    @SerializedName("cancel_size")
    private final BigDecimal cancelSize;

    @SerializedName("executed_size")
    private final BigDecimal executedSize;

    @SerializedName("total_commission")
    private final BigDecimal totalCommission;

}
