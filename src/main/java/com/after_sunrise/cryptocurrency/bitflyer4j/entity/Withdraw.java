package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Withdraw extends Entity {

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("bank_account_id")
    private final Long bank;

    @SerializedName("amount")
    private final BigDecimal amount;

    @SerializedName("code")
    private final String pin;

    public Withdraw(String currency, Long bank, BigDecimal amount, String pin) {
        this.currency = currency;
        this.bank = bank;
        this.amount = amount;
        this.pin = pin;
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

    public static class Builder {

        private String currency;

        private Long bank;

        private BigDecimal amount;

        private String pin;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Long getBank() {
            return bank;
        }

        public void setBank(Long bank) {
            this.bank = bank;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public Withdraw build() {
            return new Withdraw(currency, bank, amount, pin);
        }

    }

    public interface Response {

        String getId();

        Integer getStatus();

        String getMessage();

        String getData();

    }

}
