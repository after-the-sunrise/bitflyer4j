package com.after_sunrise.cryptocurrency.bitflyer4j.core;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Throttler {

    void throttleAddress();

    void throttlePrivate();

}
