package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Bank {

    Long getId();

    Boolean getVerified();

    String getName();

    String getBranch();

    String getType();

    String getNumber();

    String getAccount();

}
