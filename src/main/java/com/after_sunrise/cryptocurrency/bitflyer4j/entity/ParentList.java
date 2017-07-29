package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ParentList extends Entity {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("parent_order_state")
    private final StateType state;

    private ParentList(String product, StateType state) {
        this.product = product;
        this.state = state;
    }

    public String getProduct() {
        return product;
    }

    public StateType getState() {
        return state;
    }

    public static class Bulider {

        private String product;

        private StateType state;

        public ParentList build() {
            return new ParentList(product, state);
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public StateType getState() {
            return state;
        }

        public void setState(StateType state) {
            this.state = state;
        }

    }

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
