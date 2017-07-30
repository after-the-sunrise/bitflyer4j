package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.Resources;
import lombok.Getter;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

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
    SYSTEM,

    /**
     * User properties.
     */
    HOME(".bitflyer4j", System.getProperty("user.home")),

    /**
     * Site properties.
     */
    SITE("bitflyer4j-site.properties");

    @Getter
    private final String path;

    @Getter
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
    public Optional<Configuration> get() {
        return get(getPath(), getParentPath());
    }

    @VisibleForTesting
    static Optional<Configuration> get(String location, String parentPath) {

        Logger log = LoggerFactory.getLogger(ConfigurationType.class);

        if (StringUtils.isBlank(location)) {

            log.debug("Loading system.");

            return Optional.of(new SystemConfiguration());

        }

        try {

            URL url;

            if (StringUtils.isBlank(parentPath)) {

                url = Resources.getResource(location);

                log.debug("Loading classpath : {}", url);

            } else {

                Path parent = Paths.get(parentPath);

                url = parent.resolve(location).toUri().toURL();

                log.debug("Loading filepath : {}", url);

            }

            return Optional.of(new Configurations().properties(url));

        } catch (Exception e) {

            log.debug("Loading skipped : location=[{}], parenPath=[{}]", location, parentPath);

            return Optional.empty();

        }

    }

}
