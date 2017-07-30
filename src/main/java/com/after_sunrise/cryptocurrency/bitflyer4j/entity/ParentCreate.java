package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.TimeInForceType;
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
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParentCreate extends Entity {

    @SerializedName("order_method")
    private final ParentType type;

    @SerializedName("minute_to_expire")
    private final Integer expiry;

    @SerializedName("time_in_force")
    private final TimeInForceType timeInForce;

    @SerializedName("parameters")
    private final List<Parameter> parameters;

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Parameter extends Entity {

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

    public interface Response {

        String getAcceptanceId();

    }

}
