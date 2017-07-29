package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Margin {

    String getCurrency();

    BigDecimal getAmount();

}
