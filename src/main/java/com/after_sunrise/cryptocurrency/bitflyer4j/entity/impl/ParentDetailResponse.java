package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.ParentDetail;
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
public class ParentDetailResponse extends Entity implements ParentDetail.Response {

    static class ParentDetailParameter extends Entity implements ParentDetail.Response.Parameter {

        @SerializedName("product_code")
        @VisibleForTesting
        String product;

        @SerializedName("condition_type")
        @VisibleForTesting
        ConditionType condition;

        @SerializedName("side")
        @VisibleForTesting
        SideType side;

        @SerializedName("price")
        @VisibleForTesting
        BigDecimal price;

        @SerializedName("size")
        @VisibleForTesting
        BigDecimal size;

        @SerializedName("trigger_price")
        @VisibleForTesting
        BigDecimal triggerPrice;

        @SerializedName("offset")
        @VisibleForTesting
        BigDecimal offset;

        @Override
        public String getProduct() {
            return product;
        }

        @Override
        public ConditionType getCondition() {
            return condition;
        }

        @Override
        public SideType getSide() {
            return side;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public BigDecimal getSize() {
            return size;
        }

        @Override
        public BigDecimal getTriggerPrice() {
            return triggerPrice;
        }

        @Override
        public BigDecimal getOffset() {
            return offset;
        }

    }

    @SerializedName("id")
    @VisibleForTesting
    Long id;

    @SerializedName("parent_order_id")
    @VisibleForTesting
    String parentId;

    @SerializedName("order_method")
    @VisibleForTesting
    ParentType type;

    @SerializedName("minute_to_expire")
    @VisibleForTesting
    Integer expiry;

    @SerializedName("parameters")
    @VisibleForTesting
    List<ParentDetailParameter> parameters;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public ParentType getType() {
        return type;
    }

    @Override
    public Integer getExpiry() {
        return expiry;
    }

    @Override
    public List<ParentDetail.Response.Parameter> getParameters() {
        return parameters == null ? emptyList() : unmodifiableList(parameters);
    }

}
