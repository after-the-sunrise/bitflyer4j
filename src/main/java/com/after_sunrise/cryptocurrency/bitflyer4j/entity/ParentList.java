package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
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
public class ParentList extends Entity {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("parent_order_state")
    private final StateType state;

    public interface Response {

        Long getId();

        String getParentId();

        String getProduct();

        SideType getSide();

        ParentType getType();

        BigDecimal getPrice();

        BigDecimal getAveragePrice();

        BigDecimal getSize();

        StateType getState();

        ZonedDateTime getExpireDate();

        ZonedDateTime getParentDate();

        String getAcceptanceId();

        BigDecimal getOutstandingSize();

        BigDecimal getCancelSize();

        BigDecimal getExecutedSize();

        BigDecimal getTotalCommission();

    }

}
