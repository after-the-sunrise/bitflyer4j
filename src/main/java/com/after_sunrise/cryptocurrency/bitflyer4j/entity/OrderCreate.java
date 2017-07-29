package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.TimeInForceType;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class OrderCreate extends Entity {

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

    private OrderCreate(String product, ConditionType type, SideType side, BigDecimal price, BigDecimal size, Integer expiry, TimeInForceType timeInForce) {
        this.product = product;
        this.type = type;
        this.side = side;
        this.price = price;
        this.size = size;
        this.expiry = expiry;
        this.timeInForce = timeInForce;
    }

    public String getProduct() {
        return product;
    }

    public ConditionType getType() {
        return type;
    }

    public SideType getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSize() {
        return size;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public TimeInForceType getTimeInForce() {
        return timeInForce;
    }

    public static class Bulider {

        private String product;

        private ConditionType type;

        private SideType side;

        private BigDecimal price;

        private BigDecimal size;

        private Integer expiry;

        private TimeInForceType timeInForce;

        public OrderCreate build() {
            return new OrderCreate(product, type, side, price, size, expiry, timeInForce);
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public ConditionType getType() {
            return type;
        }

        public void setType(ConditionType type) {
            this.type = type;
        }

        public SideType getSide() {
            return side;
        }

        public void setSide(SideType side) {
            this.side = side;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getSize() {
            return size;
        }

        public void setSize(BigDecimal size) {
            this.size = size;
        }

        public Integer getExpiry() {
            return expiry;
        }

        public void setExpiry(Integer expiry) {
            this.expiry = expiry;
        }

        public TimeInForceType getTimeInForce() {
            return timeInForce;
        }

        public void setTimeInForce(TimeInForceType timeInForce) {
            this.timeInForce = timeInForce;
        }

    }

    public interface Response {

        String getAcceptanceId();

    }

}
