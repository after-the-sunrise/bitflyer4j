package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Execution extends Entity {

    Long getTimestamp();

    BigDecimal getPrice();

    BigDecimal getSize();

    String getSide();

    String getBuyOrderId();

    String getSellOrderId();

}
