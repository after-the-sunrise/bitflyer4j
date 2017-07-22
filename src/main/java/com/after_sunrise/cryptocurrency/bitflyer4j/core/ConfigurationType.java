package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Resources;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

import static org.apache.commons.lang.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * Enumeration of properties files to load.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public enum ConfigurationType implements Supplier<Optional<Configuration>> {

    /**
     * Library version.
     */
    VERSION("bitflyer4j-version.properties"),

    /**
     * System variables.
     */
    SYSTEM {
        @Override
        public Optional<Configuration> get() {
            return Optional.of(new SystemConfiguration());
        }
    },

    /**
     * User properties.
     */
    HOME(".bitflyer4j", System.getProperty("user.home")),

    /**
     * Site properties.
     */
    SITE("bitflyer4j-site.properties");

    private final String path;

    private final String parentPath;

    ConfigurationType() {
        this(null);
    }

    ConfigurationType(String path) {
        this(path, null);
    }

    ConfigurationType(String path, String parentPath) {
        this.path = path;
        this.parentPath = parentPath;
    }

    @Override
    public String toString() {
        return reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public Optional<Configuration> get() {
        return get(path, parentPath);
    }

    @VisibleForTesting
    static Optional<Configuration> get(String location, String parentPath) {

        try {

            URL url;

            if (StringUtils.isBlank(parentPath)) {

                url = Resources.getResource(location);

            } else {

                Path parent = Paths.get(parentPath);

                url = parent.resolve(location).toUri().toURL();

            }

            return Optional.of(new PropertiesConfiguration(url));

        } catch (Exception e) {

            return Optional.empty();

        }

    }

}
