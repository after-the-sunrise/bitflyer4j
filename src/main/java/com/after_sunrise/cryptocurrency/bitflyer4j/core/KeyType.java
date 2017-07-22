package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import org.apache.commons.configuration.Configuration;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Enumeration of property keys.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public enum KeyType implements Supplier<String>, Function<Configuration, String> {

    /**
     * Build version.
     */
    VERSION("0.0.0"),

    /**
     * Authentication key.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#authentication">API Documentation</a>
     * @see #AUTH_SECRET
     */
    AUTH_KEY("N/A"),

    /**
     * Authentication secret.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#authentication">API Documentation</a>
     * @see #AUTH_KEY
     */
    AUTH_SECRET("N/A"),

    /**
     * Endpoint URL base of the HTTP API.
     */
    HTTP_URL_BASE("https://api.bitflyer.jp"),

    /**
     * Endpoint URL suffix for market request.
     */
    HTTP_URL_MARKET("/v1/markets"),

    /**
     * HTTP API access limit's interval.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#api-limits">API Documentation</a>
     * @see #HTTP_LIMIT_TIMEUNIT
     * @see #HTTP_LIMIT_CRITERIA_ADDRESS
     * @see #HTTP_LIMIT_CRITERIA_PRIVATE
     * @see #HTTP_LIMIT_CRITERIA_DORMANT
     */
    HTTP_LIMIT_INTERVAL(60),

    /**
     * HTTP API access limit interval's time unit.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_TIMEUNIT(SECONDS.name()),

    /**
     * HTTP API access limit per IP address.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_ADDRESS(500),

    /**
     * HTTP API access limit the private APIs.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_PRIVATE(200),

    /**
     * HTTP API access limit for users with an average of less than 0.01 BTC per day.
     * The limit is imposed on the following day.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_DORMANT(10),

    /**
     * <a href="https://www.pubnub.com/">PubNub</a> subscribe key for the Realtime API.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#realtime-api">API Documentation</a>
     */
    @SuppressWarnings("SpellCheckingInspection") PUBNUB_KEY("sub-c-52a9ab50-291b-11e5-baaa-0619f8945a4f");

    private static final String PREFIX = "bitflyer4j.";

    private final String key;

    private final String defaultValue;

    KeyType(Object defaultValue) {

        this.key = PREFIX + name().toLowerCase();

        this.defaultValue = defaultValue.toString();

    }

    /**
     * Key of the property.
     *
     * @return Properties key.
     */
    @Override
    public String get() {
        return key;
    }

    /**
     * Default value, in case the key is not found.
     *
     * @return Default value.
     */
    public String getDefault() {
        return defaultValue;
    }

    /**
     * Retrieve a properties value from the given {@link Configuration} instance.
     *
     * @param configuration Configuration to retrieve the value from.
     * @return Property value. Value from {@link #getDefault()} is returned,
     * if the key is not found, or if the given instance is {@code null}.
     */
    @Override
    public String apply(Configuration configuration) {
        return configuration == null ? getDefault() : configuration.getString(get(), getDefault());
    }

}
