package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.pubnub.api.enums.PNReconnectionPolicy;
import lombok.Getter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;
import java.util.function.Function;

import static java.lang.Long.parseLong;
import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Enumeration of property keys.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public enum KeyType {

    /**
     * Build version.
     */
    VERSION("0.0.0"),

    /**
     * Site ID.
     */
    SITE(null),

    /**
     * Authentication key.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#authentication">API Documentation</a>
     * @see #AUTH_SECRET
     */
    AUTH_KEY(null),

    /**
     * Authentication secret.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#authentication">API Documentation</a>
     * @see #AUTH_KEY
     */
    AUTH_SECRET(null),

    /**
     * Endpoint URL base of the HTTP API.
     */
    HTTP_URL("https://api.bitflyer.jp"),

    /**
     * HTTP proxy type.
     *
     * @see java.net.Proxy.Type
     */
    HTTP_PROXY_TYPE(null, Proxy.Type::valueOf) {
        @Override
        public Object fetch(Configuration conf) throws RuntimeException {

            Proxy.Type type = (Proxy.Type) super.fetch(conf);

            if (type == null) {
                return null;
            }

            if (type == Proxy.Type.DIRECT) {
                return Proxy.NO_PROXY;
            }

            String host = (String) HTTP_PROXY_HOST.fetch(conf);

            Integer port = (Integer) HTTP_PROXY_PORT.fetch(conf);

            return new Proxy(type, new InetSocketAddress(host, port));

        }
    },

    /**
     * HTTP proxy host.
     */
    HTTP_PROXY_HOST(null),

    /**
     * HTTP proxy port.
     */
    HTTP_PROXY_PORT(null, Integer::parseInt),

    /**
     * HTTP socket/read timeout in millis.
     */
    HTTP_TIMEOUT(null, v -> ofMillis(parseLong(v))),

    /**
     * HTTP API access limit's interval in milliseconds.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#api-limits">API Documentation</a>
     * @see #HTTP_LIMIT_CRITERIA_ADDRESS
     * @see #HTTP_LIMIT_CRITERIA_PRIVATE
     * @see #HTTP_LIMIT_CRITERIA_DORMANT
     */
    HTTP_LIMIT_INTERVAL(SECONDS.toMillis(60), v -> ofMillis(parseLong(v))),

    /**
     * HTTP API access limit per IP address.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_ADDRESS(500, Integer::parseInt),

    /**
     * HTTP API access limit the private APIs.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_PRIVATE(200, Integer::parseInt),

    /**
     * HTTP API access limit for users with an average of less than 0.01 BTC per day.
     * The limit is imposed on the following day.
     *
     * @see #HTTP_LIMIT_INTERVAL
     */
    HTTP_LIMIT_CRITERIA_DORMANT(10, Integer::parseInt),

    /**
     * <a href="https://www.pubnub.com/">PubNub</a> subscribe key for the Realtime API.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#realtime-api">API Documentation</a>
     */
    PUBNUB_KEY("sub-c-52a9ab50-291b-11e5-baaa-0619f8945a4f"),

    /**
     * PubNub reconnect policy.
     *
     * @see <a href="https://www.pubnub.com/docs/java-se-java/api-reference-publish-and-subscribe#subscribe">PubNub Reference</a>
     */
    PUBNUB_RECONNECT(PNReconnectionPolicy.LINEAR.name(), PNReconnectionPolicy::valueOf),

    /**
     * PubNub SSL flag ("https://").
     *
     * @see <a href="https://www.pubnub.com/docs/java-se-java/api-reference-misc#reconnect">PubNub Reference</a>
     */
    PUBNUB_SECURE(true, Boolean::valueOf);

    private static final String PREFIX = "bitflyer4j.";

    @Getter
    private final String key;

    @Getter
    private final String defaultValue;

    private final Function<String, ?> function;

    KeyType(Object defaultValue) {
        this(defaultValue, value -> value);
    }

    KeyType(Object defaultValue, Function<String, ?> function) {

        this.key = PREFIX + name().toLowerCase();

        this.defaultValue = Objects.toString(defaultValue, null);

        this.function = function;

    }

    public Object fetch(Configuration conf) throws RuntimeException {

        String value = conf == null ? defaultValue : conf.getString(key, defaultValue);

        return StringUtils.isEmpty(value) ? null : function.apply(value);

    }

}
