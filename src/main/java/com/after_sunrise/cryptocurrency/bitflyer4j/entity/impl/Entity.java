package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Entity class for providing meta-data and boilerplate codes.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
class Entity implements Comparable<Entity> {

    private static final AtomicLong COUNT = new AtomicLong();

    private final long systemTime = System.currentTimeMillis();

    private final long systemCount = COUNT.incrementAndGet();

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemTime, systemCount);
    }

    @Override
    public boolean equals(Object o) {
        return getClass().isInstance(o) && compareTo(getClass().cast(o)) == 0;
    }

    @Override
    public int compareTo(Entity o) {

        CompareToBuilder b = new CompareToBuilder();

        b.append(systemTime, o.systemTime);

        b.append(systemCount, o.systemCount);

        return b.toComparison();

    }

}
