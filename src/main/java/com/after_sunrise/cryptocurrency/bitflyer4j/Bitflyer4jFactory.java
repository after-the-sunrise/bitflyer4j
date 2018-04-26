package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.impl.*;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.MarketService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.RealtimeService;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.AccountServiceImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.MarketServiceImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.OrderServiceImpl;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.impl.PubNubServiceImpl;
import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.pubnub.api.PubNub;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.event.EventSource;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 * Factory for creating a new {@link Bitflyer4j} instance.
 *
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class Bitflyer4jFactory {

    /**
     * Create a new instance of the {@link Bitflyer4j}.
     * A new instance is created for each invocation.
     *
     * @return New instance.
     */
    public Bitflyer4j createInstance() {
        return createInstance(null);
    }

    /**
     * Create a new instance of the {@link Bitflyer4j} with instance specific properties.
     * A new instance is created for each invocation.
     *
     * @param properties Instance specific properties.
     * @return New instance.
     */
    public Bitflyer4j createInstance(Properties properties) {

        log.info("Creating instance.");

        final AbstractConfiguration conf = createConfiguration(properties);

        Module module = new AbstractModule() {
            @Override
            protected void configure() {

                bind(Configuration.class).toInstance(conf);

                bind(EventSource.class).toInstance(conf);

                bind(Gson.class).toProvider(GsonProvider.class).asEagerSingleton();

                bind(PubNub.class).toProvider(PubNubProvider.class).asEagerSingleton();

                bind(Environment.class).to(EnvironmentImpl.class).asEagerSingleton();

                bind(Throttler.class).to(ThrottlerImpl.class).asEagerSingleton();

                bind(ExecutorFactory.class).to(ExecutorFactoryImpl.class).asEagerSingleton();

                bind(HttpClient.class).to(HttpClientImpl.class).asEagerSingleton();

                bind(Bitflyer4j.class).to(Bitflyer4jImpl.class).asEagerSingleton();

                bind(MarketService.class).to(MarketServiceImpl.class).asEagerSingleton();

                bind(AccountService.class).to(AccountServiceImpl.class).asEagerSingleton();

                bind(OrderService.class).to(OrderServiceImpl.class).asEagerSingleton();

                bind(RealtimeService.class).to(PubNubServiceImpl.class).asEagerSingleton();

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
    AbstractConfiguration createConfiguration(Properties properties) {

        CompositeConfiguration composite = new CompositeConfiguration();

        ConfigurationType[] types = ConfigurationType.values();

        Arrays.stream(types).forEach(s -> s.get().ifPresent(composite::addConfiguration));

        Optional.ofNullable(properties).ifPresent(p -> composite.addConfiguration(new MapConfiguration(p)));

        return composite;

    }

}
