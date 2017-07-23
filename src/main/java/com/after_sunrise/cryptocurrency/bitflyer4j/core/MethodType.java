package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.function.Supplier;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public enum MethodType implements Supplier<String> {

    GET, POST;

    @Override
    public String get() {
        return name();
    }

}
