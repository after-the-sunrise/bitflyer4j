package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.TimeInForceType;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface OrderCreate {

    String getAcceptanceId();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity {

        @SerializedName("product_code")
        private final String product;

        @SerializedName("child_order_type")
        private final ConditionType type;

        @SerializedName("side")
        private final SideType side;

        @SerializedName("price")
        private final BigDecimal price;

        @SerializedName("size")
        private final BigDecimal size;

        @SerializedName("minute_to_expire")
        private final Integer expiry;

        @SerializedName("time_in_force")
        private final TimeInForceType timeInForce;

    }

}
