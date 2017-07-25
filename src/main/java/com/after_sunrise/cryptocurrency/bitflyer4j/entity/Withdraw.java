package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Withdraw extends Entity<String, Withdraw> {

    class Request {

        @SerializedName("currency_code")
        private final String currency;

        @SerializedName("bank_account_id")
        private final Long bank;

        @SerializedName("amount")
        private final BigDecimal amount;

        @SerializedName("code")
        private final String pin;

        public Request(String currency, Long bank, BigDecimal amount, String pin) {
            this.currency = currency;
            this.bank = bank;
            this.amount = amount;
            this.pin = pin;
        }

        @Override
        public String toString() {
            return reflectionToString(this, SHORT_PREFIX_STYLE);
        }

        public String getCurrency() {
            return currency;
        }

        public Long getBank() {
            return bank;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getPin() {
            return pin;
        }

    }


    String getStatus();

    String getMessage();

    String getData();

}
