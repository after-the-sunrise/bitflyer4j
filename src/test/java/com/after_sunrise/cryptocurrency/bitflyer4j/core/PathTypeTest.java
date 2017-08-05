package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.GET;
import static com.after_sunrise.cryptocurrency.bitflyer4j.core.MethodType.POST;
import static org.testng.Assert.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class PathTypeTest {

    @Test
    public void test() {

        Set<String> paths = new HashSet<>();

        for (PathType type : PathType.values()) {

            // Check Duplicate
            assertTrue(paths.add(type.getPath()), type.toString());

            switch (type) {
                case MARKET:
                case BOARD:
                case TICKER:
                case EXECUTION:
                case HEALTH:
                case CHAT:

                    // Public API - GET

                    assertEquals(type.getMethod(), GET, type.toString());

                    assertFalse(type.isSign(), type.toString());

                    break;

                case PERMISSION:
                case BALANCE:
                case COLLATERAL:
                case MARGIN:
                case ADDRESS:
                case COIN_IN:
                case COIN_OUT:
                case BANK:
                case DEPOSIT:
                case WITHDRAWAL:
                case ORDER_LIST:
                case PARENT_LIST:
                case PARENT_DETAIL:
                case TRADE_EXECUTION:
                case TRADE_POSITION:
                case TRADE_COLLATERAL:
                case TRADE_COMMISSION:

                    // Private API - GET

                    assertEquals(type.getMethod(), GET, type.toString());

                    assertTrue(type.isSign(), type.toString());

                    break;

                case WITHDRAW:
                case ORDER_SEND:
                case ORDER_CANCEL:
                case PARENT_SEND:
                case PARENT_CANCEL:
                case PRODUCT_CANCEL:

                    // Private API - POST

                    assertEquals(type.getMethod(), POST, type.toString());

                    assertTrue(type.isSign(), type.toString());

                    break;

                default:
                    fail("Undefined : " + type);
            }


        }


    }

}