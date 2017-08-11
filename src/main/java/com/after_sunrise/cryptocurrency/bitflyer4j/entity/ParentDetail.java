package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
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
public class ParentDetail extends Entity {

    @SerializedName("parent_order_id")
    private final String parentId;

    @SerializedName("parent_order_acceptance_id")
    private final String acceptanceId;

    public interface Response {

        Long getId();

        String getParentId();

        String getAcceptanceId();

        ParentType getType();

        Integer getExpiry();

        List<Parameter> getParameters();

        interface Parameter {

            String getProduct();

            ConditionType getCondition();

            SideType getSide();

            BigDecimal getPrice();

            BigDecimal getSize();

            BigDecimal getTriggerPrice();

            BigDecimal getOffset();

        }

    }

}
