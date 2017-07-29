package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class Pagination {

    @SerializedName("count")
    private final Integer count;

    @SerializedName("before")
    private final Integer before;

    @SerializedName("after")
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

            EqualsBuilder b = new EqualsBuilder();
            b.append(getCount(), other.getCount());
            b.append(getBefore(), other.getBefore());
            b.append(getAfter(), other.getAfter());
            return b.isEquals();

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
