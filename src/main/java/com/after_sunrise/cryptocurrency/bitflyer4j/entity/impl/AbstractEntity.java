package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;

import java.util.Objects;

import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Base class for providing the boilerplate codes across all {@link Entity} implementations.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
abstract class AbstractEntity<K extends Comparable<? super K>, V extends Entity<K, V>> implements Entity<K, V> {

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getKey());
    }

    @Override
    public boolean equals(Object o) {

        Class<? extends Entity> clz = getClass();

        if (clz.isInstance(o)) {
            return Objects.equals(getKey(), clz.cast(o).getKey());
        }

        return false;

    }

    @Override
    public int compareTo(V o) {

        K self = getKey();

        K other = o.getKey();

        if (self == other) {
            return 0;
        }

        if (self == null) {
            return -1;
        }

        if (other == null) {
            return +1;
        }

        return self.compareTo(other);

    }

}
