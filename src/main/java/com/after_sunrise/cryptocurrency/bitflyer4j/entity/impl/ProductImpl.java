package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Product;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.annotations.SerializedName;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class ProductImpl extends Entity implements Product {

    @SerializedName("product_code")
    @VisibleForTesting
    String product;

    @SerializedName("alias")
    @VisibleForTesting
    String alias;

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public String getAlias() {
        return alias;
    }

}
