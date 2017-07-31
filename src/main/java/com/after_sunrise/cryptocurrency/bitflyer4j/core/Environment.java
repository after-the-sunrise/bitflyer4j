package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.net.Proxy;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Environment {

    long getTimeMillis();

    Proxy getProxy();

}
