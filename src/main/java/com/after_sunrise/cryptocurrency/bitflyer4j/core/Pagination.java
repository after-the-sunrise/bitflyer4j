package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Pagination {

    private final Integer count;

    private final Integer before;

    private final Integer after;

    public Pagination(Integer count) {
        this(count, null, null);
    }

    public Pagination(Integer count, Integer before, Integer after) {
        this.count = count;
        this.before = before;
        this.after = after;
    }

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCount(), getBefore(), getAfter());
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Pagination) {

            Pagination other = (Pagination) o;

            return Objects.equals(getCount(), other.getCount())  //
                    && Objects.equals(getBefore(), other.getBefore()) //
                    && Objects.equals(getAfter(), other.getAfter());

        }

        return false;

    }

    public Integer getCount() {
        return count;
    }

    public Integer getBefore() {
        return before;
    }

    public Integer getAfter() {
        return after;
    }

}
