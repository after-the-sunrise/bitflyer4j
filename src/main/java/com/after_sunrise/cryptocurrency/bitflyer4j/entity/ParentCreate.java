package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConditionType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ParentType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.SideType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.TimeInForceType;
import com.google.common.collect.ImmutableList;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ParentCreate extends Entity {

    @SerializedName("order_method")
    private final ParentType type;

    @SerializedName("minute_to_expire")
    private final Integer expiry;

    @SerializedName("time_in_force")
    private final TimeInForceType timeInForce;

    @SerializedName("parameters")
    private final List<Parameter> parameters;

    private ParentCreate(ParentType type, Integer expiry, TimeInForceType timeInForce, List<Parameter> parameters) {
        this.type = type;
        this.expiry = expiry;
        this.timeInForce = timeInForce;
        this.parameters = ImmutableList.copyOf(parameters);
    }

    public ParentType getType() {
        return type;
    }

    public Integer getExpiry() {
        return expiry;
    }

    public TimeInForceType getTimeInForce() {
        return timeInForce;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public static class Builder {

        private ParentType type;

        private Integer expiry;

        private TimeInForceType timeInForce;

        private List<Parameter> parameters;

        public ParentCreate build() {
            return new ParentCreate(type, expiry, timeInForce, parameters);
        }

        public ParentType getType() {
            return type;
        }

        public void setType(ParentType type) {
            this.type = type;
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

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }

    }

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

        private Parameter(String product, ConditionType condition, SideType side, BigDecimal price, BigDecimal size, BigDecimal triggerPrice, BigDecimal offset) {
            this.product = product;
            this.condition = condition;
            this.side = side;
            this.price = price;
            this.size = size;
            this.triggerPrice = triggerPrice;
            this.offset = offset;
        }

        public String getProduct() {
            return product;
        }

        public ConditionType getCondition() {
            return condition;
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

        public BigDecimal getTriggerPrice() {
            return triggerPrice;
        }

        public BigDecimal getOffset() {
            return offset;
        }

        public static class Builder {

            private String product;

            private ConditionType condition;

            private SideType side;

            private BigDecimal price;

            private BigDecimal size;

            private BigDecimal triggerPrice;

            private BigDecimal offset;

            public Parameter build() {
                return new Parameter(product, condition, side, price, size, triggerPrice, offset);
            }

            public String getProduct() {
                return product;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public ConditionType getCondition() {
                return condition;
            }

            public void setCondition(ConditionType condition) {
                this.condition = condition;
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

            public BigDecimal getTriggerPrice() {
                return triggerPrice;
            }

            public void setTriggerPrice(BigDecimal triggerPrice) {
                this.triggerPrice = triggerPrice;
            }

            public BigDecimal getOffset() {
                return offset;
            }

            public void setOffset(BigDecimal offset) {
                this.offset = offset;
            }

        }

    }

    public interface Response {

        String getAcceptanceId();

    }

}
