package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.ProductType.BTC;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.ProductType.find;
import static java.math.RoundingMode.*;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.6
 */
public class ProductTypeTest {

    @Test
    public void testFind() throws Exception {

        for (ProductType type : ProductType.values()) {

            assertSame(find(type.name()), type);

            assertNotNull(type.getStructure());

            assertNotNull(type.getFunding());

            assertNotNull(type.getLotSize());

            assertNotEquals(type.getLotSize().signum(), INTEGER_ZERO);

        }

        assertNull(find(null));

        assertNull(find("hoge"));

    }

    @Test
    public void testRoundToLotSize() throws Exception {

        BigDecimal value = new BigDecimal("0.000000015");

        assertEquals(BTC.roundToLotSize(value, UP), new BigDecimal("0.00000002"));
        assertEquals(BTC.roundToLotSize(value, HALF_UP), new BigDecimal("0.00000002"));

        assertEquals(BTC.roundToLotSize(value, DOWN), new BigDecimal("0.00000001"));
        assertEquals(BTC.roundToLotSize(value, HALF_DOWN), new BigDecimal("0.00000001"));

        assertNull(BTC.roundToLotSize(null, DOWN));
        assertNull(BTC.roundToLotSize(value, null));
        assertNull(BTC.roundToLotSize(null, null));

    }

}
