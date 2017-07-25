package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Address extends Entity<String, Address> {

    String getCurrency();

    String getType();

}
