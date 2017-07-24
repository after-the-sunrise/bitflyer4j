package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Entity<K extends Comparable<? super K>, V extends Entity<K, V>> extends Comparable<V> {

    /**
     * Key to uniqely identify this instance within the same type.
     *
     * @return Unique key.
     */
    K getKey();

}
