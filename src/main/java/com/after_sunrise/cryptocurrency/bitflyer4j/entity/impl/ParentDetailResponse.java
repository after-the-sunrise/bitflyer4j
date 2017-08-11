package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.ParentDetail;
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
public class ParentDetailResponse extends Entity implements ParentDetail.Response {

    @Getter
    @AllArgsConstructor
    public static class ParentDetailParameter extends Entity implements ParentDetail.Response.Parameter {

        @SerializedName("product_code")
        private final String product;

        @SerializedName("condition_type")
        private final ConditionType condition;

        @SerializedName("side")
        private final SideType side;

        @SerializedName("price")
        private final BigDecimal price;

        @SerializedName("size")
        private final BigDecimal size;

        @SerializedName("trigger_price")
        private final BigDecimal triggerPrice;

        @SerializedName("offset")
        private final BigDecimal offset;

    }

    @SerializedName("id")
    private final Long id;

    @SerializedName("parent_order_id")
    private final String parentId;

    @SerializedName("parent_order_acceptance_id")
    private final String acceptanceId;

    @SerializedName("order_method")
    private final ParentType type;

    @SerializedName("minute_to_expire")
    private final Integer expiry;

    @SerializedName("parameters")
    private final List<ParentDetailParameter> parameters;

    @Override
    public List<ParentDetail.Response.Parameter> getParameters() {
        return parameters == null ? emptyList() : unmodifiableList(parameters);
    }

}
