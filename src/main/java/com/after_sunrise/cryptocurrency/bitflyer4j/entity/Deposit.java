package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Deposit extends Entity<Long, Deposit> {

    String getOrderId();

    String getCurrency();

    BigDecimal getAmount();

    String getStatus();

    ZonedDateTime getEventDate();

}
