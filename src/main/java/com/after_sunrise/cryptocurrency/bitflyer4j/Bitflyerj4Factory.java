package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Bitflyer4jImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConfigurationType;
import com.google.common.annotations.VisibleForTesting;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;

import java.util.Arrays;

/**
 * Factory for creating a new {@link Bitflyer4j} instance.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
public class Bitflyerj4Factory {

    /**
     * Create a new instance of the {@link Bitflyer4j}.
     * A new instance is created for each invocation.
     *
     * @return New instance.
     */
    public Bitflyer4j createInstance() {

        final Configuration conf = createConfiguration();

        Module module = new AbstractModule() {
            @Override
            protected void configure() {

                bind(Configuration.class).toInstance(conf);

                bind(Bitflyer4j.class).to(Bitflyer4jImpl.class);

            }
        };

        return Guice.createInjector(module).getInstance(Bitflyer4j.class);

    }

    /**
     * Create a {@link Configuration} instance,
     * composed of multiple configurations which are enumerated in {@link ConfigurationType}.
     *
     * @return Composite configuration instance.
     */
    @VisibleForTesting
    Configuration createConfiguration() {

        CompositeConfiguration composite = new CompositeConfiguration();

        ConfigurationType[] types = ConfigurationType.values();

        Arrays.stream(types).forEach(s -> s.get().ifPresent(composite::addConfiguration));

        return composite;

    }

}
