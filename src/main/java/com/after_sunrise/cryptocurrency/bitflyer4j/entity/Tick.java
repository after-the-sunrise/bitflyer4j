package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Tick extends Entity {

    Long getTimestamp();

    String getProduct();

    BigDecimal getBestAskPrice();

    BigDecimal getBestAskSize();

    BigDecimal getBestBidPrice();

    BigDecimal getBestBidSize();

    BigDecimal getTotalAskDepth();

    BigDecimal getTotalBidDepth();

    BigDecimal getTradePrice();

    BigDecimal getTradeVolume();

    BigDecimal getProductVolume();

}
