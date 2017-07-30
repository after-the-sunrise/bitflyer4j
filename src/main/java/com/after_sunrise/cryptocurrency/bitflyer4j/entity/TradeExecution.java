package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

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
public class TradeExecution extends Entity {

    private final String product;

    private final String child_order_id;

    private final String child_order_acceptance_id;

}
