package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface CoinIn extends Entity<Long, CoinIn> {

    String getOrderId();

    String getCurrency();

    BigDecimal getAmount();

    String getAddress();

    String getHash();

    String getStatus();

    ZonedDateTime getEventDate();

}
