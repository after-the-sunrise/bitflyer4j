package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TradeExecution extends Entity {

    private final String product;

    private final String child_order_id;

    private final String child_order_acceptance_id;

    private TradeExecution(String product, String child_order_id, String child_order_acceptance_id) {
        this.product = product;
        this.child_order_id = child_order_id;
        this.child_order_acceptance_id = child_order_acceptance_id;
    }

    public String getProduct() {
        return product;
    }

    public String getChild_order_id() {
        return child_order_id;
    }

    public String getChild_order_acceptance_id() {
        return child_order_acceptance_id;
    }


}
