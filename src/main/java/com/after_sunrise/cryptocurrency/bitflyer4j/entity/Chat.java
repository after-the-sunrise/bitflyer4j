package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import java.time.ZonedDateTime;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface Chat {

    String getName();

    String getMessage();

    ZonedDateTime getTimestamp();

}
