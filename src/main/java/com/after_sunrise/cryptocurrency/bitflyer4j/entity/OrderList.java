package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StateType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class OrderList extends Entity {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("child_order_state")
    private final StateType state;

    @SerializedName("child_order_id")
    private final String orderId;

    @SerializedName("child_order_acceptance_id")
    private final String acceptanceId;

    @SerializedName("parent_order_id")
    private final String parentId;

    private OrderList(String product, StateType state, String orderId, String acceptanceId, String parentId) {
        this.product = product;
        this.state = state;
        this.orderId = orderId;
        this.acceptanceId = acceptanceId;
        this.parentId = parentId;
    }

    public String getProduct() {
        return product;
    }

    public StateType getState() {
        return state;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAcceptanceId() {
        return acceptanceId;
    }

    public String getParentId() {
        return parentId;
    }

    public static class Bulider {

        private String product;

        private StateType state;

        private String orderId;

        private String acceptanceId;

        private String parentId;

        public OrderList build() {
            return new OrderList(product, state, orderId, acceptanceId, parentId);
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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAcceptanceId() {
            return acceptanceId;
        }

        public void setAcceptanceId(String acceptanceId) {
            this.acceptanceId = acceptanceId;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }

    public interface Response {

        Long getId();

        String getOrderId();

        String getProduct();

        SideType getSide();

        ConditionType getCondition();

        BigDecimal getPrice();

        BigDecimal getAveragePrice();

        BigDecimal getSize();

        StateType getState();

        ZonedDateTime getExpireDate();

        ZonedDateTime getOrderDate();

        String getAcceptanceId();

        BigDecimal getOutstandingSize();

        BigDecimal getCancelSize();

        BigDecimal getExecutedSize();

        BigDecimal getTotalCommission();

    }

}
