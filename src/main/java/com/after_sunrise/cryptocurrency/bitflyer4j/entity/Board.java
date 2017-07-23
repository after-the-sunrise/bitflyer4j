package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Board {

    interface Quote {

        BigDecimal getPrice();

        BigDecimal getSize();

    }

    BigDecimal getMid();

    List<? extends Quote> getAsk();

    List<? extends Quote> getBid();

}
