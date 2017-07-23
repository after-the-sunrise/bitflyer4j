package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Product extends Entity {

    /**
     * Alternative name for the {@link #getKey()}.
     *
     * @return Alias, or {@code null} if no alias is defined.
     */
    String getAlias();

}
