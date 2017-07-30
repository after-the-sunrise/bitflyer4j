package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Withdraw extends Entity {

    @SerializedName("currency_code")
    private final String currency;

    @SerializedName("bank_account_id")
    private final Long bank;

    @SerializedName("amount")
    private final BigDecimal amount;

    @SerializedName("code")
    private final String pin;

    public interface Response {

        String getId();

        Integer getStatus();

        String getMessage();

        String getData();

    }

}
