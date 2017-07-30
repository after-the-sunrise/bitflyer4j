package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParentCancel extends Entity {

    @SerializedName("product_code")
    private final String product;

    @SerializedName("parent_order_id")
    private final String orderId;

    @SerializedName("parent_order_acceptance_id")
    private final String acceptanceId;

    public interface Response {
    }

}
