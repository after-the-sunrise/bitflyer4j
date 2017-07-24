package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Board extends Entity<ZonedDateTime, Board> {

    BigDecimal getMid();

    List<? extends Quote> getAsk();

    List<? extends Quote> getBid();

    interface Quote {

        BigDecimal getPrice();

        BigDecimal getSize();

    }

}
