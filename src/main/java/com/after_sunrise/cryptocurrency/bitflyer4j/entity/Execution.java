package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Execution extends Entity<Long, Execution> {

    ZonedDateTime getTimestamp();

    BigDecimal getPrice();

    BigDecimal getSize();

    String getSide();

    String getBuyOrderId();

    String getSellOrderId();

}
