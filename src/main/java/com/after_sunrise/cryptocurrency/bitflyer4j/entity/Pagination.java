package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Pagination {

    Integer getCount();

    Long getBefore();

    Long getAfter();

}
