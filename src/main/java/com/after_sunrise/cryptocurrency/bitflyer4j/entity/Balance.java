package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Balance {

    String getCurrency();

    BigDecimal getAmount();

    BigDecimal getAvailable();

}
