package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.ConfigurationType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.ExecutorFactory;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.Bitflyer4jImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.ExecutorFactoryImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.HttpClientImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.MarketServiceImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.OrderServiceImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
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
public class Bitflyer4jFactory {

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

                bind(Gson.class).toInstance(new Gson());

                bind(ExecutorFactory.class).to(ExecutorFactoryImpl.class).asEagerSingleton();

                bind(HttpClient.class).to(HttpClientImpl.class).asEagerSingleton();

                bind(Bitflyer4j.class).to(Bitflyer4jImpl.class).asEagerSingleton();

                bind(MarketService.class).to(MarketServiceImpl.class).asEagerSingleton();

                bind(OrderService.class).to(OrderServiceImpl.class).asEagerSingleton();

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
