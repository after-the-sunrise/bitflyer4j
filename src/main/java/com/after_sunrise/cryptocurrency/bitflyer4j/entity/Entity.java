package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Entity extends Comparable<Entity> {

    /**
     * Key to uniqely identify this instance within the same type.
     *
     * @return Unique key.
     */
    String getKey();

}
