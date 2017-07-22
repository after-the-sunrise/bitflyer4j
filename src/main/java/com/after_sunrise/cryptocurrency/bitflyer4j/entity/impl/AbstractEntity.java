package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;

import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Base class for providing the boilerplate codes across all {@link Entity} implementations.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
abstract class AbstractEntity implements Entity {

    private static final Comparator<String> COMPARATOR = nullsLast(naturalOrder());

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

        Class<? extends AbstractEntity> clz = getClass();

        if (clz.isInstance(o)) {
            return Objects.equals(getKey(), clz.cast(o).getKey());
        }

        return false;

    }

    @Override
    public int compareTo(Entity o) {
        return Objects.compare(getKey(), o.getKey(), COMPARATOR);
    }

}
