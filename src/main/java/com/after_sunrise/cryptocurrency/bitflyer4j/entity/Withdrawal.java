package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.WithdrawalStatusType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Withdrawal {

    Long getId();

    String getOrderId();

    String getCurrency();

    BigDecimal getAmount();

    WithdrawalStatusType getStatus();

    ZonedDateTime getEventDate();

}
