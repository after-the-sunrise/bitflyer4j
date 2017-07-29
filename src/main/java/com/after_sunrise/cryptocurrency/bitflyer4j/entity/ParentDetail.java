package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ParentDetail extends Entity {

    @SerializedName("parent_order_id")
    private final String parentId;

    @SerializedName("parent_order_acceptance_id")
    private final String acceptanceId;

    private ParentDetail(String parentId, String acceptanceId) {
        this.parentId = parentId;
        this.acceptanceId = acceptanceId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getAcceptanceId() {
        return acceptanceId;
    }

    public static class Builder {

        private String parentId;

        private String acceptanceId;

        public ParentDetail build() {
            return new ParentDetail(parentId, acceptanceId);
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        public String getAcceptanceId() {
            return acceptanceId;
        }

        public void setAcceptanceId(String acceptanceId) {
            this.acceptanceId = acceptanceId;
        }

    }

    public interface Response {

        Long getId();

        String getParentId();

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
