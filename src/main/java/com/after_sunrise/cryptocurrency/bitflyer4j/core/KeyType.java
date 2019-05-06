package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import lombok.Getter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Objects;
import java.util.function.Function;

import static java.lang.Long.parseLong;
import static java.time.Duration.ofMillis;
import static java.util.concurrent.TimeUnit.MINUTES;

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
    SITE("local"),

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
    HTTP_TIMEOUT(MINUTES.toMillis(3), v -> ofMillis(parseLong(v))),

    /**
     * HTTP threads for concurrent requests.
     */
    HTTP_THREADS(8, Integer::parseInt),

    /**
     * HTTP API access limit's interval in milliseconds.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs?lang=en#api-limits">API Documentation</a>
     * @see #HTTP_LIMIT_CRITERIA_ADDRESS
     * @see #HTTP_LIMIT_CRITERIA_PRIVATE
     */
    HTTP_LIMIT_INTERVAL(MINUTES.toMillis(5), v -> ofMillis(parseLong(v))),

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
    HTTP_LIMIT_CRITERIA_PRIVATE(500, Integer::parseInt),

    /**
     * Implementaion type to utilize for realtime subcription.
     *
     * @see #SOCKET_ENDPOINT
     */
    REALTIME_TYPE(null),

    /**
     * Socket.IO 2.0 WebSocket Endpoint.
     *
     * @see <a href="https://lightning.bitflyer.jp/docs#realtime-api">Socket.IO 2.0 (WebSocket)</a>
     */
    SOCKET_ENDPOINT("https://io.lightstream.bitflyer.com");

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
