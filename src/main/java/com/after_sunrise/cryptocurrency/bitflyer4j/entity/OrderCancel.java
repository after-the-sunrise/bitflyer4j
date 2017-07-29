package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class OrderCancel extends Entity {

    @SerializedName("product_code")
    @VisibleForTesting
    private final String product;

    @SerializedName("child_order_id")
    @VisibleForTesting
    private final String orderId;

    @SerializedName("child_order_acceptance_id")
    @VisibleForTesting
    private final String acceptanceId;

    private OrderCancel(String product, String orderId, String acceptanceId) {
        this.product = product;
        this.orderId = orderId;
        this.acceptanceId = acceptanceId;
    }

    public String getProduct() {
        return product;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAcceptanceId() {
        return acceptanceId;
    }

    public static class Builder {

        private String product;

        private String orderId;

        private String acceptanceId;

        public OrderCancel build() {
            return new OrderCancel(product, orderId, acceptanceId);
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
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

    }

    public interface Response {
    }

}
