package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Board {

    BigDecimal getMid();

    List<Quote> getAsk();

    List<Quote> getBid();

    interface Quote {

        BigDecimal getPrice();

        BigDecimal getSize();

    }

}
