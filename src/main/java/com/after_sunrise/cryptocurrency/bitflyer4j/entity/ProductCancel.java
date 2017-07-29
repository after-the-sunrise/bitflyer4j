package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ProductCancel extends Entity {

    @SerializedName("product_code")
    @VisibleForTesting
    private final String product;

    private ProductCancel(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public static class Builder {

        private String product;

        public ProductCancel build() {
            return new ProductCancel(product);
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

    }

    public interface Response {
    }

}
