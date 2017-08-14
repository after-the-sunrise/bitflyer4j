package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import lombok.Getter;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * @author takanori.takase
 * @version 0.0.6
 */
public enum ProductType {

    JPY(new BigDecimal("1")),

    BTC(new BigDecimal("0.00000001")),

    BCH(new BigDecimal("0.00000001")),

    ETH(new BigDecimal("0.00000001")),

    ETC(new BigDecimal("0.00000001")),

    LTC(new BigDecimal("0.00000001")),

    BTC_JPY(BTC, JPY, new BigDecimal("0.001")),

    BCH_JPY(BCH, JPY, new BigDecimal("0.01")),

    BCH_BTC(BCH, BTC, new BigDecimal("0.01")),

    ETH_BTC(ETH, BTC, new BigDecimal("0.01")),

    COLLATERAL_JPY(new BigDecimal("1")),

    COLLATERAL_BTC(new BigDecimal("0.00000001")),

    FX_BTC(new BigDecimal("0.001")),

    FX_BTC_JPY(FX_BTC, COLLATERAL_JPY, new BigDecimal("0.001")),

    BTC1W(new BigDecimal("0.001")),

    BTC2W(new BigDecimal("0.001")),

    BTCJPY_MAT1WK(BTC1W, COLLATERAL_JPY, new BigDecimal("0.001")),

    BTCJPY_MAT2WK(BTC2W, COLLATERAL_JPY, new BigDecimal("0.001"));

    private static Map<String, ProductType> NAMES = stream(values()).collect(toMap(ProductType::name, t -> t));

    public static ProductType find(String name) {
        return NAMES.get(name);
    }

    @Getter
    private final ProductType structure;

    @Getter
    private final ProductType funding;

    @Getter
    private final BigDecimal lotSize;

    ProductType(BigDecimal lotSize) {
        this.structure = this;
        this.funding = this;
        this.lotSize = lotSize;
    }

    ProductType(ProductType structure, ProductType funding, BigDecimal lotSize) {
        this.structure = structure;
        this.funding = funding;
        this.lotSize = lotSize;
    }

    public BigDecimal roundToLotSize(BigDecimal value, RoundingMode mode) {

        if (value == null || mode == null) {
            return null;
        }

        BigDecimal units = value.divide(lotSize, NumberUtils.INTEGER_ZERO, mode);

        return units.multiply(lotSize);

    }

}
